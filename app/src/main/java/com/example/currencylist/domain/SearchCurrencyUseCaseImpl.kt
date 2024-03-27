package com.example.currencylist.domain

import com.example.currencylist.data.local.CurrencyDataSource
import kotlin.coroutines.cancellation.CancellationException

class SearchCurrencyUseCaseImpl(private val currencyDS: CurrencyDataSource): SearchCurrencyUseCase {
    override suspend fun invoke(
        types: Set<CurrencyType>,
        searchText: String
    ): Result<List<Currency>, SearchCurrencyUseCase.SearchCurrencyError> = try {
        val typesKey = types.map { it.stringKey }.toSet()
        val result = if (searchText.isNotBlank()) currencyDS.searchCurrencyList(searchText, typesKey) else currencyDS.getCurrencyList(typesKey)
        Result.Success(result.map { it.toCurrency() })
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.Error(
            SearchCurrencyUseCase.SearchCurrencyError.Unknown(
                e.localizedMessage ?: "No message"
            )
        )
    }
}