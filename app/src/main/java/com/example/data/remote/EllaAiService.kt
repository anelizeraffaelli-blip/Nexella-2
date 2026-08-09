package com.example.data.remote

import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity

class EllaAiService {

    private val proxyService = EllaBackendProxyService()

    suspend fun queryElla(
        userPrompt: String,
        usersInDb: List<UserEntity>,
        opportunitiesInDb: List<OpportunityEntity>
    ): String {
        val intent = proxyService.classifyUserIntent(userPrompt)
        val response = proxyService.sendUserIntentToElla(
            userIntent = intent,
            usersInDb = usersInDb,
            opportunitiesInDb = opportunitiesInDb
        )
        return response.replyText
    }

    suspend fun sendUserIntent(
        userIntent: UserIntent,
        usersInDb: List<UserEntity>,
        opportunitiesInDb: List<OpportunityEntity>
    ): EllaIntentResponse {
        return proxyService.sendUserIntentToElla(
            userIntent = userIntent,
            usersInDb = usersInDb,
            opportunitiesInDb = opportunitiesInDb
        )
    }
}
