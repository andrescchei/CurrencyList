package com.example.currencylist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.domain.Currency
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DemoViewModel(
    private val currencyDataSource: CurrencyDataSource
): ViewModel() {
    val uiState = mutableStateOf (CurrencyListingState())
    fun getCurrencyList() {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(currencies = testingData)
            delay(5000L)
            val list = currencyDataSource.getCurrencyList()
            uiState.value = uiState.value.copy(currencies = list)
        }
    }
    init {
        getCurrencyList()
    }
}

val testingData = listOf(
    Currency.Fiat("HKD", "HKD", "HKD", "HKD"),
    Currency.Fiat("GBP", "GBP", "GBP", "GBP"),
    Currency.Fiat("USD", "USD", "USD", "USD"),
    Currency.Crypto("BTC", "Bitcoin", "BTC"),
)