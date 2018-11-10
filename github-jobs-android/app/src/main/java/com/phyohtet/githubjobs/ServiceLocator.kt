package com.phyohtet.githubjobs

import android.content.Context
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.api.RetrofitSingleton
import com.phyohtet.githubjobs.model.repo.GithubJobRepo
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ServiceLocator {

    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null

        fun getInstance(ctx: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(ctx.applicationContext)
                }

                return instance!!
            }
        }
    }

    fun getGitHubJobRepo(): GithubJobRepo

    fun getGitHubJobApi(): GithubJobApi

    fun getNetworkExecutor(): Executor

}

class DefaultServiceLocator(ctx: Context) : ServiceLocator {

    private val api: GithubJobApi by lazy {
        RetrofitSingleton.create(GithubJobApi::class.java)
    }

    private val NETWORK_IO = Executors.newFixedThreadPool(3)

    override fun getGitHubJobRepo(): GithubJobRepo {
        return GithubJobRepo(getGitHubJobApi(), getNetworkExecutor())
    }

    override fun getGitHubJobApi(): GithubJobApi = api

    override fun getNetworkExecutor(): Executor = NETWORK_IO

}