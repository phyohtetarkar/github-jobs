package com.phyohtet.githubjobs.ui.position.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.phyohtet.githubjobs.model.NetworkResource
import com.phyohtet.githubjobs.model.NetworkState
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.GithubJobRepo

class JobPositionDetailViewModel(private val repo: GithubJobRepo) : ViewModel() {

    val positionId = MutableLiveData<String>()

    private val result: LiveData<NetworkResource<JobPositionDTO>> = Transformations.map(positionId) {
        repo.getPosition(it)
    }

    val position: LiveData<JobPositionDTO> = Transformations.switchMap(result) { it.liveData }

    val networkState: LiveData<NetworkState> = Transformations.switchMap(result) { it.networkState }

}