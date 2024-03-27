package com.example.currencylist.domain.usecase

import com.example.currencylist.domain.Error
import com.example.currencylist.domain.Result

interface DeleteCurrencyDataBaseUseCase {
    suspend fun deleteCurrencyDataBase(): Result<Unit, DeleteDBError>

    sealed interface DeleteDBError: Error {
        data class Unknown(val errorMessage: String): DeleteDBError
    }
}