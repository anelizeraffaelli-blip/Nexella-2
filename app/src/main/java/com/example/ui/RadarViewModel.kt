package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NexellaDatabase
import com.example.data.local.entity.ConnectionEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.NexellaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RadarUiState(
    val selectedCity: String = "Cascavel",
    val selectedNeighborhood: String = "Todos Os Bairros",
    val selectedCategory: String = "Todas As Categorias",
    val searchQuery: String = "",
    val isImobiliarioOnly: Boolean = false,
    val prioritizeRealConnections: Boolean = true,
    val isLoading: Boolean = false
)

class RadarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NexellaRepository

    init {
        val dao = NexellaDatabase.getDatabase(application).nexellaDao()
        repository = NexellaRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    private val _uiState = MutableStateFlow(RadarUiState())
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    // Filter values
    val availableCities = listOf("Cascavel", "Toledo", "Foz do Iguaçu", "Marechal Cândido Rondon")
    val availableNeighborhoods = listOf(
        "Todos Os Bairros",
        "Centro",
        "Neva",
        "Parque Verde",
        "Cancelli",
        "Coqueiral",
        "FAG",
        "Tropical",
        "Maria Luiza",
        "Alto Alegre"
    )
    val availableCategories = listOf(
        "Todas As Categorias",
        "Corretoras de Imóveis",
        "Fotografia Imobiliária",
        "Arquitetura & Design",
        "Branding & Social Media",
        "Contabilidade & Finanças",
        "Direito Imobiliário & Família",
        "Saúde & Bem-Estar"
    )

    // Filtered Opportunities Flow respecting City, Neighborhood, and Real Connection Prioritization
    val filteredOpportunities: StateFlow<List<OpportunityEntity>> = combine(
        repository.allOpportunities,
        repository.allUsers,
        repository.allConnections,
        _uiState
    ) { opportunities, users, connections, state ->
        val userMap = users.associateBy { it.id }
        val connectedAuthorIds = connections.map { setOf(it.requesterId, it.recipientId) }.flatten().toSet()

        opportunities.filter { opp ->
            // Filter by City
            val matchesCity = state.selectedCity.isBlank() || opp.city.equals(state.selectedCity, ignoreCase = true)

            // Filter by Neighborhood
            val matchesNeighborhood = state.selectedNeighborhood == "Todos Os Bairros" ||
                    opp.neighborhood.equals(state.selectedNeighborhood, ignoreCase = true)

            // Filter by Category
            val matchesCategory = state.selectedCategory == "Todas As Categorias" ||
                    opp.category.equals(state.selectedCategory, ignoreCase = true)

            // Filter by Search Query
            val query = state.searchQuery.trim()
            val matchesQuery = query.isBlank() ||
                    opp.title.contains(query, ignoreCase = true) ||
                    opp.description.contains(query, ignoreCase = true) ||
                    opp.category.contains(query, ignoreCase = true) ||
                    opp.authorName.contains(query, ignoreCase = true) ||
                    opp.authorBusiness.contains(query, ignoreCase = true)

            // Filter by Imobiliario Radar flag
            val matchesImobiliario = !state.isImobiliarioOnly || opp.isImobiliario

            matchesCity && matchesNeighborhood && matchesCategory && matchesQuery && matchesImobiliario
        }.sortedWith { opp1, opp2 ->
            if (state.prioritizeRealConnections) {
                // Prioritization logic:
                // 1. Authors who have active real connections in the community get highest priority
                // 2. Authors who are founding members or approved real users in Cascavel
                // 3. Recency of creation date
                val author1 = userMap[opp1.authorId]
                val author2 = userMap[opp2.authorId]

                val isAuthor1Connected = connectedAuthorIds.contains(opp1.authorId)
                val isAuthor2Connected = connectedAuthorIds.contains(opp2.authorId)

                val score1 = (if (isAuthor1Connected) 10 else 0) +
                        (if (author1?.isFoundingMember == true) 5 else 0) +
                        (if (author1?.status == "Aprovado") 2 else 0)

                val score2 = (if (isAuthor2Connected) 10 else 0) +
                        (if (author2?.isFoundingMember == true) 5 else 0) +
                        (if (author2?.status == "Aprovado") 2 else 0)

                if (score1 != score2) {
                    score2.compareTo(score1) // Higher score first
                } else {
                    opp2.createdAt.compareTo(opp1.createdAt) // Newer first
                }
            } else {
                opp2.createdAt.compareTo(opp1.createdAt)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Actions to update state
    fun setCity(city: String) {
        _uiState.value = _uiState.value.copy(selectedCity = city)
    }

    fun setNeighborhood(neighborhood: String) {
        _uiState.value = _uiState.value.copy(selectedNeighborhood = neighborhood)
    }

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleImobiliarioOnly(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isImobiliarioOnly = enabled)
    }

    fun togglePrioritizeRealConnections(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(prioritizeRealConnections = enabled)
    }

    fun createOpportunity(
        title: String,
        description: String,
        category: String,
        city: String = "Cascavel",
        neighborhood: String,
        authorUser: UserEntity,
        type: String = "Procuro profissional",
        isImobiliario: Boolean = false
    ) {
        viewModelScope.launch {
            val opp = OpportunityEntity(
                title = title,
                description = description,
                category = category,
                city = city,
                neighborhood = neighborhood,
                authorId = authorUser.id,
                authorName = authorUser.name,
                authorBusiness = authorUser.businessName,
                authorPhoto = authorUser.photoUrl,
                type = type,
                isImobiliario = isImobiliario,
                status = "Aprovada"
            )
            repository.insertOpportunity(opp)
        }
    }

    suspend fun getOpportunityById(id: Long): OpportunityEntity? {
        return repository.allOpportunities.first().find { it.id == id }
    }
}
