package com.example.currencylist.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.ui.theme.CurrencyListTheme

class CurrencyListingFragment: Fragment() {
    var onBackPress: (() -> Unit)? = null
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listOfTypes = arguments?.getStringArray(CURRENCY_TYPE_KEY)/*?.map { CurrencyType.valueOf(it) }*/ ?: arrayOf()
        Log.i("AndroidBindingView", "fragment onCreateView")
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CurrencyListTheme {
                    Scaffold(topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    "Centered Top App Bar",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    onBackPress?.invoke()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    }) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding)) {
                            Text(text = listOfTypes.toList().joinToString(","), modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }

    }

    override fun onAttach(context: Context) {
        Log.i("AndroidBindingView", "fragment onattach")
        super.onAttach(context)
    }
    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
    }

    companion object {
        const val CURRENCY_TYPE_KEY = "CURRENCY_TYPE_KEY"
    }
}