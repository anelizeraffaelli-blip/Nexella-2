package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class BusinessEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val category: String,
    val neighborhood: String = "Centro",
    val city: String = "Cascavel",
    val description: String = "",
    val services: String = "",
    val website: String = "",
    val instagram: String = "",
    val isImobiliario: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
