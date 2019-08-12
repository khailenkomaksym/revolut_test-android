package com.revolut.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolut.app.repository.NetworkRepository

class CurrencyViewModelFactory(val networkRepository: NetworkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(networkRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}