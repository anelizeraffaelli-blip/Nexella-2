package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long = 0,
    val name: String = "",
    val city: String = "Cascavel",
    val neighborhood: String = "Centro",
    val businessName: String = "",
    val category: String = "",
    val email: String = "",
    val bio: String = "",
    val instagram: String = "",
    val linkedin: String = "",
    val whatsapp: String = "",
    val isPublic: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
