package com.example.currencylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.domain.Result
import com.example.currencylist.domain.SearchCurrencyUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CurrencyListingViewModel(val searchCurrencyUseCase: SearchCurrencyUseCase): ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyListingState())
    val uiState: StateFlow<CurrencyListingState> = _uiState
    private var job: Job? = null
    fun onEvent(event: CurrencyListingEvent) {
        when(event) {
            is CurrencyListingEvent.OnInitial -> {
                if(_uiState.value.selectedTypes.isEmpty() && _uiState.value.onBackPress == null) {
                    _uiState.update {
                        it.copy(
                            selectedTypes = event.types.toImmutableSet(),
                            onBackPress = event.onBackPress
                        )
                    }
                    onEvent(CurrencyListingEvent.OnSearch(_uiState.value.searchText))
                }
            }
            is CurrencyListingEvent.OnSearch -> {
                _uiState.update {
                    it.copy(searchText = event.searchText)
                }
                viewModelScope.launch(Dispatchers.IO){
                    cancelAndReassignJob(search())
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

    private fun search(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(300L)
            val result = searchCurrencyUseCase.invoke(
                _uiState.value.selectedTypes,
                _uiState.value.searchText
            )
            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(currencies = result.response.toImmutableList()) }
                }

                is Result.Error -> {
                    when (val error = result.error) {
                        is SearchCurrencyUseCase.SearchCurrencyError.Unknown -> Log.e(
                            "",
                            error.errorMessage
                        )
                    }
                }
            }
            Log.i("CurrencyListingViewModel", "OnSearch $result")
        }
    }

    @Synchronized
    private fun cancelAndReassignJob(newJob: Job) {
        job?.cancel()
        job = newJob
    }
}