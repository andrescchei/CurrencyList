package com.example.currencylist.domain

import android.content.res.Resources
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.currencylist.R
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.data.local.LocalJSONDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import kotlin.coroutines.CoroutineContext

class InsertCurrencyDataBaseUseCaseImpl(private val currencyDS: CurrencyDataSource, private val localJSONDS: LocalJSONDataSource):
    InsertCurrencyDataBaseUseCase {
    override suspend fun populateCurrencyDataBase(): Result<Unit, InsertCurrencyDataBaseUseCase.PopulateDBError> = try {
        withContext(Dispatchers.IO) {
                val job1 = launch {
                    val fiatList: JSONArray = localJSONDS.getJSONArrayFromLocalJSONFile(R.raw.fiat)
                    fiatList.takeIf { it.length() > 0 }?.let { list ->
                        for (index in 0 until list.length()) {
                            delay(100L)
                            val currencyObj = list.getJSONObject(index)
                            val id = currencyObj.getString("id")
                            val name = currencyObj.getString("name")
                            val symbol = currencyObj.getString("symbol")
                            val code = currencyObj.getString("code")
                            currencyDS.insertCurrencyDataBase(
                                Currency.Fiat(id, name, symbol, code).toCurrencyDto()
                            )
                            Log.i("Coroutine" , "job1 running $name")
                        }
                        Log.i("Coroutine" , "job1 completed")
                    }
                }
                val job2 = launch {
                    val cryptoList: JSONArray = localJSONDS.getJSONArrayFromLocalJSONFile(R.raw.crypto)

                    cryptoList.takeIf { it.length() > 0 }?.let { list ->
                        for (index in 0 until list.length()) {
                            delay(100L)
                            val currencyObj = list.getJSONObject(index)
                            val id = currencyObj.getString("id")
                            val name = currencyObj.getString("name")
                            val symbol = currencyObj.getString("symbol")
                            currencyDS.insertCurrencyDataBase(
                                Currency.Crypto(id, name, symbol).toCurrencyDto()
                            )
                            Log.i("Coroutine" , "job2 running $name")
                        }
                        Log.i("Coroutine" , "job2 completed")
                    }
                }
                Result.Success(listOf(job1, job2).joinAll())
        }
    } catch (e: Exception) {
        when(e) {
            is CancellationException -> throw e
            is SQLiteConstraintException -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.SQLiteConstraintException)
            is JSONException -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.InvalidDataFormat)
            is Resources.NotFoundException -> Result.Error(InsertCurrencyDataBaseUseCase.PopulateDBError.SourceNotFound)
            else -> Result.Error(
                InsertCurrencyDataBaseUseCase.PopulateDBError.Unknown(
                    e.localizedMessage ?: "No error message"
                )
            )
        }
    }
}