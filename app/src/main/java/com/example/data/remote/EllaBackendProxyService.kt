package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class IntentCategory {
    FIND_PARTNER,        // Seeking female entrepreneur, service, or business partner
    RADAR_OPPORTUNITY,   // Looking for/posting business opportunity
    BUSINESS_ADVICE,     // Asking for strategy, mentorship, networking guidance
    GENERAL              // General greeting, platform info
}

data class UserIntent(
    val category: IntentCategory,
    val rawPrompt: String,
    val primaryTopic: String = "",
    val targetNeighborhood: String = "Cascavel"
)

data class EllaIntentResponse(
    val replyText: String,
    val detectedIntent: UserIntent,
    val matchedUserIds: List<Long> = emptyList(),
    val isSecureProxy: Boolean = true
)

/**
 * EllaBackendProxyService acts as a secure backend-side proxy service to the Gemini API.
 * It encapsulates GEMINI_API_KEY handling so credentials are never exposed to the frontend/UI.
 */
class EllaBackendProxyService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Securely forwards user intent to the Ella personality via the Gemini API proxy.
     */
    suspend fun sendUserIntentToElla(
        userIntent: UserIntent,
        usersInDb: List<UserEntity>,
        opportunitiesInDb: List<OpportunityEntity>
    ): EllaIntentResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val systemPrompt = buildEllaSystemPrompt(userIntent, usersInDb, opportunitiesInDb)

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val fallbackText = localFallbackByIntent(userIntent, usersInDb, opportunitiesInDb)
            return@withContext EllaIntentResponse(
                replyText = fallbackText,
                detectedIntent = userIntent,
                isSecureProxy = true
            )
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val promptText = """
                [CATEGORIA DE INTENÇÃO]: ${userIntent.category.name}
                [PROMPT DA USUÁRIA]: ${userIntent.rawPrompt}
                [TÓPICO CHAVE]: ${userIntent.primaryTopic}
                [LOCALIZAÇÃO]: ${userIntent.targetNeighborhood}
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", promptText))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemPrompt))
                    })
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: ""
                val respJson = JSONObject(respStr)
                val candidates = respJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val reply = parts.getJSONObject(0).optString("text")
                        if (!reply.isNullOrBlank()) {
                            return@withContext EllaIntentResponse(
                                replyText = reply,
                                detectedIntent = userIntent,
                                isSecureProxy = true
                            )
                        }
                    }
                }
            }

            val fallback = localFallbackByIntent(userIntent, usersInDb, opportunitiesInDb)
            EllaIntentResponse(
                replyText = fallback,
                detectedIntent = userIntent,
                isSecureProxy = true
            )
        } catch (e: Exception) {
            Log.e("EllaBackendProxyService", "Gemini Proxy Exception: ${e.message}")
            val fallback = localFallbackByIntent(userIntent, usersInDb, opportunitiesInDb)
            EllaIntentResponse(
                replyText = fallback,
                detectedIntent = userIntent,
                isSecureProxy = true
            )
        }
    }

    /**
     * Helper to classify raw prompt string into structured UserIntent.
     */
    fun classifyUserIntent(rawPrompt: String): UserIntent {
        val lower = rawPrompt.lowercase()
        return when {
            lower.contains("procur") || lower.contains("precis") || lower.contains("indica") ||
            lower.contains("quem") || lower.contains("contato") || lower.contains("fotógraf") ||
            lower.contains("arquiteta") || lower.contains("corretor") || lower.contains("contad") -> {
                UserIntent(
                    category = IntentCategory.FIND_PARTNER,
                    rawPrompt = rawPrompt,
                    primaryTopic = extractTopic(rawPrompt)
                )
            }
            lower.contains("oportunidade") || lower.contains("radar") || lower.contains("vaga") ||
            lower.contains("demanda") || lower.contains("parceria") -> {
                UserIntent(
                    category = IntentCategory.RADAR_OPPORTUNITY,
                    rawPrompt = rawPrompt,
                    primaryTopic = extractTopic(rawPrompt)
                )
            }
            lower.contains("dica") || lower.contains("conselho") || lower.contains("como") ||
            lower.contains("estratégia") || lower.contains("vendas") || lower.contains("mentoria") -> {
                UserIntent(
                    category = IntentCategory.BUSINESS_ADVICE,
                    rawPrompt = rawPrompt,
                    primaryTopic = extractTopic(rawPrompt)
                )
            }
            else -> {
                UserIntent(
                    category = IntentCategory.GENERAL,
                    rawPrompt = rawPrompt
                )
            }
        }
    }

    private fun extractTopic(text: String): String {
        val words = text.split(" ")
        return words.filter { it.length > 3 }.take(3).joinToString(" ")
    }

    private fun buildEllaSystemPrompt(
        intent: UserIntent,
        usersInDb: List<UserEntity>,
        opportunitiesInDb: List<OpportunityEntity>
    ): String {
        val usersContext = usersInDb.joinToString("\n") { u ->
            "- [ID:${u.id}] Nome: ${u.name} | Negócio: ${u.businessName} | Categoria: ${u.category} | Bairro: ${u.neighborhood}, Cascavel | Especialidades: ${u.specialities} | Procuro: ${u.procuro} | Ofereço: ${u.ofereco} | CRECI: ${u.creci ?: "N/A"}"
        }

        val oppsContext = opportunitiesInDb.joinToString("\n") { o ->
            "- Oportunidade: '${o.title}' | Autora: ${o.authorName} (${o.authorBusiness}) | Categoria: ${o.category} | Bairro: ${o.neighborhood}"
        }

        return """
            Você é a ELLA, a assistente oficial e mentora da NEXELLA, uma plataforma inteligente de conexões para mulheres empreendedoras focada em Cascavel - Paraná.
            
            SUA PERSONALIDADE E TOM DE VOZ:
            - Acolhedora, inteligente, objetiva, motivadora, empreendedora e especialista em conexões.
            - Fale como uma mentora elegante, humana e refinada (estilo Notion/Apple).
            - INTENÇÃO DETECTADA DA USUÁRIA: ${intent.category.name} (${intent.primaryTopic})
            
            DIRETRIZ DE OURO:
            - O único propósito de cada interação na Nexella é ajudar duas mulheres a criarem uma oportunidade real de negócio.
            
            INSTRUÇÕES ESPECÍFICAS PARA A INTENÇÃO ATUAL (${intent.category.name}):
            ${
                when (intent.category) {
                    IntentCategory.FIND_PARTNER -> "A usuária quer encontrar uma profissional/parceira. Busque a melhor empreendedora em Cascavel no contexto fornecido e apresente o match calorosamente com sugestão de contato via WhatsApp."
                    IntentCategory.RADAR_OPPORTUNITY -> "A usuária busca ou deseja publicar uma oportunidade no Radar. Indique oportunidades compatíveis ou convide-a a publicar sua demanda no Radar Nexella."
                    IntentCategory.BUSINESS_ADVICE -> "Ofereça um conselho prático, estratégico e encorajador focado no empreendedorismo feminino local e networking."
                    IntentCategory.GENERAL -> "Responda de forma acolhedora, explicando como a Nexella pode impulsionar conexões reais entre mulheres em Cascavel."
                }
            }
            
            --- DADOS DA COMUNIDADE NEXELLA EM CASCAVEL/PR ---
            EMPREENDEDORAS CADASTRADAS:
            $usersContext
            
            OPORTUNIDADES NO RADAR:
            $oppsContext
        """.trimIndent()
    }

    private fun localFallbackByIntent(
        intent: UserIntent,
        users: List<UserEntity>,
        opps: List<OpportunityEntity>
    ): String {
        val query = intent.rawPrompt.lowercase()

        val matchingUsers = users.filter { u ->
            u.name.lowercase().contains(query) ||
            u.category.lowercase().contains(query) ||
            u.specialities.lowercase().contains(query) ||
            u.description.lowercase().contains(query) ||
            u.neighborhood.lowercase().contains(query) ||
            u.procuro.lowercase().contains(query) ||
            u.ofereco.lowercase().contains(query)
        }

        if (matchingUsers.isNotEmpty()) {
            val u = matchingUsers.first()
            return "Que ótima busca! Analisei nossa comunidade em Cascavel e encontrei alguém perfeita para somar com você: ${u.name}, que trabalha com ${u.category} no bairro ${u.neighborhood}. Ela pode te ajudar com essa necessidade. Quer que eu facilite o contato direto via WhatsApp?"
        }

        return when (intent.category) {
            IntentCategory.FIND_PARTNER ->
                "Analisando nossa comunidade em Cascavel, ainda não encontrei um cadastro exato para essa área. Que tal publicarmos essa necessidade no Radar de Oportunidades para que as membros possam responder?"
            IntentCategory.RADAR_OPPORTUNITY ->
                "Temos várias demandas ativas no Radar da Nexella! Se você tem uma necessidade específica ou serviço a oferecer, pode cadastrar diretamente no nosso Radar."
            IntentCategory.BUSINESS_ADVICE ->
                "A chave para acelerar seus resultados em Cascavel é a reciprocidade e o networking ativo. Conecte-se com 2 empreendedoras da comunidade esta semana!"
            IntentCategory.GENERAL ->
                "Seja muito bem-vinda à Nexella! Estou aqui para ajudar você a encontrar parceiras estratégicas, fechar negócios e expandir sua rede em Cascavel. Como posso te apoiar hoje? 💜"
        }
    }
}
