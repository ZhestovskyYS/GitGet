package com.example.gitget.presentation.fragments.itemList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitget.data.RepoItem
import com.example.gitget.network.models.SimpleRepositoryInfo
import com.example.gitget.network.repository.GitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ItemListViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val startInfo = mutableListOf<SimpleRepositoryInfo>()

    private val _allRepoItem = MutableLiveData<List<RepoItem>>()
    val allRepoItem: LiveData<List<RepoItem>> get() = _allRepoItem

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
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    fun clearAllRepoItem() {
        startInfo.clear()
        _allRepoItem.value = mutableListOf()

    }

    private fun updateRepo(id: Int, repoName: String, repoOwner: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRepo = _allRepoItem.value!![id].copy(
                repoName = repoName,
                repoOwner = repoOwner,
                lastCommitDate = date
            )
            _allRepoItem.value!!.minus(_allRepoItem.value!![id])
            _allRepoItem.value!!.plus(newRepo)
        }
    }

    private fun isSearchTextValid(searchText: String): Boolean =
        searchText != "" && searchText.isNotEmpty()

}