package com.example.currencylist.domain

sealed interface Currency {
    data class Crypto(val id: String, val name: String, val symbol: String): Currency
    data class Fiat(val id: String, val name: String, val symbol: String, val code: String): Currency

}