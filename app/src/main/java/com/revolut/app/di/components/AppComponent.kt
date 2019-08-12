package com.revolut.app.di.components

import com.revolut.app.di.modules.ContextModule
import com.revolut.app.di.modules.NetworkModule
import com.revolut.app.ui.CurrencyActivity
import dagger.Component

@Component(modules = [NetworkModule::class, ContextModule::class])
interface AppComponent {
    fun injectsMainActivity(currencyActivity: CurrencyActivity)
}