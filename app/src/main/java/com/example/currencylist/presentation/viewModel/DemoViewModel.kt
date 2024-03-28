package com.example.currencylist.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.domain.DeleteCurrencyDataBaseUseCase
import com.example.currencylist.domain.PopulateCurrencyDataBaseUseCase
import com.example.currencylist.domain.Result
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoViewModel(
    private val populateCurrencyDataBaseUseCase: PopulateCurrencyDataBaseUseCase,
    private val deleteCurrencyDataBaseUseCase: DeleteCurrencyDataBaseUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DemoState())
    val uiState: StateFlow<DemoState> = _uiState

    private var job: Job? = null
    fun onEvent(event: DemoEvent) {
        when(event) {
            is DemoEvent.OnClickNavigation -> selectCurrencyTypes(event.selectedCurrencyTypes)
            is DemoEvent.OnClearDb -> cancelAndReassignJob(deleteDB())
            is DemoEvent.OnInsertDB -> cancelAndReassignJob(insertDB())
        }
    }
    private fun selectCurrencyTypes(currencyTypes: ImmutableSet<CurrencyType>) {
        _uiState.update {
            it.copy(selectedCurrencyTypes = currencyTypes)
        }
    }

    private fun insertDB(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            onFinishIO(when(val result = populateCurrencyDataBaseUseCase.invoke()) {
                is Result.Success -> "Insert DB Success"
                is Result.Error ->
                    when(val error = result.error) {
                        PopulateCurrencyDataBaseUseCase.PopulateDBError.InvalidDataFormat,
                        PopulateCurrencyDataBaseUseCase.PopulateDBError.SourceNotFound,
                        PopulateCurrencyDataBaseUseCase.PopulateDBError.SQLiteConstraintException -> error.toString()
                        is PopulateCurrencyDataBaseUseCase.PopulateDBError.Unknown -> error.errorMessage
                    }
            })
        }
    }

    private fun deleteDB(): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            onFinishIO(when(val result = deleteCurrencyDataBaseUseCase.invoke()) {
                is Result.Success -> "Delete DB Success"
                is Result.Error ->
                    when(val error = result.error) {
                        is DeleteCurrencyDataBaseUseCase.DeleteDBError.Unknown -> error.errorMessage
                    }
            })
        }
    }

    private fun onFinishIO(message: String){
        _uiState.update { it.copy(toastMessage = message) }
    }

    //expose給view call的function
    fun onToasted(){
        _uiState.update { it.copy(toastMessage = null) }
    }

    @Synchronized
    private fun cancelAndReassignJob(newJob: Job) {
        job?.cancel()
        job = newJob
    }
}
