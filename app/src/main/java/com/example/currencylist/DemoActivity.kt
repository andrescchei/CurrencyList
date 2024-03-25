package com.example.currencylist

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.currencylist.databinding.FragmentContainerViewBinding
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.ui.CurrencyListingFragment
import com.example.currencylist.ui.CurrencyListingFragment.Companion.CURRENCY_TYPE_KEY
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
            CurrencyListTheme {
                NavHost(navController = navController, startDestination = "demo") {
                    composable("demo") { Demo(navController, viewModel::onEvent) }
                    composable("currencyList")
                        {
                            FragmentInCompose(navController, viewModel.uiState.value.selectedCurrencyTypes)
                        }
                    // Add more destinations similarly.
                }
            }
        }
    }
}

@Composable
fun Demo(navController: NavController, onEvent: (DemoEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onEvent(DemoEvent.onClearDb)
        }) {
            Text(text = "Clear DB")
        }
        Button(onClick = { onEvent(DemoEvent.onInsertDB) }) {
            Text(text = "Insert DB")
        }
        CurrencyType.entries.forEach {
            Button(onClick = {
                onEvent(DemoEvent.onClickNavigation(persistentSetOf(it)))
                navController.navigate("currencyList")
            }) {
                Text(text = it.name)
            }
        }
        Button(onClick = {
            onEvent(DemoEvent.onClickNavigation(CurrencyType.entries.toImmutableSet()))
            navController.navigate("currencyList")
        }) {
            Text(text = "All")
        }
    }
}

@Composable
fun FragmentInCompose(navController: NavHostController, types: Set<CurrencyType>) {
    AndroidViewBinding(
        FragmentContainerViewBinding::inflate,
        onReset = {

        },
        onRelease = {
            Log.i("AndroidViewBinding", "release FragmentContainerViewBinding")
        },
        update = {
            Log.i("AndroidBindingView", "fragment onUpdate")
            fragmentContainerView.getFragment<CurrencyListingFragment>().onInitiate(
                types
            ) {
                navController.navigateUp()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyListTheme {
    }
}
