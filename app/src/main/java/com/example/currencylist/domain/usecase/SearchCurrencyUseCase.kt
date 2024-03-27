package com.example.currencylist.domain.usecase

import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.domain.Error
import com.example.currencylist.domain.Result

interface SearchCurrencyUseCase {
    suspend fun invoke(types: Set<CurrencyType>, searchText: String): Result<List<Currency>, SearchCurrencyError>

    sealed interface SearchCurrencyError: Error {
        data class Unknown(val errorMessage: String): SearchCurrencyError
    }
}

