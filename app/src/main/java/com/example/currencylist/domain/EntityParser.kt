package com.example.currencylist.domain

import com.example.currencylist.data.entities.CurrencyDto

fun CurrencyDto.toCurrency(): Currency {
    return when {
        type == CurrencyType.FIAT.stringKey && !code.isNullOrBlank() -> Currency.Fiat(
            id = id,
            name = name,
            symbol = symbol,
            code = code
        )
        type == CurrencyType.CRYPTO.stringKey -> Currency.Crypto(
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
            id, name, symbol, type = CurrencyType.FIAT.stringKey, code
        )
        is Currency.Crypto -> CurrencyDto(
            id, name, symbol, type = CurrencyType.CRYPTO.stringKey, null
        )
    }

}