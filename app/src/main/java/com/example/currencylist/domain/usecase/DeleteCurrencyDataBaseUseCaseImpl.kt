package com.example.currencylist.domain.usecase

import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.domain.Result
import kotlin.coroutines.cancellation.CancellationException

class DeleteCurrencyDataBaseUseCaseImpl(val currencyDS: CurrencyDataSource):
    DeleteCurrencyDataBaseUseCase {
    override suspend fun deleteCurrencyDataBase(): Result<Unit, DeleteCurrencyDataBaseUseCase.DeleteDBError> {
        return try {
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
}