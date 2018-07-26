package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.model.dto.JobPositionDTO
import com.phyohtet.githubjobs.model.repo.impl.RepositoryFactory

class JobPositionsViewModel : ViewModel() {

    private val repo = RepositoryFactory.githubJobRepo
    private val page = MutableLiveData<Int>()
    private val positionId = MutableLiveData<String>()

    private var description = ""
    private var location = ""
    private var fullTime = false

    var loadMore: Boolean = false

    val positions: LiveData<DataSource<List<JobPositionDTO>>> = Transformations.switchMap(page) {
                repo.findPositions(description, location, fullTime, it)
            }

    val position: LiveData<DataSource<JobPositionDTO>> = Transformations.switchMap(positionId) {
                repo.getPosition(it)
            }

    fun find() {
        loadMore = false
        page.value = 0
    }

    fun loadMore() {
        loadMore = true
        page.value = page.value?.let { it + 1 }
    }

}