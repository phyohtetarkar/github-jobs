package com.phyohtet.githubjobs.ui.position

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.phyohtet.githubjobs.model.JobPositionSearch
import com.phyohtet.githubjobs.model.NetworkResource
import com.phyohtet.githubjobs.model.NetworkState
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.GithubJobRepo

class JobPositionsViewModel(private val repo: GithubJobRepo) : ViewModel() {

    val search = MutableLiveData<JobPositionSearch>()

    private val result: LiveData<NetworkResource<PagedList<JobPositionDTO>>> = Transformations.map(search) {
        repo.findPositions(it)
    }

    val positions: LiveData<PagedList<JobPositionDTO>> = Transformations.switchMap(result) { it.liveData }

    val networkState: LiveData<NetworkState> = Transformations.switchMap(result) { it.networkState }

    val refreshState: LiveData<NetworkState> = Transformations.switchMap(result) { it.refreshState }

    fun search() {
        search.value = search.value
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun retry() {
        result.value?.retry?.invoke()
    }

}