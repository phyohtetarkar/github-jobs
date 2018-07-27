package com.phyohtet.githubjobs.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.ui.position.JobPositionsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.contentMain, JobPositionsFragment())
            .commit()
    }
}
