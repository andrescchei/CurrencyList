package com.example.currencylist.domain.usecase

import com.example.currencylist.domain.Error
import com.example.currencylist.domain.Result

interface InsertCurrencyDataBaseUseCase {
    suspend fun populateCurrencyDataBase(): Result<Unit, PopulateDBError>

    sealed interface PopulateDBError: Error {
        data object InvalidDataFormat: PopulateDBError
        data object SourceNotFound: PopulateDBError
        data class Unknown(val errorMessage: String): PopulateDBError
    }
}