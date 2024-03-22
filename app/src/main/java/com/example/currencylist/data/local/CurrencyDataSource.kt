package com.example.currencylist.data.local

import com.example.currencylist.data.entities.CurrencyDto
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun getCurrencyListStream(): Flow<List<Currency>>
    suspend fun getCurrencyList(currencyTypes: Set<CurrencyType>): List<Currency>
    suspend fun searchCurrencyList(searchText: String, currencyTypes: Set<CurrencyType>): List<Currency>
    suspend fun insertCurrencyDataBase(currency: Currency): Unit
    suspend fun deleteCurrencyDataBase(): Unit
}