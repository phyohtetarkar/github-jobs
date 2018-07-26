package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.DataSource
import kotlinx.android.synthetic.main.fragment_job_positions.*
import kotlinx.android.synthetic.main.layout_progress.*

class JobPositionsFragment : Fragment() {

    private lateinit var viewModel: JobPositionViewModel
    private lateinit var positionAdapter: JobPositionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JobPositionViewModel::class.java)
        positionAdapter = JobPositionAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_job_positions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            adapter = positionAdapter
        }

        fabFilter.setOnClickListener {

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.positions.observe(this, Observer {
            when (it?.status) {
                DataSource.Status.LOADING -> {
                    if (viewModel.loadState) {
                        progressBar.visibility = View.VISIBLE
                    }
                }
                DataSource.Status.ERROR -> {
                    progressBar.visibility = View.GONE
                }
                DataSource.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    if (viewModel.loadState) {
                        positionAdapter.submitList(it.data?.toMutableList())
                    } else {
                        positionAdapter.appendList(it.data?.toMutableList())
                    }

                }
            }
        })

        viewModel.find()

    }

}