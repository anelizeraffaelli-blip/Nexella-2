package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "opportunities")
data class OpportunityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val city: String = "Cascavel",
    val neighborhood: String = "Centro",
    val authorId: Long,
    val authorName: String,
    val authorBusiness: String,
    val authorPhoto: String,
    val type: String = "Procuro profissional", // "Procuro profissional", "Procuro parceira", "Ofereço serviço", "Procuro fornecedor", "Quero indicar alguém", "Outra oportunidade"
    val isImobiliario: Boolean = false, // Radar Imobiliário filter
    val status: String = "Aprovada", // "Aprovada", "Pendente"
    val createdAt: Long = System.currentTimeMillis()
)
