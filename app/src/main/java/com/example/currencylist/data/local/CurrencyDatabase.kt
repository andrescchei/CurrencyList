package com.example.currencylist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencylist.data.entities.CurrencyDto

@Database(entities = [CurrencyDto::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}