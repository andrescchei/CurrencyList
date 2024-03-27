package com.example.currencylist.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.presentation.NavDestinations
import com.example.currencylist.presentation.viewModel.DemoEvent
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

@Composable
fun Demo(onEvent: (DemoEvent) -> Unit, onNavigate: (NavDestinations) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onEvent(DemoEvent.OnClearDb)
        }) {
            Text(text = "Clear DB")
        }
        Button(onClick = { onEvent(DemoEvent.OnInsertDB) }) {
            Text(text = "Insert DB")
        }
        CurrencyType.entries.forEach {
            Button(onClick = {
                onEvent(DemoEvent.OnClickNavigation(persistentSetOf(it)))
                onNavigate(NavDestinations.CURRENCY_LIST)
            }) {
                Text(text = it.name)
            }
        }
        Button(onClick = {
            onEvent(DemoEvent.OnClickNavigation(CurrencyType.entries.toImmutableSet()))
            onNavigate(NavDestinations.CURRENCY_LIST)
        }) {
            Text(text = "All")
        }
    }
}