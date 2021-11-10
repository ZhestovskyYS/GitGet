package com.example.gitget.presentation.fragments.itemList

import androidx.lifecycle.ViewModel
import com.example.gitget.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ItemListModule {
    @Binds
    @IntoMap
    @ViewModelKey(ItemListViewModel::class)
    fun bindItemListViewModel(viewModel: ItemListViewModel): ViewModel
}