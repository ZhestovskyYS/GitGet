package com.example.gitget.presentation.fragments.itemDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitget.network.models.SimpleRepositoryInfo
import com.example.gitget.network.repository.GitRepository
import javax.inject.Inject

class ItemDetailsViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private suspend fun getDetails(info: SimpleRepositoryInfo) {
        val details = gitRepository.getDetails(info)
        _date.postValue(details?.commit?.details?.author?.date)
    }

    fun clearDateOnCancel(){
        _date.value = ""
    }
}