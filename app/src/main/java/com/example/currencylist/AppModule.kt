package com.example.currencylist

import androidx.room.Room
import com.example.currencylist.data.local.CurrencyDao
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.data.local.CurrencyDataSourceImpl
import com.example.currencylist.data.local.CurrencyDatabase
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCase
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCaseImpl
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCase
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    single<CurrencyDatabase> {
        CurrencyDatabase.getDatabase(androidApplication().applicationContext)
    }
    single<CurrencyDao> {
        val database = get<CurrencyDatabase>()
        database.currencyDao()
    }
    single<CurrencyDataSource> {
        CurrencyDataSourceImpl(get())
    }

    factory<InsertCurrencyDataBaseUseCase>{InsertCurrencyDataBaseUseCaseImpl(applicationContext = androidContext().applicationContext, get())}
    factory<DeleteCurrencyDataBaseUseCase>{DeleteCurrencyDataBaseUseCaseImpl(get())}


    viewModel {
        DemoViewModel(get(), get())
    }
}