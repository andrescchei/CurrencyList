package com.example.currencylist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencylist.CurrencyListingEvent
import com.example.currencylist.CurrencyListingState
import com.example.currencylist.CurrencyListingViewModel
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.ui.theme.CurrencyListTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyListingFragment: Fragment() {
    val viewModel: CurrencyListingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                CurrencyListTheme {
                    CurrencyListing(uiState.value, viewModel::onEvent)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CurrencyListing(uiState: CurrencyListingState, onEvent: (CurrencyListingEvent) -> Unit) {
        SearchBar(
            uiState.searchText,
            onQueryChange = {
                onEvent(CurrencyListingEvent.OnSearch(it))
            },
            onSearch = {
                onEvent(CurrencyListingEvent.OnSearch(it))
            },
            active = true,
            onActiveChange = {
                Log.i("Currecny Listing", "onActiveChange")
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = uiState.selectedTypes.joinToString(separator = ", "))
            },
            leadingIcon = {
                IconButton(onClick = {
                    uiState.onBackPress?.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            trailingIcon = {
                val isSearching = uiState.searchText.isNotEmpty()
                IconButton(onClick = {
                     if(isSearching) {
                         onEvent(CurrencyListingEvent.OnSearchCancel)
                         onEvent(CurrencyListingEvent.OnSearch(""))
                     }
                }) {
                    Icon(
                        imageVector = if(isSearching) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = "Cancel"
                    )
                }
            }
        ) {
            if (uiState.currencies.isNotEmpty()) {
                LazyColumn {
                    items(items = uiState.currencies) { currency ->
                        Row {
                            val name = when(currency) {
                                is Currency.Fiat -> currency.name
                                is Currency.Crypto -> currency.name
                            }
                            Text(text = name)
                        }
                    }
                }    
            } else {
                Text(text = "No Result")
            }
        }
    }

    fun onInitiate(types: Set<CurrencyType>, onBackPress: () -> Unit) {
        viewModel.onEvent(CurrencyListingEvent.OnInitial(
            types, onBackPress
        ))
    }

    companion object {
        const val CURRENCY_TYPE_KEY = "CURRENCY_TYPE_KEY"
    }
}