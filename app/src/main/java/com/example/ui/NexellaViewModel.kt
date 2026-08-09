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

    init {
        val dao = NexellaDatabase.getDatabase(application).nexellaDao()
        repository = NexellaRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            loadCurrentLoggedInUser()
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
        }
    }

    fun updateConnectionImpact(connectionId: Long, resultedInOpportunity: Boolean) {
        viewModelScope.launch {
            val conn = repository.allConnections.first().find { it.id == connectionId }
            if (conn != null) {
                repository.updateConnection(conn.copy(generatedOpportunity = resultedInOpportunity))
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
        }
    }

    fun joinMeeting(meetingId: Long) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            repository.joinMeeting(meetingId, user)
        }
    }

    fun sendEllaPrompt(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insertEllaMessage(EllaMessageEntity(sender = "USER", text = text))
            _isEllaLoading.value = true

            val allUsers = repository.allUsers.first()
            val allOpps = repository.allOpportunities.first()

            val ellaReply = ellaService.queryElla(
                userPrompt = text,
                usersInDb = allUsers,
                opportunitiesInDb = allOpps
            )

            repository.insertEllaMessage(EllaMessageEntity(sender = "ELLA", text = ellaReply))
            _isEllaLoading.value = false
        }
    }

    fun sendEllaIntent(userIntent: UserIntent) {
        if (userIntent.rawPrompt.isBlank()) return
        viewModelScope.launch {
            repository.insertEllaMessage(EllaMessageEntity(sender = "USER", text = userIntent.rawPrompt))
            _isEllaLoading.value = true

            val allUsers = repository.allUsers.first()
            val allOpps = repository.allOpportunities.first()

            val ellaResponse = ellaService.sendUserIntent(
                userIntent = userIntent,
                usersInDb = allUsers,
                opportunitiesInDb = allOpps
            )

            repository.insertEllaMessage(EllaMessageEntity(sender = "ELLA", text = ellaResponse.replyText))
            _isEllaLoading.value = false
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

            // Save to ProfileEntity in Room database
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

            // Save to BusinessEntity in Room database
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
        }
    }

    fun loginUser(emailOrName: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val users = repository.allAdminUsers.first()
            val found = users.find {
                it.email.equals(emailOrName, ignoreCase = true) ||
                it.name.contains(emailOrName, ignoreCase = true) ||
                it.businessName.contains(emailOrName, ignoreCase = true)
            }
            if (found != null) {
                _currentUser.value = found
                onResult(true, "Bem-vinda de volta, ${found.name}!")
            } else {
                onResult(false, "Usuária não encontrada. Cadastre seu perfil e faça parte da Nexella!")
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
