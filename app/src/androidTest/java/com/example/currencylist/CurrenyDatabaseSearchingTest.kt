package com.example.currencylist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencylist.data.entities.CurrencyDto
import com.example.currencylist.data.local.CurrencyDao
import com.example.currencylist.data.local.CurrencyDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.any
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith


import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var currencyDao: CurrencyDao
    private lateinit var db: CurrencyDatabase
    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CurrencyDatabase::class.java).build()
        currencyDao = db.currencyDao()
        val currencies: List<CurrencyDto> = testingData()
        currencies.forEach {
            currencyDao.insertCurrency(it)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `name starts with the search item`() = runTest {
        val currencyByKeywordSearch1 = currencyDao.searchByKeywordAndType("foo", currencyTypes = listOf("fiat", "crypto"))
        assertThat(currencyByKeywordSearch1.firstOrNull()?.name, equalTo("Foobar"))

        val currencyByKeywordSearch2 = currencyDao.searchByKeywordAndType("Ethereum", currencyTypes = listOf("fiat", "crypto"))
        assertThat(currencyByKeywordSearch2.map { it.name }, hasItems(
            "Ethereum", "Ethereum Classic"
        ))
    }

    @Test
    fun `name contains a partial match with space prefixed to the search term`() = runTest {
        val currencyByKeyword = currencyDao.searchByKeywordAndType("Classic", currencyTypes = listOf("fiat", "crypto"))
        val currencyByKeywordNames = currencyByKeyword.map { it.name }
        assertThat(currencyByKeywordNames, hasItems("Ethereum Classic"))
        assertThat(currencyByKeywordNames, not(hasItems("Tronclassic")))
    }

    @Test
    fun `symbol starts with the search term`() = runTest {
        val currencyByKeyword = currencyDao.searchByKeywordAndType("ET", currencyTypes = listOf("fiat", "crypto"))
        val currencyByKeywordSymbols = currencyByKeyword.map { it.symbol }
        assertThat(currencyByKeywordSymbols, hasItems("ETH", "ETC", "ETN"))
        assertThat(currencyByKeywordSymbols, not(hasItems("BET")))
    }
}

fun testingData(): List<CurrencyDto> {
    return listOf(
        CurrencyDto(
            id = "Foobar",
            name = "Foobar",
            symbol = "Foobar",
            code = "Foobar",
            type = "fiat"
        ),
        CurrencyDto(
            id = "Barfoo",
            name = "Barfoo",
            symbol = "Barfoo",
            code = "Barfoo",
            type = "fiat"
        ),
        CurrencyDto(
            id = "ETH",
            name = "Ethereum",
            symbol = "ETH",
            code = null,
            type = "crypto"
        ),
        CurrencyDto(
            id = "ETC",
            name = "Ethereum Classic",
            symbol = "ETC",
            code = null,
            type = "crypto"
        ),
        CurrencyDto(
            id = "Tronclassic",
            name = "Tronclassic",
            symbol = "Tronclassic",
            code = null,
            type = "crypto"
        ),
        CurrencyDto(
            id = "ETN",
            name = "ETN",
            symbol = "ETN",
            code = null,
            type = "crypto"
        ),
        CurrencyDto(
            id = "BET",
            name = "BET",
            symbol = "BET",
            code = null,
            type = "crypto"
        )
    )
}