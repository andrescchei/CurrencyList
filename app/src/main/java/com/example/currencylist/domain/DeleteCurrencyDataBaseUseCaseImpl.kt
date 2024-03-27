package com.example.currencylist.domain

import com.example.currencylist.data.local.CurrencyDataSource
import kotlin.coroutines.cancellation.CancellationException

class DeleteCurrencyDataBaseUseCaseImpl(private val currencyDS: CurrencyDataSource):
    DeleteCurrencyDataBaseUseCase {
    override suspend fun deleteCurrencyDataBase(): Result<Unit, DeleteCurrencyDataBaseUseCase.DeleteDBError> = try {
        currencyDS.deleteCurrencyDataBase()
        Result.Success(Unit)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.Error(
            DeleteCurrencyDataBaseUseCase.DeleteDBError.Unknown(
                e.localizedMessage ?: "No error message"
            )
        )
    }
}