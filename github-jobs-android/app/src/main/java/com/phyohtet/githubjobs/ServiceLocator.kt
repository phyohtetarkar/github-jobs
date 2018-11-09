package com.phyohtet.githubjobs

import android.content.Context
import com.phyohtet.githubjobs.model.api.GithubJobApi
import com.phyohtet.githubjobs.model.api.RetrofitSingleton
import com.phyohtet.githubjobs.model.repo.GithubJobRepo
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

}

class DefaultServiceLocator(ctx: Context) : ServiceLocator {

    private val repo: GithubJobRepo by lazy {
        val api = RetrofitSingleton.create(GithubJobApi::class.java)
        return@lazy GithubJobRepo(api, Executors.newFixedThreadPool(3))
    }

    override fun getGitHubJobRepo(): GithubJobRepo = repo

}