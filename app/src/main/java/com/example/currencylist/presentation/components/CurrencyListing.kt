package com.example.currencylist.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencylist.domain.Currency
import com.example.currencylist.presentation.viewModel.CurrencyListingEvent
import com.example.currencylist.presentation.viewModel.CurrencyListingState

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
            Log.i("Currency Listing", "onActiveChange")
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
                    Row(modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 8.dp, vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        val name: String
                        val code: String
                        when(currency) {
                            is Currency.Fiat -> {
                                name = currency.name
                                code = currency.code
                            }
                            is Currency.Crypto -> {
                                name = currency.name
                                code = currency.symbol
                            }
                        }

                        Box(modifier = Modifier
                            .size(35.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.Gray)) {
                            Text(text = name.first().toString(), Modifier.align(Alignment.Center), fontSize = 20.sp, color = Color.White)
                        }

                        Text(
                            text = name,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp)
                        )

                        Text(
                            text = code,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )

                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Right"
                        )
                    }
                    Divider(color = Color.LightGray, modifier = Modifier.padding(50.dp, 0.dp, 0.dp, 0.dp))
                }
            }
        } else {
            Text(text = "No Result")
        }
    }
}