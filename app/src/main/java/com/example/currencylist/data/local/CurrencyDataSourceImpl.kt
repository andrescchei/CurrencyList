package com.example.currencylist.data.local


import com.example.currencylist.data.entities.CurrencyDto
import com.example.currencylist.domain.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyDataSourceImpl(private val currencyDao: CurrencyDao): CurrencyDataSource {
    override fun getCurrencyList(): Flow<List<Currency>> {
        return currencyDao.getAll().map { currencies ->
            return@map currencies.map {
                it.toCurrency()
            }
        }
    }
}

fun CurrencyDto.toCurrency(): Currency {
    return code?.let { code ->
        Currency.Fiat(
            id = id,
            name = name,
            symbol = symbol,
            code = code
        )
    } ?: Currency.Crypto(
        id = id,
        name = name,
        symbol = symbol,
    )
}