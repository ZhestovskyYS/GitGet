package com.example.gitget.presentation.fragments.itemList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitget.data.RepoItem
import com.example.network.models.SimpleRepositoryInfo
import com.example.network.repository.GitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemListViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val startInfo = mutableListOf<SimpleRepositoryInfo>()

    private val _allRepoItem = MutableLiveData<MutableList<RepoItem>>()
    val allRepoItem: LiveData<MutableList<RepoItem>> get() = _allRepoItem

    private suspend fun getRepoSimpleInfo(searchText: String): List<SimpleRepositoryInfo> =
        gitRepository.search(searchText)

    fun initializeInfo(searchText: String) {
        if (isSearchTextValid(searchText)) {
            viewModelScope.launch(Dispatchers.IO) {
                startInfo.addAll(getRepoSimpleInfo(searchText))
                fillAllRepoItem()
            }
        } else return
    }

    private fun fillAllRepoItem() {
        val allRepoInstance = mutableListOf<RepoItem>()
        startInfo.forEachIndexed { index, it ->
            allRepoInstance.add(
                RepoItem(
                    id = index,
                    repoName = it.repositoryName,
                    repoUrl = it.repositoryURL,
                    repoOwner = it.repositoryOwner.userName,
                    lastCommitDate = ""
                )
            )
        }
        _allRepoItem.postValue(allRepoInstance)
    }

    fun clearAllRepoItem() {
        startInfo.clear()
        _allRepoItem.value = mutableListOf()

    }

    fun updateRepo(id: Int, repoName: String, repoOwner: String, date: String) {
        val newRepo = _allRepoItem.value!![id].copy(
            repoName = repoName,
            repoOwner = repoOwner,
            lastCommitDate = date
        )
        _allRepoItem.value!![id] = newRepo
    }

    private fun isSearchTextValid(searchText: String): Boolean =
        searchText != "" && searchText.isNotEmpty()

}