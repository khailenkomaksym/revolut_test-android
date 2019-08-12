package com.revolut.app.repository

import com.revolut.app.api.CurrencyAPI
import com.revolut.app.data.CurrencyResponse
import io.reactivex.Observable


class NetworkRepository(val currencyAPI: CurrencyAPI) {

    fun getCurrencyList(baseCurrency: String): Observable<CurrencyResponse> {
        return currencyAPI.getLatestCurrencyValues(baseCurrency)
    }


}