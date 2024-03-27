package com.example.currencylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCase
import com.example.currencylist.domain.InsertCurrencyDataBaseUseCase
import com.example.currencylist.domain.Result
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoViewModel(
    private val insertCurrencyDataBaseUseCase: InsertCurrencyDataBaseUseCase,
    private val deleteCurrencyDataBaseUseCase: DeleteCurrencyDataBaseUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DemoState())
    val uiState: StateFlow<DemoState> = _uiState

    fun onEvent(event: DemoEvent) {
        when(event) {
            is DemoEvent.OnClickNavigation -> selectCurrencyTypes(event.selectedCurrencyTypes)
            is DemoEvent.OnClearDb -> deleteDB()
            is DemoEvent.OnInsertDB -> insertDB()
        }
    }
    private fun selectCurrencyTypes(currencyTypes: ImmutableSet<CurrencyType>) {
        _uiState.update {
            it.copy(selectedCurrencyTypes = currencyTypes)
        }
    }

    private fun insertDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = insertCurrencyDataBaseUseCase.populateCurrencyDataBase()) {
                is Result.Success -> Log.i("DemoViewModel", "insertDB success")
                is Result.Error ->
                    when(val error = result.error) {
                        InsertCurrencyDataBaseUseCase.PopulateDBError.InvalidDataFormat,
                        InsertCurrencyDataBaseUseCase.PopulateDBError.SourceNotFound-> Log.e("DemoModel", error.toString())
                        is InsertCurrencyDataBaseUseCase.PopulateDBError.Unknown -> Log.e("DemoModel", error.errorMessage)
                    }
            }
            Log.i("Coroutine" , "insertDB completed")
        }
    }

    private fun deleteDB() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = deleteCurrencyDataBaseUseCase.deleteCurrencyDataBase()) {
                is Result.Success -> Log.i("DemoViewModel", "deleteDB success")
                is Result.Error ->
                    when(val error = result.error) {
                        is DeleteCurrencyDataBaseUseCase.DeleteDBError.Unknown -> Log.e("DemoModel", error.errorMessage)
                    }
            }
        }
    }
}

//val testingData = listOf(
//    Currency.Fiat("HKD", "HKD", "HKD", "HKD"),
//    Currency.Fiat("GBP", "GBP", "GBP", "GBP"),
//    Currency.Fiat("USD", "USD", "USD", "USD"),
//    Currency.Crypto("BTC", "Bitcoin", "BTC"),
//)