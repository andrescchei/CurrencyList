package com.example.currencylist

import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

data class CurrencyListingState(
    val currencies: ImmutableList<Currency> = persistentListOf(),
    val selectedCurrencyTypes: ImmutableSet<CurrencyType> = persistentSetOf()
)

sealed interface DemoEvent {
    data object onClearDb: DemoEvent
    data object onInsertDB: DemoEvent
    data class onClickNavigation(val selectedCurrencyTypes: ImmutableSet<CurrencyType>): DemoEvent
}