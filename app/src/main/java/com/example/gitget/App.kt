package com.example.gitget


import dagger.android.DaggerApplication
import com.example.gitget.dagger.AppComponent
import dagger.android.AndroidInjector

class App : DaggerApplication() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        initDagger()
        super.onCreate()
        appComponent.inject(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    fun initDagger() {
        appComponent = DaggerAppComponent.bilder()
            .application(this)
            .build()
    }
}