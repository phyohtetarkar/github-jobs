package com.phyohtet.githubjobs.model

import androidx.lifecycle.LiveData

data class NetworkResource<T>(
        val liveData: LiveData<T>,
        val networkState: LiveData<NetworkState>,
        val refreshState: LiveData<NetworkState>,
        val refresh: () -> Unit,
        val retry: () -> Unit)