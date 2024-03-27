package com.example.currencylist.data.local


import com.example.currencylist.data.entities.CurrencyDto
import kotlinx.coroutines.flow.Flow

class CurrencyDataSourceImpl(private val currencyDao: CurrencyDao): CurrencyDataSource {
    override fun getCurrencyListStream(): Flow<List<CurrencyDto>> {
        return currencyDao.getAllStream()
    }

    override suspend fun getCurrencyList(currencyTypes: Set<String>): List<CurrencyDto> {
        if (currencyTypes.isNotEmpty()) {
            return currencyDao.getListByType(currencyTypes.map { it.lowercase() })
        } else {
            return currencyDao.getAllList()
        }
    }

    override suspend fun searchCurrencyList(
        searchText: String,
        currencyTypes: Set<String>
    ): List<CurrencyDto> {
        return currencyDao.searchByKeywordAndType(searchText, currencyTypes.map{ it.lowercase() })
    }

    override suspend fun insertCurrencyDataBase(currency: CurrencyDto) {
        return currencyDao.insertCurrency(currency)
    }

    override suspend fun deleteCurrencyDataBase() {
        return currencyDao.deleteAll()
    }
}