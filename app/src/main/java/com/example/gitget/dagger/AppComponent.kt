package com.example.gitget.dagger

import com.example.gitget.App
import com.example.gitget.network.GitApiService
import com.example.gitget.utils.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector

@AppScope
@Component(modules = [
    AppModule::class,
    ActivitiesModule::class,
    GitApiService::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }
}