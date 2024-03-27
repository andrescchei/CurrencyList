package com.example.currencylist.domain

interface SearchCurrencyUseCase {
    suspend fun invoke(types: Set<CurrencyType>, searchText: String): Result<List<Currency>, SearchCurrencyError>

    sealed interface SearchCurrencyError: Error {
        data class Unknown(val errorMessage: String): SearchCurrencyError
    }
}

