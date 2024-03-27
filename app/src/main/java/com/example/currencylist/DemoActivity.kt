package com.example.currencylist

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencylist.databinding.FragmentContainerViewBinding
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.ui.CurrencyListingFragment
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

@Composable
fun FragmentInCompose(onNavigateUp: () -> Unit, types: Set<CurrencyType>) {
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
                onNavigateUp()
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
