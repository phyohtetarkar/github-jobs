package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.DataSource
import com.phyohtet.githubjobs.ui.custom.RecyclerViewItemTouchListener
import com.phyohtet.githubjobs.ui.position.detail.JobPositionDetailFragment
import kotlinx.android.synthetic.main.fragment_job_positions.*

class JobPositionsFragment : Fragment() {

    companion object {
        private const val TAG = "JobPositionsFragment"
        private const val FILTER_DIALOG_TAG = "JobPositionsFilter"
        private const val LOAD_MORE_DELAY = 250L
    }

    private lateinit var viewModel: JobPositionsViewModel
    private lateinit var positionAdapter: JobPositionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.also {
            viewModel = ViewModelProviders.of(it).get(JobPositionsViewModel::class.java)
        }
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.action_filter) {

            val ft = fragmentManager?.beginTransaction()
            val prev = fragmentManager?.findFragmentByTag(FILTER_DIALOG_TAG)

            if (prev != null) {
                ft?.remove(prev)
            }

            ft?.addToBackStack(null)

            val frag = JobPositionsFilterFragment()
            frag.show(ft, FILTER_DIALOG_TAG)

            return true
        }

        return super.onOptionsItemSelected(item)
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

                if (dy <= 0) {
                    return
                }

                val size = positionAdapter.itemCount
                val last = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val threshold = 2


                if (!viewModel.loadMore && size <= (last + threshold)) {
                    Handler().postDelayed( { positionAdapter.append(null) }, LOAD_MORE_DELAY)
                    viewModel.loadMore()
                }

            }
        })

        recyclerView.addOnItemTouchListener(RecyclerViewItemTouchListener(context, object : RecyclerViewItemTouchListener.OnTouchListener {
            override fun onTouch(view: View, position: Int) {
                positionAdapter.getItemAt(position)?.also { dto ->
                    val frag = JobPositionDetailFragment()
                    val b = Bundle().also {
                        it.putString("id", dto.id)
                    }
                    frag.arguments = b

                    fragmentManager?.beginTransaction()
                            ?.addToBackStack(null)
                            ?.replace(R.id.contentMain, frag)
                            ?.commit()
                }
            }

        }))

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
                        positionAdapter.submitList(it.data)
                        if (positionAdapter.itemCount > 0) {
                            tvNoPosition.visibility = View.GONE
                        } else {
                            tvNoPosition.visibility = View.VISIBLE
                        }
                    } else {
                        viewModel.loadMore = false
                        // remove loading view
                        positionAdapter.remove(positionAdapter.itemCount - 1)
                        positionAdapter.appendList(it.data)
                    }

                }
            }
        })

        viewModel.find()

    }

}