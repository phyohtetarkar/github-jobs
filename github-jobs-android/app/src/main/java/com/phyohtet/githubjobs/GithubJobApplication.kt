package com.phyohtet.githubjobs

import android.app.Application
import com.squareup.picasso.Picasso

class GithubJobApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Picasso.get().setIndicatorsEnabled(true)
    }

}