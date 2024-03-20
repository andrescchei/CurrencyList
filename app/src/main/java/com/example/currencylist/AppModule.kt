package com.example.currencylist

import androidx.room.Room
import com.example.currencylist.data.local.CurrencyDao
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.data.local.CurrencyDataSourceImpl
import com.example.currencylist.data.local.CurrencyDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CurrencyDatabase> {
        val db = Room.databaseBuilder(
            androidApplication().applicationContext,
            CurrencyDatabase::class.java, "currency-db"
        ).build()
        db
    }
    single<CurrencyDao> {
        val database = get<CurrencyDatabase>()
        database.currencyDao()
    }
    single<CurrencyDataSource> {
        CurrencyDataSourceImpl(get())
    }

    viewModel {
//        val db = Room.databaseBuilder(
//            androidApplication().applicationContext,
//            CurrencyDatabase::class.java, "currency-db"
//        ).build()
//        val dao = db.currencyDao()
//        val ds = CurrencyDataSourceImpl(dao)
        DemoViewModel(get())
    }
}