package com.example.currencylist.domain

import android.content.Context
import android.content.res.Resources
import com.example.currencylist.R
import com.example.currencylist.data.local.CurrencyDataSource
import org.json.JSONArray
import org.json.JSONException

class InsertCurrencyDataBaseUseCaseImpl(val applicationContext: Context, val currencyDS: CurrencyDataSource): InsertCurrencyDataBaseUseCase {
    override suspend fun populateCurrencyDataBase(): Result<Unit, InsertCurrencyDataBaseUseCase.PopulateDBError> {
        try {
            //TODO should extract to LocalJSONDataSource
            val fiatList: JSONArray =
                applicationContext.resources.openRawResource(R.raw.fiat).bufferedReader().use {
                    JSONArray(it.readText())
                }
            //TODO should extract to LocalJSONDataSource
            val cryptoList: JSONArray =
                applicationContext.resources.openRawResource(R.raw.crypto).bufferedReader().use {
                    JSONArray(it.readText())
                }

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