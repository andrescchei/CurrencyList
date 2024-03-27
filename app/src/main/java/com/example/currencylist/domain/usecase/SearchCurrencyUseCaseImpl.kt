package com.example.currencylist.domain.usecase

import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.domain.Result
import com.example.currencylist.domain.toCurrency
import kotlin.coroutines.cancellation.CancellationException

class SearchCurrencyUseCaseImpl(val currencyDS: CurrencyDataSource): SearchCurrencyUseCase {
    override suspend fun invoke(
        types: Set<CurrencyType>,
        searchText: String
    ): Result<List<Currency>, SearchCurrencyUseCase.SearchCurrencyError> {
        val typesKey = types.map { it.stringKey }.toSet()
        return try {
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
}