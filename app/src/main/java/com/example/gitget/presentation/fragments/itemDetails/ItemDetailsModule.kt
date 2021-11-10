package com.example.gitget.presentation.fragments.itemDetails

import androidx.lifecycle.ViewModel
import com.example.gitget.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ItemDetailsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ItemDetailsViewModel::class)
    fun bindItemDetailsViewModel(viewModel: ItemDetailsViewModel): ViewModel
}
