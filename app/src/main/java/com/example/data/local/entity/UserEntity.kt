package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val photoUrl: String,
    val businessName: String,
    val city: String = "Cascavel",
    val neighborhood: String = "Centro",
    val category: String,
    val services: String,
    val description: String,
    val instagram: String = "",
    val whatsapp: String = "",
    val allowWhatsapp: Boolean = true,
    val website: String = "",
    val email: String = "",
    val password: String = "",
    val status: String = "Aprovado", // "Aguardando Aprovação", "Aprovado", "Suspenso"
    val creci: String? = null,
    val specialities: String = "",
    val procuro: String = "",
    val ofereco: String = "",
    val interests: String = "",
    val isFoundingMember: Boolean = true, // Selo Membro Fundadora
    val isCorretora: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
