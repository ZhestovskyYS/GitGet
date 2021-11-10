package com.example.gitget.viewModel

import androidx.lifecycle.*
import com.example.gitget.data.RepoItem
import com.example.gitget.network.models.SimpleRepositoryInfo
import com.example.gitget.network.repository.GitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoSearchViewModel @Inject constructor(
    private val gitRepository: GitRepository
) : ViewModel() {

    private val startInfo = mutableListOf<SimpleRepositoryInfo>()

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _allRepoItem = MutableLiveData<List<RepoItem>>()
    val allRepoItem: LiveData<List<RepoItem>> get() = _allRepoItem

    private suspend fun getRepoSimpleInfo(searchText: String): List<SimpleRepositoryInfo> =
        gitRepository.search(searchText)

    private suspend fun getDetails(info: SimpleRepositoryInfo) {
        val details = gitRepository.getDetails(info)
        _date.postValue(details?.commit?.details?.author?.date)
    }

    fun initializeInfo(searchText: String) {
        if (isSearchTextValid(searchText)) {
            viewModelScope.launch(Dispatchers.IO) {
                startInfo.addAll(getRepoSimpleInfo(searchText))
                fillAllRepoItem()
            }
        } else return
    }

    private fun isSearchTextValid(searchText: String): Boolean =
        searchText != "" && searchText.isNotEmpty()

    fun initializeDate(itemId: Int) {
        if (startInfo.isNotEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                val infoItem = SimpleRepositoryInfo(
                    startInfo[itemId].repositoryName, startInfo[itemId].repositoryURL,
                    startInfo[itemId].repositoryOwner
                )
                getDetails(infoItem)
            }
        else return
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

    fun clearDateOnCancel(){
        _date.value = ""
    }

    fun clearAllRepoItem() {
        startInfo.clear()
        _allRepoItem.value = mutableListOf()

    }

    fun isEntryValid(
        repoName: String,
        lastCommitDate: String,
        repoOwner: String
    ): Boolean {
        return !(repoName.isBlank() || lastCommitDate.isBlank() || repoOwner.isBlank())
    }

    fun updateRepo(
        id: Int,
        repoName: String,
        repoUrl: String,
        repoOwner: String,
        date: String
    ) {
        val updatedRepoItem = getUpdatedRepoEntry(id, repoName, repoUrl, repoOwner, date)
        updateRepo(updatedRepoItem)
    }

    fun retrieveRepo(currentItemIndex: Int) = allRepoItem.value!![currentItemIndex]

    private fun updateRepo(repoItem: RepoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            _allRepoItem.value?.get(repoItem.id)!!.apply {
                repoName = repoItem.repoName
                repoOwner = repoItem.repoOwner
                lastCommitDate = repoItem.lastCommitDate
            }
        }
    }

    private fun getUpdatedRepoEntry(
        id: Int,
        repoName: String,
        repoUrl: String,
        repoOwner: String,
        date: String
    ): RepoItem {
        return RepoItem(
            id = id,
            repoName = repoName,
            repoUrl = repoUrl,
            repoOwner = repoOwner,
            lastCommitDate = date
        )
    }

}
