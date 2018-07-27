package com.phyohtet.githubjobs.ui.position.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.impl.RepositoryFactory

class JobPositionDetailViewModel : ViewModel() {

    private val repo = RepositoryFactory.githubJobRepo

    val positionId = MutableLiveData<String>()

    val position: LiveData<DataSource<JobPositionDTO>> = Transformations.switchMap(positionId) {
        repo.getPosition(it)
    }

}