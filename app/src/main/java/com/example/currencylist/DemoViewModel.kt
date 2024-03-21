package com.example.currencylist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DemoViewModel(
    private val currencyDataSource: CurrencyDataSource
): ViewModel() {
    val uiState = mutableStateOf (CurrencyListingState())

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private fun getCurrencyList() {
        viewModelScope.launch {
            currencyDataSource.getCurrencyList().collect {
                uiState.value = uiState.value.copy(currencies = it.toImmutableList())
            }
        }
    }
    init {
        getCurrencyList()
    }

    fun onEvent(event: DemoEvent) {
        when(event) {
            is DemoEvent.onClickNavigation -> selectCurrencyTypes(event.selectedCurrencyTypes)
            is DemoEvent.onClearDb -> TODO()
            is DemoEvent.onInsertDB -> TODO()
        }
    }
    private fun selectCurrencyTypes(currencyTypes: ImmutableSet<CurrencyType>) {
        uiState.value = uiState.value.copy(selectedCurrencyTypes = currencyTypes)
    }
}

val testingData = listOf(
    Currency.Fiat("HKD", "HKD", "HKD", "HKD"),
    Currency.Fiat("GBP", "GBP", "GBP", "GBP"),
    Currency.Fiat("USD", "USD", "USD", "USD"),
    Currency.Crypto("BTC", "Bitcoin", "BTC"),
)