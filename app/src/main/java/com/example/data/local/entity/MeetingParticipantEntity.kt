package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_participants")
data class MeetingParticipantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val meetingId: Long,
    val userId: Long,
    val userName: String,
    val userBusiness: String,
    val userPhoto: String,
    val registeredAt: Long = System.currentTimeMillis()
)
