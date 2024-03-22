package com.example.currencylist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.data.local.CurrencyDataSource
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCase
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCase
import com.example.currencylist.domain.Result
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DemoViewModel(
    private val insertCurrencyDataBaseUseCase: InsertCurrencyDataBaseUseCase,
    private val deleteCurrencyDataBaseUseCase: DeleteCurrencyDataBaseUseCase
): ViewModel() {
    val uiState = mutableStateOf (CurrencyListingState())

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private fun getCurrencyList() {
        viewModelScope.launch {
//            currencyDataSource.getCurrencyListStream().collect {
//                uiState.value = uiState.value.copy(currencies = it.toImmutableList())
//            }
        }
    }
    init {
        getCurrencyList()
    }

    fun onEvent(event: DemoEvent) {
        when(event) {
            is DemoEvent.onClickNavigation -> selectCurrencyTypes(event.selectedCurrencyTypes)
            is DemoEvent.onClearDb -> deleteDB()
            is DemoEvent.onInsertDB -> insertDB()
        }
    }
    private fun selectCurrencyTypes(currencyTypes: ImmutableSet<CurrencyType>) {
        uiState.value = uiState.value.copy(selectedCurrencyTypes = currencyTypes)
    }

    private fun insertDB() {
        viewModelScope.launch {
            when(val result = insertCurrencyDataBaseUseCase.populateCurrencyDataBase()) {
                is Result.Success -> Log.i("DemoViewModel", "insertDB success")
                is Result.Error ->
                    when(val error = result.error) {
                        InsertCurrencyDataBaseUseCase.PopulateDBError.InvalidDataFormat,
                        InsertCurrencyDataBaseUseCase.PopulateDBError.SourceNotFound-> Log.e("DemoModel", error.toString())
                        is InsertCurrencyDataBaseUseCase.PopulateDBError.Unknown -> Log.e("DemoModel", error.errorMessage)
                    }
            }
        }
    }

    private fun deleteDB() {
        viewModelScope.launch {
            when(val result = deleteCurrencyDataBaseUseCase.deleteCurrencyDataBase()) {
                is Result.Success -> Log.i("DemoViewModel", "deleteDB success")
                is Result.Error ->
                    when(val error = result.error) {
                        is DeleteCurrencyDataBaseUseCase.DeleteDBError.Unknown -> Log.e("DemoModel", error.errorMessage)
                    }
            }
        }
    }

    private fun clearDB() {
        viewModelScope.launch {

        }
    }
}

val testingData = listOf(
    Currency.Fiat("HKD", "HKD", "HKD", "HKD"),
    Currency.Fiat("GBP", "GBP", "GBP", "GBP"),
    Currency.Fiat("USD", "USD", "USD", "USD"),
    Currency.Crypto("BTC", "Bitcoin", "BTC"),
)