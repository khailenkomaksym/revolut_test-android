package com.revolut.app.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ContextModule internal constructor(
    @get:Provides
    @get:Named("ApplicationContext")
    internal var context: Context
)