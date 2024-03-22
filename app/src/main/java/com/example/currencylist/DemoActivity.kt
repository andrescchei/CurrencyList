package com.example.currencylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.ui.theme.CurrencyListTheme
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : ComponentActivity() {
    val viewModel: DemoViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CurrencyListTheme {
                NavHost(navController = navController, startDestination = "demo") {
                    composable("demo") { Demo(navController, viewModel) }
                    composable(
                        "currencyList"
                    ) {
                        CurrencyList(navController, viewModel.uiState.value.selectedCurrencyTypes)
                    }
                    // Add more destinations similarly.
                }
            }
        }
    }
}

@Composable
fun Demo(navController: NavController, viewModel: DemoViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.onEvent(DemoEvent.onClearDb)
        }) {
            Text(text = "Clear DB")
        }
        Button(onClick = { viewModel.onEvent(DemoEvent.onInsertDB) }) {
            Text(text = "Insert DB")
        }
        CurrencyType.entries.forEach {
            Button(onClick = {
                viewModel.onEvent(DemoEvent.onClickNavigation(persistentSetOf(it)))
                navController.navigate("currencyList")
            }) {
                Text(text = it.name)
            }
        }
        Button(onClick = {
            viewModel.onEvent(DemoEvent.onClickNavigation(CurrencyType.entries.toImmutableSet()))
            navController.navigate("currencyList")
        }) {
            Text(text = "All")
        }
    }
}

@Composable
fun CurrencyList(navController: NavHostController, types: Set<CurrencyType>) {
    Text(types.toString())
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyListTheme {
    }
}
