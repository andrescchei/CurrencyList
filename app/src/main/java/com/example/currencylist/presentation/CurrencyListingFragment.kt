package com.example.currencylist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencylist.presentation.viewModel.CurrencyListingEvent
import com.example.currencylist.presentation.viewModel.CurrencyListingState
import com.example.currencylist.presentation.viewModel.CurrencyListingViewModel
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.presentation.components.CurrencyListing
import com.example.currencylist.ui.theme.CurrencyListTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyListingFragment: Fragment() {
    private val viewModel: CurrencyListingViewModel by viewModel()

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

    fun onInitiate(types: Set<CurrencyType>, onBackPress: () -> Unit) {
        viewModel.onEvent(
            CurrencyListingEvent.OnInitial(
            types, onBackPress
        ))
    }

    companion object {
        const val CURRENCY_TYPE_KEY = "CURRENCY_TYPE_KEY"
    }
}