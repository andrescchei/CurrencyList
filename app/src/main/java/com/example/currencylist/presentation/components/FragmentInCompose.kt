package com.example.currencylist.presentation.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.example.currencylist.databinding.FragmentContainerViewBinding
import com.example.currencylist.domain.CurrencyType
import com.example.currencylist.presentation.CurrencyListingFragment

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