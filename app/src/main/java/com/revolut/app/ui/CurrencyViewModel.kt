package com.revolut.app.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.revolut.app.repository.NetworkRepository
import com.revolut.app.Constants.NETWORK.Companion.BASE_CURRENCY
import com.revolut.app.data.CurrencyResponse
import com.revolut.app.data.CurrencyValue
import io.reactivex.Observable
import kotlin.collections.ArrayList


class CurrencyViewModel(val networkRepository: NetworkRepository) : ViewModel() {

    var amount: Double = 1.0

    var listCurrency: ArrayList<CurrencyValue> = ArrayList()
    var mapCurrencyPosition: HashMap<String, Int> = HashMap()

    var currency: String = BASE_CURRENCY

    fun getCurrencyList(): Observable<CurrencyResponse> = networkRepository.getCurrencyList(currency)

    fun setCurrency(position: Int) {
        currency = listCurrency.get(position).title!!
    }

    fun setNewAmount(position: Int) {
        amount *= listCurrency.get(position).value!!
    }

    fun moveItem(position: Int) {

        val item0: CurrencyValue = listCurrency.get(0)
        val itemPosition: CurrencyValue = listCurrency.get(position)
        itemPosition.value = 1.0

        mapCurrencyPosition.put(item0.title!!, position)
        mapCurrencyPosition.put(itemPosition.title!!, 0)

        listCurrency.remove(itemPosition)
        listCurrency.add(0, itemPosition)
    }

}