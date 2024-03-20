package com.example.currencylist

import com.example.currencylist.domain.Currency

data class CurrencyListingState(
    val currencies: List<Currency> = emptyList(),
    val searchQuery: String = "",
    val selectingCurrency: CurrencyType? = null
)

enum class CurrencyType {
    FIAT, CRYPTO
}