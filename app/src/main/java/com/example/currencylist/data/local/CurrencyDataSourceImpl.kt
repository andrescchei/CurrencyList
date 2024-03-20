package com.example.currencylist.data.local


import com.example.currencylist.domain.Currency
class CurrencyDataSourceImpl(private val currencyDao: CurrencyDao): CurrencyDataSource {
    override suspend fun getCurrencyList(): List<Currency> {
        return currencyDao.getAll().map {
            return@map it.code?.let { code ->
                Currency.Fiat(
                    id = it.id,
                    name = it.name,
                    symbol = it.symbol,
                    code = code
                )
            } ?: Currency.Crypto(
                    id = it.id,
                    name = it.name,
                    symbol = it.symbol,
            )
        }
    }
}