package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NexellaDatabase
import com.example.data.local.entity.BusinessEntity
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.EllaMessageEntity
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import com.example.data.remote.EllaAiService
import com.example.data.remote.UserIntent
import com.example.data.repository.NexellaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AdminMetrics(
    val totalUsers: Int = 0,
    val totalCorretoras: Int = 0,
    val totalOpportunities: Int = 0,
    val totalConnections: Int = 0,
    val totalSuccessfulDeals: Int = 0,
    val totalMeetings: Int = 0
)

class NexellaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NexellaRepository
    private val ellaService = EllaAiService()

    private val _isSupabaseSyncing = MutableStateFlow(false)
    val isSupabaseSyncing: StateFlow<Boolean> = _isSupabaseSyncing.asStateFlow()

    private val _supabaseStatusText = MutableStateFlow("Pendente verificação")
    val supabaseStatusText: StateFlow<String> = _supabaseStatusText.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _isLoadingData = MutableStateFlow(true)
    val isLoadingData: StateFlow<Boolean> = _isLoadingData.asStateFlow()

    private val _isActionLoading = MutableStateFlow(false)
    val isActionLoading: StateFlow<Boolean> = _isActionLoading.asStateFlow()

    val isSupabaseConfigured: Boolean
        get() = repository.supabaseService.isConfigured()

    fun showMessage(message: String) {
        _userMessage.value = message
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    init {
        val dao = NexellaDatabase.getDatabase(application).nexellaDao()
        repository = NexellaRepository(dao)

        viewModelScope.launch {
            _isLoadingData.value = true
            try {
                repository.seedInitialDataIfNeeded()
                loadCurrentLoggedInUser()
                syncWithSupabase()
            } catch (e: Exception) {
                _userMessage.value = "Aviso: Carregando banco local. (${e.localizedMessage ?: "Erro de inicialização"})"
            } finally {
                _isLoadingData.value = false
            }
        }
    }

    fun syncWithSupabase() {
        viewModelScope.launch {
            if (repository.supabaseService.isConfigured()) {
                _isSupabaseSyncing.value = true
                _supabaseStatusText.value = "Sincronizando com Supabase em tempo real..."
                try {
                    repository.syncRemoteData()
                    _supabaseStatusText.value = "Supabase Conectado (Dados reais ativas)"
                } catch (e: Exception) {
                    _supabaseStatusText.value = "Modo Fallback Local (Room DB)"
                    _userMessage.value = "Modo offline ativado: usando dados locais de Cascavel."
                } finally {
                    _isSupabaseSyncing.value = false
                }
            } else {
                _supabaseStatusText.value = "Modo Desenvolvimento (Fallback Room DB)"
            }
        }
    }

    // Navigation & View State
    private val _currentTab = MutableStateFlow(0) // 0=Home, 1=Comunidade, 2=Conexões, 3=Encontros, 4=Perfil/Admin
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun selectTab(index: Int) {
        _currentTab.value = index
    }

    // Active Logged In User
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private suspend fun loadCurrentLoggedInUser() {
        val users = repository.allUsers.first()
        if (users.isNotEmpty()) {
            _currentUser.value = users.first()
        }
    }

    fun switchCurrentUser(user: UserEntity) {
        _currentUser.value = user
    }

    // Search & Filter State for Community & Radar
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedNeighborhood = MutableStateFlow("Todos Os Bairros")
    val selectedNeighborhood: StateFlow<String> = _selectedNeighborhood.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todas As Categorias")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isImobiliarioOnly = MutableStateFlow(false)
    val isImobiliarioOnly: StateFlow<Boolean> = _isImobiliarioOnly.asStateFlow()

    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun updateNeighborhoodFilter(neighborhood: String) { _selectedNeighborhood.value = neighborhood }
    fun updateCategoryFilter(category: String) { _selectedCategory.value = category }
    fun toggleImobiliarioOnly(enabled: Boolean) { _isImobiliarioOnly.value = enabled }

    // Users Flow (Filtered)
    val usersList: StateFlow<List<UserEntity>> = combine(
        repository.allUsers,
        _searchQuery,
        _selectedNeighborhood,
        _selectedCategory,
        _isImobiliarioOnly
    ) { users, query, neighborhood, category, imobiliarioOnly ->
        users.filter { user ->
            val matchesQuery = query.isBlank() ||
                    user.name.contains(query, ignoreCase = true) ||
                    user.businessName.contains(query, ignoreCase = true) ||
                    user.category.contains(query, ignoreCase = true) ||
                    user.specialities.contains(query, ignoreCase = true) ||
                    user.procuro.contains(query, ignoreCase = true) ||
                    user.ofereco.contains(query, ignoreCase = true)

            val matchesNeighborhood = neighborhood == "Todos Os Bairros" || user.neighborhood.equals(neighborhood, ignoreCase = true)
            val matchesCategory = category == "Todas As Categorias" || user.category.equals(category, ignoreCase = true)
            val matchesImobiliario = !imobiliarioOnly || user.isCorretora || user.category.contains("Imob", ignoreCase = true) || user.category.contains("Fotografia", ignoreCase = true) || user.category.contains("Arquit", ignoreCase = true)

            matchesQuery && matchesNeighborhood && matchesCategory && matchesImobiliario
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminUsersList: StateFlow<List<UserEntity>> = repository.allAdminUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Opportunities Flow
    val opportunitiesList: StateFlow<List<OpportunityEntity>> = combine(
        repository.allOpportunities,
        _searchQuery,
        _selectedNeighborhood,
        _isImobiliarioOnly
    ) { opps, query, neighborhood, imobiliarioOnly ->
        opps.filter { opp ->
            val matchesQuery = query.isBlank() ||
                    opp.title.contains(query, ignoreCase = true) ||
                    opp.description.contains(query, ignoreCase = true) ||
                    opp.category.contains(query, ignoreCase = true)

            val matchesNeighborhood = neighborhood == "Todos Os Bairros" || opp.neighborhood.equals(neighborhood, ignoreCase = true)
            val matchesImobiliario = !imobiliarioOnly || opp.isImobiliario

            matchesQuery && matchesNeighborhood && matchesImobiliario
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Connections
    val connectionsList: StateFlow<List<ConnectionEntity>> = repository.allConnections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Meetings
    val meetingsList: StateFlow<List<MeetingEntity>> = repository.allMeetings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ELLA Chat Messages
    val ellaMessages: StateFlow<List<EllaMessageEntity>> = repository.allEllaMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isEllaLoading = MutableStateFlow(false)
    val isEllaLoading: StateFlow<Boolean> = _isEllaLoading.asStateFlow()

    // Admin Metrics
    val adminMetrics: StateFlow<AdminMetrics> = combine(
        repository.totalUsersCount,
        repository.totalCorretorasCount,
        repository.totalOpportunitiesCount,
        repository.totalConnectionsCount,
        repository.successfulDealsCount,
        repository.totalMeetingsCount
    ) { flows ->
        AdminMetrics(
            totalUsers = flows[0],
            totalCorretoras = flows[1],
            totalOpportunities = flows[2],
            totalConnections = flows[3],
            totalSuccessfulDeals = flows[4],
            totalMeetings = flows[5]
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminMetrics())

    // Success Connection Dialog State
    private val _latestCreatedConnection = MutableStateFlow<ConnectionEntity?>(null)
    val latestCreatedConnection: StateFlow<ConnectionEntity?> = _latestCreatedConnection.asStateFlow()

    fun dismissConnectionSuccessDialog() {
        _latestCreatedConnection.value = null
    }

    // --- ACTIONS ---

    fun createConnection(recipient: UserEntity, origin: String, notes: String = "") {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val user = _currentUser.value ?: return@launch
                val conn = ConnectionEntity(
                    requesterId = user.id,
                    requesterName = user.name,
                    recipientId = recipient.id,
                    recipientName = recipient.name,
                    recipientBusiness = recipient.businessName,
                    recipientPhoto = recipient.photoUrl,
                    recipientCategory = recipient.category,
                    recipientWhatsapp = recipient.whatsapp,
                    recipientNeighborhood = recipient.neighborhood,
                    date = "Hoje",
                    origin = origin,
                    notes = notes
                )
                repository.insertConnection(conn)
                _latestCreatedConnection.value = conn
                _userMessage.value = "Conexão iniciada com ${recipient.name}!"
            } catch (e: Exception) {
                _userMessage.value = "Erro ao criar conexão: ${e.localizedMessage ?: "Tente novamente"}"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun updateConnectionImpact(connectionId: Long, resultedInOpportunity: Boolean) {
        viewModelScope.launch {
            try {
                val conn = repository.allConnections.first().find { it.id == connectionId }
                if (conn != null) {
                    repository.updateConnection(conn.copy(generatedOpportunity = resultedInOpportunity))
                    _userMessage.value = "Impacto da conexão atualizado com sucesso!"
                }
            } catch (e: Exception) {
                _userMessage.value = "Erro ao atualizar impacto: ${e.localizedMessage}"
            }
        }
    }

    fun createOpportunity(
        title: String,
        description: String,
        category: String,
        neighborhood: String,
        type: String,
        isImobiliario: Boolean
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val user = _currentUser.value ?: return@launch
                val opp = OpportunityEntity(
                    title = title,
                    description = description,
                    category = category,
                    city = "Cascavel",
                    neighborhood = neighborhood,
                    authorId = user.id,
                    authorName = user.name,
                    authorBusiness = user.businessName,
                    authorPhoto = user.photoUrl,
                    type = type,
                    isImobiliario = isImobiliario,
                    status = "Aprovada"
                )
                repository.insertOpportunity(opp)
                _userMessage.value = "Oportunidade publicada no Radar Nexella! 🚀"
            } catch (e: Exception) {
                _userMessage.value = "Erro ao publicar oportunidade: ${e.localizedMessage ?: "Verifique sua conexão."}"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun joinMeeting(meetingId: Long) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                val user = _currentUser.value ?: return@launch
                repository.joinMeeting(meetingId, user)
                _userMessage.value = "Presença confirmada no encontro com sucesso! 🤝"
            } catch (e: Exception) {
                _userMessage.value = "Erro ao confirmar presença: ${e.localizedMessage}"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun sendEllaPrompt(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertEllaMessage(EllaMessageEntity(sender = "USER", text = text))
            _isEllaLoading.value = true

            try {
                val allUsers = repository.allUsers.first()
                val allOpps = repository.allOpportunities.first()

                val ellaReply = ellaService.queryElla(
                    userPrompt = text,
                    usersInDb = allUsers,
                    opportunitiesInDb = allOpps
                )

                repository.insertEllaMessage(EllaMessageEntity(sender = "ELLA", text = ellaReply))
            } catch (e: Exception) {
                _userMessage.value = "Não foi possível obter resposta da Ella. Tente novamente."
                repository.insertEllaMessage(
                    EllaMessageEntity(
                        sender = "ELLA",
                        text = "Tive uma oscilação na conexão com a inteligência em nuvem. Por favor, tente novamente em instantes! 💜"
                    )
                )
            } finally {
                _isEllaLoading.value = false
            }
        }
    }

    fun clearEllaChat() {
        viewModelScope.launch {
            repository.clearEllaMessages()
            // Insert greeting message back
            repository.insertEllaMessage(
                EllaMessageEntity(
                    sender = "ELLA",
                    text = "Olá! Eu sou a Ella 💜 Mentora e Assistente AI da Nexella.\nEstou pronta para te dar conselhos de negócios, estratégias de precificação, parcerias locais e sugestões de conexões em Cascavel!"
                )
            )
            _userMessage.value = "Conversa com Ella reiniciada."
        }
    }

    fun sendEllaIntent(userIntent: UserIntent) {
        if (userIntent.rawPrompt.isBlank()) return
        viewModelScope.launch {
            repository.insertEllaMessage(EllaMessageEntity(sender = "USER", text = userIntent.rawPrompt))
            _isEllaLoading.value = true

            try {
                val allUsers = repository.allUsers.first()
                val allOpps = repository.allOpportunities.first()

                val ellaResponse = ellaService.sendUserIntent(
                    userIntent = userIntent,
                    usersInDb = allUsers,
                    opportunitiesInDb = allOpps
                )

                repository.insertEllaMessage(EllaMessageEntity(sender = "ELLA", text = ellaResponse.replyText))
            } catch (e: Exception) {
                _userMessage.value = "Erro no processamento da Ella. Tente novamente."
            } finally {
                _isEllaLoading.value = false
            }
        }
    }

    fun registerNewUser(
        name: String,
        businessName: String,
        category: String,
        neighborhood: String,
        services: String,
        description: String,
        instagram: String,
        whatsapp: String,
        procuro: String,
        ofereco: String,
        isCorretora: Boolean,
        creci: String?,
        email: String = "",
        password: String = ""
    ) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                if (email.isNotBlank() && password.isNotBlank() && repository.supabaseService.isConfigured()) {
                    val authRes = repository.supabaseService.signUp(email, password, name)
                    if (authRes.success) {
                        _supabaseStatusText.value = "Usuária registrada no Supabase Auth"
                    }
                }

                val newUser = UserEntity(
                    name = name,
                    photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
                    businessName = businessName,
                    city = "Cascavel",
                    neighborhood = neighborhood,
                    category = category,
                    services = services,
                    description = description,
                    instagram = instagram,
                    whatsapp = whatsapp,
                    email = email,
                    password = password,
                    allowWhatsapp = true,
                    status = "Aprovado",
                    creci = creci,
                    specialities = services,
                    procuro = procuro,
                    ofereco = ofereco,
                    isFoundingMember = true,
                    isCorretora = isCorretora
                )
                val newId = repository.insertUser(newUser)

                // Save to ProfileEntity in Room database and sync to Supabase
                val newProfile = ProfileEntity(
                    userId = newId,
                    name = name,
                    city = "Cascavel",
                    neighborhood = neighborhood,
                    businessName = businessName,
                    category = category,
                    email = email,
                    bio = description,
                    instagram = instagram,
                    whatsapp = whatsapp
                )
                repository.insertProfile(newProfile)

                // Save to BusinessEntity in Room database and sync to Supabase
                val newBusiness = BusinessEntity(
                    userId = newId,
                    name = businessName,
                    category = category,
                    neighborhood = neighborhood,
                    city = "Cascavel",
                    description = description,
                    services = services,
                    instagram = instagram,
                    isImobiliario = isCorretora
                )
                repository.insertBusiness(newBusiness)

                val activeUser = newUser.copy(id = newId)
                _currentUser.value = activeUser
                _userMessage.value = "Seja muito bem-vinda à Nexella, ${name}! 🎉"
            } catch (e: Exception) {
                _userMessage.value = "Erro ao cadastrar perfil: ${e.localizedMessage ?: "Verifique seus dados."}"
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun loginUser(emailOrName: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _isActionLoading.value = true
            try {
                if (emailOrName.contains("@") && repository.supabaseService.isConfigured()) {
                    val authRes = repository.supabaseService.signIn(emailOrName, "123456")
                    if (authRes.success) {
                        _supabaseStatusText.value = "Autenticada via Supabase Auth"
                    }
                }

                val users = repository.allAdminUsers.first()
                val found = users.find {
                    it.email.equals(emailOrName, ignoreCase = true) ||
                    it.name.contains(emailOrName, ignoreCase = true) ||
                    it.businessName.contains(emailOrName, ignoreCase = true)
                }
                if (found != null) {
                    _currentUser.value = found
                    val successMsg = "Bem-vinda de volta, ${found.name}!"
                    _userMessage.value = successMsg
                    onResult(true, successMsg)
                } else {
                    val errMsg = "Usuária não encontrada. Cadastre seu perfil e faça parte da Nexella!"
                    _userMessage.value = errMsg
                    onResult(false, errMsg)
                }
            } catch (e: Exception) {
                val errMsg = "Erro no login: ${e.localizedMessage ?: "Tente novamente"}"
                _userMessage.value = errMsg
                onResult(false, errMsg)
            } finally {
                _isActionLoading.value = false
            }
        }
    }

    fun adminApproveUser(userId: Long) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            if (user != null) {
                repository.updateUser(user.copy(status = "Aprovado"))
            }
        }
    }

    fun adminSuspendUser(userId: Long) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            if (user != null) {
                repository.updateUser(user.copy(status = "Suspenso"))
            }
        }
    }
}
