package com.example.currencylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyDto(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val type: String,
    val code: String?
)