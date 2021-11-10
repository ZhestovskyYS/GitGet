package com.example.gitget.presentation.activity

import com.example.gitget.presentation.fragments.itemDetails.ItemDetailsFragment
import com.example.gitget.presentation.fragments.itemDetails.ItemDetailsModule
import com.example.gitget.presentation.fragments.itemList.ItemListFragment
import com.example.gitget.presentation.fragments.itemList.ItemListModule
import com.example.gitget.utils.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ItemListModule::class])
    fun contributeToItemListFragment(): ItemListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ItemDetailsModule::class])
    fun contributeToItemDetailsFragment(): ItemDetailsFragment
}