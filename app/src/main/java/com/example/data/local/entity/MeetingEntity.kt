package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: String,
    val time: String,
    val location: String,
    val neighborhood: String,
    val city: String = "Cascavel",
    val description: String,
    val totalSpots: Int = 15,
    val occupiedSpots: Int = 0,
    val isCorretoraFocus: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
