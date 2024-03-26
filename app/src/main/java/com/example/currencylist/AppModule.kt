package com.example.currencylist

import androidx.room.Room
import com.example.currencylist.data.local.CurrencyDao
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.data.local.CurrencyDataSourceImpl
import com.example.currencylist.data.local.CurrencyDatabase
import com.example.currencylist.data.local.LocalJSONDataSource
import com.example.currencylist.data.local.LocalJSONDataSourceImpl
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCase
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCaseImpl
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCase
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCaseImpl
import com.example.currencylist.domain.SearchCurrencyUseCase
import com.example.currencylist.domain.SearchCurrencyUseCaseImpl
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
    single<LocalJSONDataSource>{
        LocalJSONDataSourceImpl(androidApplication().applicationContext)
    }

    factory<InsertCurrencyDataBaseUseCase>{InsertCurrencyDataBaseUseCaseImpl(get(), get())}
    factory<DeleteCurrencyDataBaseUseCase>{DeleteCurrencyDataBaseUseCaseImpl(get())}
    factory<SearchCurrencyUseCase>{ SearchCurrencyUseCaseImpl(get()) }


    viewModel {
        DemoViewModel(get(), get())
    }
    viewModel {
        CurrencyListingViewModel(get())
    }
}