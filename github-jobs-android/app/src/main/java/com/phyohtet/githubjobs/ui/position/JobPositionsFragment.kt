package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.DataSource
import kotlinx.android.synthetic.main.fragment_job_positions.*

class JobPositionsFragment : Fragment() {

    private val TAG = "JobPositionsFragment"

    private lateinit var viewModel: JobPositionsViewModel
    private lateinit var positionAdapter: JobPositionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JobPositionsViewModel::class.java)
        positionAdapter = JobPositionAdapter()

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_job_positions, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_filter, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = positionAdapter
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy < 0) {
                    return
                }

                val size = positionAdapter.itemCount
                val last = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val threshold = 4


                if (!viewModel.loadMore && size <= (last + threshold)) {
                    Log.v(TAG, "Load More")
                    viewModel.loadMore()
                }

            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.positions.observe(this, Observer {
            when (it?.status) {
                DataSource.Status.LOADING -> {
                    if (!viewModel.loadMore) {
                        progress.visibility = View.VISIBLE
                    }
                }
                DataSource.Status.ERROR -> {
                    progress.visibility = View.GONE
                }
                DataSource.Status.SUCCESS -> {
                    progress.visibility = View.GONE
                    if (!viewModel.loadMore) {
                        positionAdapter.submitList(it.data?.toMutableList())
                    } else {
                        viewModel.loadMore = false
                        positionAdapter.appendList(it.data?.toMutableList())
                    }

                }
            }
        })

        viewModel.find()

    }

}