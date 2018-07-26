package com.phyohtet.githubjobs.model.repo.impl

import com.phyohtet.githubjobs.model.api.RetrofitSingleton
import com.phyohtet.githubjobs.model.api.service.GithubJobService
import com.phyohtet.githubjobs.model.repo.GithubJobRepo
import com.phyohtet.githubjobs.model.repo.Repository

object RepositoryFactory {

    private val cache = mutableMapOf<Class<out Repository>, Repository>()

    val githubJobRepo: GithubJobRepo
        get() {

            var repository = cache[GithubJobRepo::class.java]

            if (repository == null) {
                repository = GithubJobRepoImpl(RetrofitSingleton.create(GithubJobService::class.java))
                cache[GithubJobRepo::class.java] = repository
            }

            return repository as GithubJobRepo
        }

}