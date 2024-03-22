package com.example.currencylist.domain

import com.example.currencylist.data.local.CurrencyDataSource

class DeleteCurrencyDataBaseUseCaseImpl(val currencyDS: CurrencyDataSource): DeleteCurrencyDataBaseUseCase {
    override suspend fun deleteCurrencyDataBase(): Result<Unit, DeleteCurrencyDataBaseUseCase.DeleteDBError> {
        return try {
            currencyDS.deleteCurrencyDataBase()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DeleteCurrencyDataBaseUseCase.DeleteDBError.Unknown(e.localizedMessage ?: "No error message"))
        }
    }
}