package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.impl.RepositoryFactory

class JobPositionViewModel : ViewModel() {

    private val repo = RepositoryFactory.githubJobRepo
    private val page = MutableLiveData<Int>()
    private val positionId = MutableLiveData<String>()

    private var description = ""
    private var location = ""
    private var fullTime = false

    var loadState: Boolean = false

    val dataSource: LiveData<DataSource<List<JobPositionDTO>>> = Transformations.switchMap(page) {
                repo.findPositions(description, location, fullTime, it)
            }

    val positions = MutableLiveData<List<JobPositionDTO>>()

    val position: LiveData<DataSource<JobPositionDTO>> = Transformations.switchMap(positionId) {
                repo.getPosition(it)
            }

    fun find() {
        loadState = true
        page.value = 0
    }

    fun loadMore() {
        loadState = false
        page.value?.inc()
    }

}