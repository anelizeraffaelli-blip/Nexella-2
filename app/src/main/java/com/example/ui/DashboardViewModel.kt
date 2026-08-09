package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NexellaDatabase
import com.example.data.repository.NexellaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardMetricsUiState(
    val totalConnections: Int = 0,
    val opportunitiesResolved: Int = 0,
    val totalOpportunities: Int = 0,
    val totalUsers: Int = 0,
    val totalMeetings: Int = 0,
    val corretorasCount: Int = 0,
    val resolutionRatePercentage: Int = 0,
    val isLoading: Boolean = false
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NexellaRepository

    init {
        val dao = NexellaDatabase.getDatabase(application).nexellaDao()
        repository = NexellaRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    val metricsState: StateFlow<DashboardMetricsUiState> = combine(
        repository.totalConnectionsCount,
        repository.successfulDealsCount,
        repository.totalOpportunitiesCount,
        repository.totalUsersCount,
        repository.totalMeetingsCount,
        repository.totalCorretorasCount
    ) { flows ->
        val connections = flows[0]
        val successfulDeals = flows[1]
        val opportunities = flows[2]
        val users = flows[3]
        val meetings = flows[4]
        val corretoras = flows[5]

        val rate = if (connections > 0) {
            ((successfulDeals.toDouble() / connections.toDouble()) * 100).toInt()
        } else {
            0
        }
        DashboardMetricsUiState(
            totalConnections = connections,
            opportunitiesResolved = successfulDeals,
            totalOpportunities = opportunities,
            totalUsers = users,
            totalMeetings = meetings,
            corretorasCount = corretoras,
            resolutionRatePercentage = rate,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardMetricsUiState(isLoading = true)
    )
}
