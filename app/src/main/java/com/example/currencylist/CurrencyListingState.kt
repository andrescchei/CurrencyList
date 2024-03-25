package com.example.currencylist

import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

data class CurrencyListingState (
    val searchText: String = "",
    val selectedTypes: ImmutableSet<CurrencyType> = persistentSetOf(),
    val currencies: ImmutableList<Currency> = persistentListOf(),
    val onBackPress: (() -> Unit)? = null
)

sealed interface CurrencyListingEvent {
    data class OnInitial(
        val types: Set<CurrencyType>,
        val onBackPress: () -> Unit
    ): CurrencyListingEvent

    data class OnSearch(
        val searchText: String
    ): CurrencyListingEvent

    data object OnSearchCancel: CurrencyListingEvent
    data object OnClearText: CurrencyListingEvent
}