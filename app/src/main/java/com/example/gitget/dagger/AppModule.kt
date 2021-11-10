package com.example.gitget.dagger

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.gitget.App
import com.example.gitget.utils.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface AppModule {
    @Binds
    fun bindApplicationContext(application: App): Context

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}