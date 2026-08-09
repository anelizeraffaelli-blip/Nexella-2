package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.entity.BusinessEntity
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ProfileEntity
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

data class SupabaseAuthResult(
    val success: Boolean,
    val userId: String? = null,
    val email: String? = null,
    val token: String? = null,
    val errorMessage: String? = null
)

/**
 * Service layer for Supabase integration using PostgREST and Supabase Auth APIs.
 * Ensures API keys are loaded strictly from BuildConfig / environment variables (.env).
 */
class SupabaseService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val supabaseUrl: String
        get() {
            return try {
                val url = BuildConfig.VITE_SUPABASE_URL
                if (url.isNotBlank() && !url.contains("your-supabase-project")) url
                else BuildConfig.SUPABASE_URL
            } catch (e: Exception) {
                ""
            }
        }

    val supabaseKey: String
        get() {
            return try {
                val key = BuildConfig.VITE_SUPABASE_PUBLISHABLE_KEY
                if (key.isNotBlank() && !key.contains("your-supabase-anon-key")) key
                else BuildConfig.SUPABASE_ANON_KEY
            } catch (e: Exception) {
                ""
            }
        }

    fun isConfigured(): Boolean {
        val url = supabaseUrl
        val key = supabaseKey
        return url.isNotBlank() &&
                key.isNotBlank() &&
                !url.contains("your-supabase-project") &&
                !key.contains("your-supabase-anon-key")
    }

    private fun buildHeaders(token: String? = null): Map<String, String> {
        val map = mutableMapOf(
            "apikey" to supabaseKey,
            "Authorization" to "Bearer ${token ?: supabaseKey}",
            "Content-Type" to "application/json",
            "Prefer" to "return=representation"
        )
        return map
    }

    // -------------------------------------------------------------
    // 1. AUTENTICAÇÃO E CADASTRO DE USUÁRIAS (SUPABASE AUTH)
    // -------------------------------------------------------------

    suspend fun signUp(email: String, pass: String, name: String): SupabaseAuthResult = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext SupabaseAuthResult(
                success = false,
                errorMessage = "Supabase não configurado. Utilizando fallback local."
            )
        }

        try {
            val url = "$supabaseUrl/auth/v1/signup"
            val bodyJson = JSONObject().apply {
                put("email", email)
                put("password", pass)
                put("data", JSONObject().put("full_name", name))
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.post(bodyJson.toString().toRequestBody("application/json".toMediaType())).build()

            val response = client.newCall(request).execute()
            val respStr = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(respStr)
                val userObj = json.optJSONObject("user")
                val uid = userObj?.optString("id") ?: json.optString("id")
                val token = if (json.has("access_token")) json.optString("access_token") else null
                SupabaseAuthResult(success = true, userId = uid, email = email, token = token)
            } else {
                val jsonErr = try { JSONObject(respStr) } catch (e: Exception) { null }
                val msg = jsonErr?.optString("msg") ?: jsonErr?.optString("error_description") ?: "Erro ao cadastrar no Supabase"
                SupabaseAuthResult(success = false, errorMessage = msg)
            }
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro no signUp: ${e.message}")
            SupabaseAuthResult(success = false, errorMessage = e.message ?: "Erro de rede no Supabase Auth")
        }
    }

    suspend fun signIn(email: String, pass: String): SupabaseAuthResult = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            return@withContext SupabaseAuthResult(
                success = false,
                errorMessage = "Supabase não configurado. Utilizando fallback local."
            )
        }

        try {
            val url = "$supabaseUrl/auth/v1/token?grant_type=password"
            val bodyJson = JSONObject().apply {
                put("email", email)
                put("password", pass)
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.post(bodyJson.toString().toRequestBody("application/json".toMediaType())).build()

            val response = client.newCall(request).execute()
            val respStr = response.body?.string() ?: ""

            if (response.isSuccessful) {
                val json = JSONObject(respStr)
                val userObj = json.optJSONObject("user")
                val uid = userObj?.optString("id") ?: ""
                val token = json.optString("access_token")
                SupabaseAuthResult(success = true, userId = uid, email = email, token = token)
            } else {
                val jsonErr = try { JSONObject(respStr) } catch (e: Exception) { null }
                val msg = jsonErr?.optString("error_description") ?: "Credenciais inválidas no Supabase"
                SupabaseAuthResult(success = false, errorMessage = msg)
            }
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro no signIn: ${e.message}")
            SupabaseAuthResult(success = false, errorMessage = e.message)
        }
    }

    // -------------------------------------------------------------
    // 2. PROFILES (CRIAÇÃO E EDIÇÃO)
    // -------------------------------------------------------------

    suspend fun upsertProfile(profile: ProfileEntity): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val url = "$supabaseUrl/rest/v1/profiles"
            val json = JSONObject().apply {
                put("user_id", profile.userId)
                put("name", profile.name)
                put("city", profile.city)
                put("neighborhood", profile.neighborhood)
                put("business_name", profile.businessName)
                put("category", profile.category)
                put("bio", profile.bio)
                put("instagram", profile.instagram)
                put("whatsapp", profile.whatsapp)
                put("email", profile.email)
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            requestBuilder.addHeader("Prefer", "resolution=merge-duplicates")

            val request = requestBuilder.post(json.toString().toRequestBody("application/json".toMediaType())).build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em upsertProfile: ${e.message}")
            false
        }
    }

    suspend fun fetchProfiles(): List<ProfileEntity>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val url = "$supabaseUrl/rest/v1/profiles?select=*"
            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: "[]"
                val jsonArr = JSONArray(respStr)
                val list = mutableListOf<ProfileEntity>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    list.add(
                        ProfileEntity(
                            id = obj.optLong("id", (i + 1).toLong()),
                            userId = obj.optLong("user_id", 0),
                            name = obj.optString("name", ""),
                            city = obj.optString("city", "Cascavel"),
                            neighborhood = obj.optString("neighborhood", "Centro"),
                            businessName = obj.optString("business_name", ""),
                            category = obj.optString("category", ""),
                            bio = obj.optString("bio", ""),
                            instagram = obj.optString("instagram", ""),
                            whatsapp = obj.optString("whatsapp", ""),
                            email = obj.optString("email", "")
                        )
                    )
                }
                list
            } else null
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em fetchProfiles: ${e.message}")
            null
        }
    }

    // -------------------------------------------------------------
    // 3. BUSINESSES (CADASTRO DE NEGÓCIOS)
    // -------------------------------------------------------------

    suspend fun upsertBusiness(business: BusinessEntity): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val url = "$supabaseUrl/rest/v1/businesses"
            val json = JSONObject().apply {
                put("user_id", business.userId)
                put("name", business.name)
                put("category", business.category)
                put("neighborhood", business.neighborhood)
                put("city", business.city)
                put("description", business.description)
                put("services", business.services)
                put("instagram", business.instagram)
                put("is_imobiliario", business.isImobiliario)
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.post(json.toString().toRequestBody("application/json".toMediaType())).build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em upsertBusiness: ${e.message}")
            false
        }
    }

    suspend fun fetchBusinesses(): List<BusinessEntity>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val url = "$supabaseUrl/rest/v1/businesses?select=*"
            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: "[]"
                val jsonArr = JSONArray(respStr)
                val list = mutableListOf<BusinessEntity>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    list.add(
                        BusinessEntity(
                            id = obj.optLong("id", (i + 1).toLong()),
                            userId = obj.optLong("user_id", 0),
                            name = obj.optString("name", ""),
                            category = obj.optString("category", ""),
                            neighborhood = obj.optString("neighborhood", "Centro"),
                            city = obj.optString("city", "Cascavel"),
                            description = obj.optString("description", ""),
                            services = obj.optString("services", ""),
                            instagram = obj.optString("instagram", ""),
                            isImobiliario = obj.optBoolean("is_imobiliario", false)
                        )
                    )
                }
                list
            } else null
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em fetchBusinesses: ${e.message}")
            null
        }
    }

    // -------------------------------------------------------------
    // 4. OPPORTUNITIES / RADAR NEXELLA
    // -------------------------------------------------------------

    suspend fun createOpportunity(opp: OpportunityEntity): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val url = "$supabaseUrl/rest/v1/opportunities"
            val json = JSONObject().apply {
                put("title", opp.title)
                put("description", opp.description)
                put("category", opp.category)
                put("city", opp.city)
                put("neighborhood", opp.neighborhood)
                put("author_id", opp.authorId)
                put("author_name", opp.authorName)
                put("author_business", opp.authorBusiness)
                put("type", opp.type)
                put("is_imobiliario", opp.isImobiliario)
                put("status", opp.status)
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.post(json.toString().toRequestBody("application/json".toMediaType())).build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em createOpportunity: ${e.message}")
            false
        }
    }

    suspend fun fetchOpportunities(): List<OpportunityEntity>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val url = "$supabaseUrl/rest/v1/opportunities?select=*&order=created_at.desc"
            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: "[]"
                val jsonArr = JSONArray(respStr)
                val list = mutableListOf<OpportunityEntity>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    list.add(
                        OpportunityEntity(
                            id = obj.optLong("id", (i + 1).toLong()),
                            title = obj.optString("title", ""),
                            description = obj.optString("description", ""),
                            category = obj.optString("category", ""),
                            city = obj.optString("city", "Cascavel"),
                            neighborhood = obj.optString("neighborhood", "Centro"),
                            authorId = obj.optLong("author_id", 0),
                            authorName = obj.optString("author_name", "Empreendedora"),
                            authorBusiness = obj.optString("author_business", ""),
                            authorPhoto = obj.optString("author_photo", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400"),
                            type = obj.optString("type", "Procuro profissional"),
                            isImobiliario = obj.optBoolean("is_imobiliario", false),
                            status = obj.optString("status", "Aprovada")
                        )
                    )
                }
                list
            } else null
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em fetchOpportunities: ${e.message}")
            null
        }
    }

    // -------------------------------------------------------------
    // 5. CONNECTIONS (MINHAS CONEXÕES)
    // -------------------------------------------------------------

    suspend fun createConnection(conn: ConnectionEntity): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext false
        try {
            val url = "$supabaseUrl/rest/v1/connections"
            val json = JSONObject().apply {
                put("requester_id", conn.requesterId)
                put("requester_name", conn.requesterName)
                put("recipient_id", conn.recipientId)
                put("recipient_name", conn.recipientName)
                put("recipient_business", conn.recipientBusiness)
                put("recipient_category", conn.recipientCategory)
                put("recipient_whatsapp", conn.recipientWhatsapp)
                put("recipient_neighborhood", conn.recipientNeighborhood)
                put("origin", conn.origin)
                put("notes", conn.notes)
            }

            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.post(json.toString().toRequestBody("application/json".toMediaType())).build()
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em createConnection: ${e.message}")
            false
        }
    }

    suspend fun fetchConnections(): List<ConnectionEntity>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val url = "$supabaseUrl/rest/v1/connections?select=*&order=created_at.desc"
            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: "[]"
                val jsonArr = JSONArray(respStr)
                val list = mutableListOf<ConnectionEntity>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    list.add(
                        ConnectionEntity(
                            id = obj.optLong("id", (i + 1).toLong()),
                            requesterId = obj.optLong("requester_id", 0),
                            requesterName = obj.optString("requester_name", ""),
                            recipientId = obj.optLong("recipient_id", 0),
                            recipientName = obj.optString("recipient_name", ""),
                            recipientBusiness = obj.optString("recipient_business", ""),
                            recipientPhoto = obj.optString("recipient_photo", "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400"),
                            recipientCategory = obj.optString("recipient_category", ""),
                            recipientWhatsapp = obj.optString("recipient_whatsapp", ""),
                            recipientNeighborhood = obj.optString("recipient_neighborhood", "Centro"),
                            date = obj.optString("date", "Recente"),
                            origin = obj.optString("origin", "Perfil"),
                            notes = obj.optString("notes", "")
                        )
                    )
                }
                list
            } else null
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em fetchConnections: ${e.message}")
            null
        }
    }

    // -------------------------------------------------------------
    // 6. EVENTS / ENCONTROS
    // -------------------------------------------------------------

    suspend fun fetchEvents(): List<MeetingEntity>? = withContext(Dispatchers.IO) {
        if (!isConfigured()) return@withContext null
        try {
            val url = "$supabaseUrl/rest/v1/events?select=*"
            val requestBuilder = Request.Builder().url(url)
            buildHeaders().forEach { (k, v) -> requestBuilder.addHeader(k, v) }
            val request = requestBuilder.get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: "[]"
                val jsonArr = JSONArray(respStr)
                val list = mutableListOf<MeetingEntity>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    list.add(
                        MeetingEntity(
                            id = obj.optLong("id", (i + 1).toLong()),
                            name = obj.optString("name", "Café Nexella"),
                            date = obj.optString("date", ""),
                            time = obj.optString("time", "08:30"),
                            location = obj.optString("location", ""),
                            neighborhood = obj.optString("neighborhood", "Centro"),
                            city = obj.optString("city", "Cascavel"),
                            description = obj.optString("description", ""),
                            totalSpots = obj.optInt("total_spots", 15),
                            occupiedSpots = obj.optInt("occupied_spots", 0),
                            isCorretoraFocus = obj.optBoolean("is_corretora_focus", false)
                        )
                    )
                }
                list
            } else null
        } catch (e: Exception) {
            Log.e("SupabaseService", "Erro em fetchEvents: ${e.message}")
            null
        }
    }
}
