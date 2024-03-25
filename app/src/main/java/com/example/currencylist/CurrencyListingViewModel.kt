package com.example.currencylist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.domain.Result
import com.example.currencylist.domain.SearchCurrencyUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CurrencyListingViewModel(val searchCurrencyUseCase: SearchCurrencyUseCase): ViewModel() {
    val uiState = mutableStateOf(CurrencyListingState())
    var job: Job? = null
    fun onEvent(event: CurrencyListingEvent) {
        when(event) {
            is CurrencyListingEvent.OnInitial -> {
                if(uiState.value.selectedTypes.isEmpty() && uiState.value.onBackPress == null) {
                    uiState.value = uiState.value.copy(
                        selectedTypes = event.types.toImmutableSet(),
                        onBackPress = event.onBackPress
                    )
                    onEvent(CurrencyListingEvent.OnSearch(uiState.value.searchText))
                }
            }
            is CurrencyListingEvent.OnSearch -> {
                uiState.value = uiState.value.copy(searchText = event.searchText)

                viewModelScope.launch(Dispatchers.IO) {
                    job?.cancel()
                    job = null
                    job = launch(Dispatchers.IO) {
                        delay(300L)
                        val result = searchCurrencyUseCase.invoke(uiState.value.selectedTypes, uiState.value.searchText)
                        when(result) {
                            is Result.Success -> {
                                uiState.value = uiState.value.copy (currencies = result.response.toImmutableList())
                            }
                            is Result.Error -> {
                                when(val error = result.error) {
                                    is SearchCurrencyUseCase.SearchCurrencyError.Unknown -> Log.e("", error.errorMessage)
                                }
                            }
                        }
                        Log.i("CurrencyListingViewModel", "OnSearch $result")
                    }
                }
            }
            is CurrencyListingEvent.OnSearchCancel -> {
                Log.i("CurrencyListingViewModel", "OnSearchCancel")
                viewModelScope.launch(Dispatchers.IO) {
                    job?.cancel()
                    job = null
                }
            }
            is CurrencyListingEvent.OnClearText -> {
                onEvent(CurrencyListingEvent.OnSearch(""))
            }
        }
    }
}