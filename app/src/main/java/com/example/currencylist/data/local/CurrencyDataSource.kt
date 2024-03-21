package com.example.currencylist.data.local

import com.example.currencylist.domain.Currency
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun getCurrencyList(): Flow<List<Currency>>
}