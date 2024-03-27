package com.example.currencylist.data.local

import com.example.currencylist.data.entities.CurrencyDto
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.coroutines.flow.Flow

interface CurrencyDataSource {
    fun getCurrencyListStream(): Flow<List<CurrencyDto>>
    suspend fun getCurrencyList(currencyTypes: Set<String>): List<CurrencyDto>
    suspend fun searchCurrencyList(searchText: String, currencyTypes: Set<String>): List<CurrencyDto>
    suspend fun insertCurrencyDataBase(currency: CurrencyDto): Unit
    suspend fun deleteCurrencyDataBase(): Unit
}