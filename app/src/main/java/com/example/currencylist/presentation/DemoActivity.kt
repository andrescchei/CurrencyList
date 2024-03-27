package com.example.currencylist.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencylist.databinding.FragmentContainerViewBinding
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.presentation.components.Demo
import com.example.currencylist.presentation.components.FragmentInCompose
import com.example.currencylist.presentation.viewModel.DemoEvent
import com.example.currencylist.presentation.viewModel.DemoViewModel
import com.example.currencylist.ui.theme.CurrencyListTheme
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {
    val viewModel: DemoViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            CurrencyListTheme {
                NavHost(navController = navController, startDestination = NavDestinations.DEMO.route) {
                    composable(NavDestinations.DEMO.route) {
                        Demo(viewModel::onEvent) {
                            navController.navigate(it.route)
                        }
                    }
                    composable(NavDestinations.CURRENCY_LIST.route)
                        {
                            FragmentInCompose(navController::navigateUp, uiState.value.selectedCurrencyTypes)
                        }
                    // Add more destinations similarly.
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyListTheme {
    }
}
