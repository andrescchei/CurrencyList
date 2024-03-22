package com.example.currencylist.data.local


import com.example.currencylist.data.entities.CurrencyDto
import com.example.currencylist.domain.Currency
import com.example.currencylist.domain.CurrencyType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyDataSourceImpl(private val currencyDao: CurrencyDao): CurrencyDataSource {
    override fun getCurrencyListStream(): Flow<List<Currency>> {
        return currencyDao.getAllStream().map { currencies ->
            return@map currencies.map {
                it.toCurrency()
            }
        }
    }

    override suspend fun getCurrencyList(currencyTypes: Set<CurrencyType>): List<Currency> {
        if (currencyTypes.isNotEmpty()) {
            return currencyDao.getListByType(currencyTypes.map { it.name }).map {
                it.toCurrency()
            }
        } else {
            return currencyDao.getAllList().map { it.toCurrency() }
        }
    }

    override suspend fun searchCurrencyList(
        searchText: String,
        currencyTypes: Set<CurrencyType>
    ): List<Currency> {
        return currencyDao.searchByKeywordAndType(searchText, currencyTypes.map{ it.name }).map {
            it.toCurrency()
        }
    }

    override suspend fun insertCurrencyDataBase(currency: Currency) {
        return currencyDao.insertCurrency(currency.toCurrencyDto())
    }

    override suspend fun deleteCurrencyDataBase() {
        return currencyDao.deleteAll()
    }
}

fun CurrencyDto.toCurrency(): Currency {
    return when {
        type == "fiat" && !code.isNullOrBlank() -> Currency.Fiat(
            id = id,
            name = name,
            symbol = symbol,
            code = code
        )
        type == "crypto" -> Currency.Crypto(
            id = id,
            name = name,
            symbol = symbol,
        )
        else -> throw Exception("parsing error")
    }
}

fun Currency.toCurrencyDto(): CurrencyDto {
    return when(this) {
        is Currency.Fiat -> CurrencyDto(
            id, name, symbol, type = "fiat", code
        )
        is Currency.Crypto -> CurrencyDto(
            id, name, symbol, type = "crypto", null
        )
    }

}