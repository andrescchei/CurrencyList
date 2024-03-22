//package com.example.currencylist.data.local
//
//import android.content.Context
//import android.util.Log
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.example.currencylist.R
//import com.example.currencylist.data.entities.CurrencyDto
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import org.json.JSONArray
//
//class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {
//
//    override fun onCreate(db: SupportSQLiteDatabase) {
//        super.onCreate(db)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            prePopulateCurrencies(context)
//        }
//    }
//
//    suspend fun prePopulateCurrencies(context: Context) {
//        try {
//            val currencyDao = CurrencyDatabase.getDatabase(context).currencyDao()
//
//            val currencyList: JSONArray =
//                context.resources.openRawResource(R.raw.fiat).bufferedReader().use {
//                    JSONArray(it.readText())
//                }
//
//            currencyList.takeIf { it.length() > 0 }?.let { list ->
//                for (index in 0 until list.length()) {
//                    val currencyObj = list.getJSONObject(index)
//                    val id = currencyObj.optString("id")
//                    val name = currencyObj.optString("name")
//                    val symbol = currencyObj.optString("symbol")
//                    val code = currencyObj.optString("code")
//                    if (!(id.isBlank() || name.isBlank() || symbol.isBlank())) {
//                        currencyDao.insertCurrency(
//                            CurrencyDto(
//                                id,
//                                name,
//                                symbol,
//                                code.ifBlank { null }
//                            )
//                        )
//                    }
//                }
//                Log.e("Currency List", "successfully pre-populated currencies into database")
//            }
//        } catch (exception: Exception) {
//            Log.e(
//                "Currency List",
//                exception.localizedMessage ?: "failed to pre-populate currencies into database"
//            )
//        }
//    }
//}