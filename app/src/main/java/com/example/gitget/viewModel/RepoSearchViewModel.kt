package com.example.gitget.viewModel

import androidx.lifecycle.*
import com.example.gitget.data.RepoItem
import com.example.gitget.network.models.SimpleRepositoryInfo
import com.example.gitget.network.repository.GitRepImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val DATE = ""

class RepoSearchViewModel : ViewModel() {

    var searchText: String = ""
    var currentItemIndex: Int = 0

    private val startInfo = mutableListOf<SimpleRepositoryInfo>()

    val _date2 = MutableLiveData<String>()
    val date2: LiveData<String> get() = _date2

    val allRepoItem = MutableLiveData<List<RepoItem>>()

    /**
     *  Retrieve [SimpleRepositoryInfo] from query to the web-server
     */
    private suspend fun getRepoSimpleInfo(): List<SimpleRepositoryInfo> {
        return GitRepImpl.search(searchText)
    }

    /**
     *  Retrieve date of last commit from server
     */
    private suspend fun getDetails(info: SimpleRepositoryInfo) {
        val details = GitRepImpl.getDetails(info)
        _date2.postValue(details?.commit?.details?.author?.date)
    }

    /**
     *  Get all the retrieved from the net data and put it into the viewModel instances
     */
    fun initializeInfo() {
        if (isSearchTextValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                startInfo.addAll(getRepoSimpleInfo())
                fillAllRepoItem()
            }
        } else return
    }

    private fun isSearchTextValid(): Boolean = searchText != "" && searchText.isNotEmpty()

    fun initializeDate() {
        if (startInfo.isNotEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                val infoItem = SimpleRepositoryInfo(
                    startInfo[currentItemIndex].repositoryName, startInfo[currentItemIndex].repositoryURL,
                    startInfo[currentItemIndex].repositoryOwner
                )
                getDetails(infoItem)
            }
        else return
    }

    fun fillAllRepoItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val allRepoInstance = mutableListOf<RepoItem>()
            startInfo.forEach {
                allRepoInstance.add(
                    RepoItem(
                        repoName = it.repositoryName,
                        repoUrl = it.repositoryURL,
                        repoOwner = it.repositoryOwner.userName,
                        lastCommitDate = DATE
                    )
                )
            }
            allRepoItem.postValue(allRepoInstance)
        }
    }

    fun clearAllRepoItem() {
        this.allRepoItem.value = mutableListOf<RepoItem>()
    }

    fun isEntryValid(
        repoName: String,
        lastCommitDate: String,
        repoOwner: String
    ): Boolean {
        return !(repoName.isBlank() || lastCommitDate.isBlank() || repoOwner.isBlank())
    }

    fun updateRepo(
        repoName: String,
        repoUrl: String,
        repoOwner: String,
        date: String
    ) {
        val updatedRepoItem = getUpdatedRepoEntry(repoName, repoUrl, repoOwner, date)
        updateRepo(updatedRepoItem)
    }

    fun getCurrentRepoItemIndex(name: String) {
        startInfo.forEachIndexed { index, it ->
            if (it.repositoryName == name)
                currentItemIndex = index
        }
    }

    fun retrieveRepo() = RepoItem(
        repoName = startInfo[currentItemIndex].repositoryName,
        repoUrl = startInfo[currentItemIndex].repositoryURL,
        repoOwner = startInfo[currentItemIndex].repositoryOwner.userName,
        lastCommitDate = DATE
    )

    private fun updateRepo(repoItem: RepoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            allRepoItem.value?.get(currentItemIndex)!!.apply {
                repoName = repoItem.repoName
                repoOwner = repoItem.repoOwner
                repoUrl = repoItem.repoUrl
                lastCommitDate = repoItem.lastCommitDate
            }
        }
    }

    private fun getUpdatedRepoEntry(
        repoName: String,
        repoUrl: String,
        repoOwner: String,
        date: String
    ): RepoItem {
        return RepoItem(
            repoName = repoName,
            repoUrl = repoUrl,
            repoOwner = repoOwner,
            lastCommitDate = date
        )
    }

}

class RepoSearchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepoSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepoSearchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}