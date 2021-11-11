package com.example.gitget.presentation.fragments.itemDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitget.data.ArgData
import com.example.gitget.data.RepoItem
import com.example.gitget.network.models.SimpleRepositoryInfo
import com.example.gitget.network.repository.GitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemDetailsViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val _args = MutableLiveData<ArgData>()
    val args: LiveData<ArgData> = _args

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private suspend fun getDetails(
        repoName: String,
        repoOwner: String
    ) {
        val details = gitRepository.getDetails(repoName, repoOwner)
        _date.postValue(details?.commit?.details?.author?.date)
    }

    fun initializeDate(
        repoName: String,
        repoOwner: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getDetails(repoName, repoOwner)
        }
    }

    fun initializeArgs(
        itemId: Int,
        repoName: String,
        repoOwner: String,
        date: String
    ){
        _args.value = ArgData(
            itemId,
            repoName,
            repoOwner,
            date
        )
    }

    fun isEntryValid(
        repoName: String,
        lastCommitDate: String,
        repoOwner: String
    ): Boolean {
        return !(repoName.isBlank() || lastCommitDate.isBlank() || repoOwner.isBlank())
    }

    fun clearDateOnCancel() {
        _date.value = ""
    }
}