package com.example.currencylist.domain

import android.content.Context
import android.content.res.Resources
import com.example.currencylist.R
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.data.local.LocalJSONDataSource
import org.json.JSONArray
import org.json.JSONException

class InsertCurrencyDataBaseUseCaseImpl(val currencyDS: CurrencyDataSource, val localJSONDS: LocalJSONDataSource): InsertCurrencyDataBaseUseCase {
    override suspend fun populateCurrencyDataBase(): Result<Unit, InsertCurrencyDataBaseUseCase.PopulateDBError> {
        try {
            val fiatList: JSONArray = localJSONDS.getJSONArrayFromLocalJSONFile(R.raw.fiat)
            val cryptoList: JSONArray = localJSONDS.getJSONArrayFromLocalJSONFile(R.raw.crypto)

            fiatList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val currencyObj = list.getJSONObject(index)
                    val id = currencyObj.getString("id")
                    val name = currencyObj.getString("name")
                    val symbol = currencyObj.getString("symbol")
                    val code = currencyObj.getString("code")
                    currencyDS.insertCurrencyDataBase(
                        Currency.Fiat(id, name, symbol, code)
                    )
                }
            }

            cryptoList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    val currencyObj = list.getJSONObject(index)
                    val id = currencyObj.getString("id")
                    val name = currencyObj.getString("name")
                    val symbol = currencyObj.getString("symbol")
                    currencyDS.insertCurrencyDataBase(
                        Currency.Crypto(id, name, symbol)
                    )
                }
            }

        } catch (e: Exception) {
            return when(e) {
                is JSONException -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.InvalidDataFormat)
                is Resources.NotFoundException -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.SourceNotFound)
                else -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.Unknown(e.localizedMessage ?: "No error message"))
            }
        }
        return Result.Success(Unit)
    }
}