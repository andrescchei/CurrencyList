package com.example.currencylist.domain

interface DeleteCurrencyDataBaseUseCase {
    suspend fun invoke(): Result<Unit, DeleteDBError>

    sealed interface DeleteDBError: Error {
        data class Unknown(val errorMessage: String): DeleteDBError
    }
}