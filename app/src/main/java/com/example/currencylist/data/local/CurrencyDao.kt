package com.example.currencylist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.currencylist.data.entities.CurrencyDto
import kotlinx.coroutines.flow.Flow


@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencydto")
    fun getAllStream(): Flow<List<CurrencyDto>>

    @Query("SELECT * FROM currencydto")
    suspend fun getAllList(): List<CurrencyDto>
    @Query("SELECT * FROM currencydto WHERE type IN(:currencyTypes)")
    suspend fun getListByType(currencyTypes: List<String>): List<CurrencyDto>

//    @Query("SELECT * FROM currencyDto WHERE name LIKE :name AND " +
//            "symbol LIKE :symbol LIMIT 1")
//    fun findByName(name: String, symbol: String): CurrencyDto
    @Query("SELECT * FROM currencydto WHERE " +
            "(name LIKE '%' || ' ' || :keyword || '%' OR " +
            "name LIKE :keyword || '%'  OR " +
            "symbol LIKE :keyword || '%' OR " +
            "code LIKE :keyword || '%' ) AND " +
            "type IN(:currencyTypes)")
    suspend fun searchByKeywordAndType(keyword: String, currencyTypes: List<String>): List<CurrencyDto>

    @Insert
    suspend fun insertCurrency(vararg currencies: CurrencyDto)
//
    @Delete
    suspend fun delete(currencies: CurrencyDto)

    @Query("DELETE FROM currencydto")
    suspend fun deleteAll()
}