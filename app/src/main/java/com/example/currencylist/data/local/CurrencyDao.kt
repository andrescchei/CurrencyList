package com.example.currencylist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.currencylist.data.entities.CurrencyDto


@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencydto")
    suspend fun getAll(): List<CurrencyDto>

//    @Query("SELECT * FROM currencydto WHERE id IN (:currencyIds)")
//    suspend fun loadAllByIds(currencyIds: IntArray): List<CurrencyDto>

//    @Query("SELECT * FROM currencyDto WHERE name LIKE :name AND " +
//            "symbol LIKE :symbol LIMIT 1")
//    fun findByName(name: String, symbol: String): CurrencyDto

//    @Insert
//    suspend fun insertAll(vararg currencies: CurrencyDto)
//
//    @Delete
//    suspend fun delete(currencies: CurrencyDto)

}