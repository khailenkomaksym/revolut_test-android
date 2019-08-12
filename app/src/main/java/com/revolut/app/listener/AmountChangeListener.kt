package com.revolut.app.listener

interface AmountChangeListener {
    fun onChange (amount: Double)
}