package com.example.currencylist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencylist.presentation.components.Demo
import com.example.currencylist.presentation.components.FragmentInCompose
import com.example.currencylist.presentation.viewModel.DemoViewModel
import com.example.currencylist.ui.theme.CurrencyListTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {
    private val viewModel: DemoViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val uiState = viewModel.uiState.collectAsStateWithLifecycle()
            if(uiState.value.toastMessage != null) {
                Toast.makeText(this, uiState.value.toastMessage, Toast.LENGTH_SHORT).show()
                viewModel.onToasted()
            }
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
