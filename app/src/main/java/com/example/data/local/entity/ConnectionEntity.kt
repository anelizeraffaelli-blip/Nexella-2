package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connections")
data class ConnectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val requesterId: Long,
    val requesterName: String,
    val recipientId: Long,
    val recipientName: String,
    val recipientBusiness: String,
    val recipientPhoto: String,
    val recipientCategory: String,
    val recipientWhatsapp: String = "",
    val recipientNeighborhood: String = "Centro",
    val date: String,
    val origin: String = "Perfil", // "Perfil", "Radar Nexella", "Café Nexella", "Assistente ELLA"
    val opportunityId: Long? = null,
    val meetingId: Long? = null,
    val generatedOpportunity: Boolean? = null, // null = pending check, true = SIM, false = NÃO
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
