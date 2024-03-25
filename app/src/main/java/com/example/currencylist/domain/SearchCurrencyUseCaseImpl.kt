package com.example.currencylist.domain

import com.example.currencylist.data.local.CurrencyDataSource

class SearchCurrencyUseCaseImpl(val currencyDS: CurrencyDataSource): SearchCurrencyUseCase {
    override suspend fun invoke(
        types: Set<CurrencyType>,
        searchText: String
    ): Result<List<Currency>, SearchCurrencyUseCase.SearchCurrencyError> {
        return try {
            val result = if (searchText.isNotBlank()) currencyDS.searchCurrencyList(searchText, types) else currencyDS.getCurrencyList(types)
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(SearchCurrencyUseCase.SearchCurrencyError.Unknown(e.localizedMessage ?: "No message"))
        }
    }
}