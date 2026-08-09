package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ella_messages")
data class EllaMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "ELLA"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
