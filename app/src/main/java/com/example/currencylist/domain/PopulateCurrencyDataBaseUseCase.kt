package com.example.currencylist.domain

interface PopulateCurrencyDataBaseUseCase {
    suspend fun invoke(): Result<Unit, PopulateDBError>

    sealed interface PopulateDBError: Error {
        data object InvalidDataFormat: PopulateDBError
        data object SourceNotFound: PopulateDBError
        data object SQLiteConstraintException: PopulateDBError
        data class Unknown(val errorMessage: String): PopulateDBError
    }
}