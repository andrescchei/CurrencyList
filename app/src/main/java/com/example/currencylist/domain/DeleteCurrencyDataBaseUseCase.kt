package com.example.currencylist.domain

interface DeleteCurrencyDataBaseUseCase {
    suspend fun deleteCurrencyDataBase(): Result<Unit, DeleteDBError>

    sealed interface DeleteDBError: Error {
        data class Unknown(val errorMessage: String): DeleteDBError
    }
}