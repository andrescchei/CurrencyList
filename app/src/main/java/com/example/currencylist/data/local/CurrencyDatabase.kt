package com.example.currencylist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencylist.data.entities.CurrencyDto

@Database(entities = [CurrencyDto::class], version = 1)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {

        private var DB_INSTANCE: CurrencyDatabase? = null
        fun getDatabase(context: Context): CurrencyDatabase {
            synchronized(this) {
                var instance = DB_INSTANCE
                if (instance == null) {
                    DB_INSTANCE = Room.databaseBuilder(
                        context,
                        CurrencyDatabase::class.java,
                        "currency-db"
                    ).build()
                    instance = DB_INSTANCE
                }

                return instance!!
            }
        }

    }
}