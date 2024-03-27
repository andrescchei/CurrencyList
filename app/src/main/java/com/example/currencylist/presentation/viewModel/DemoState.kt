package com.example.currencylist.presentation.viewModel

import com.example.currencylist.domain.CurrencyType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

data class DemoState(
    val selectedCurrencyTypes: ImmutableSet<CurrencyType> = persistentSetOf()
)

sealed interface DemoEvent {
    data object OnClearDb: DemoEvent
    data object OnInsertDB: DemoEvent
    data class OnClickNavigation(val selectedCurrencyTypes: ImmutableSet<CurrencyType>): DemoEvent
}