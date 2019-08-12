package com.revolut.app.api

import com.revolut.app.data.CurrencyResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("latest")
    fun getLatestCurrencyValues(@Query("base") base: String): Observable<CurrencyResponse>

}