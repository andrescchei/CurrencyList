package com.example.currencylist.data.local

import com.example.currencylist.domain.Currency

interface CurrencyDataSource {
    suspend fun getCurrencyList(): List<Currency>
}