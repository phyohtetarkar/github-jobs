package com.phyohtet.githubjobs.ui.position

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.ServiceLocator
import com.phyohtet.githubjobs.model.JobPositionSearch
import com.phyohtet.githubjobs.model.Status
import kotlinx.android.synthetic.main.fragment_job_positions.*

class JobPositionsFragment : Fragment() {

    companion object {
        private const val TAG = "JobPositionsFragment"
        private const val FILTER_DIALOG_TAG = "JobPositionsFilter"
    }

    private var viewModel: JobPositionsViewModel? = null
    private val positionAdapter: JobPositionAdapter by lazy { JobPositionAdapter { viewModel?.retry() } }
    private var init: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.let {
             ViewModelProviders.of(it, object : ViewModelProvider.Factory {
                 override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                     val repo = ServiceLocator.getInstance(it).getGitHubJobRepo()
                     @Suppress("UNCHECKED_CAST")
                     return JobPositionsViewModel(repo) as T
                 }
             })[JobPositionsViewModel::class.java]
        }
        setHasOptionsMenu(true)

        viewModel?.positions?.observe(this, Observer {
            positionAdapter.submitList(it)
            if (it.size > 0) {
                tvNoPosition.visibility = View.GONE
            } else {
                tvNoPosition.visibility = View.VISIBLE
            }
        })

        viewModel?.networkState?.observe(this, Observer {
            positionAdapter.setNetworkState(it)
        })

        viewModel?.refreshState?.observe(this, Observer {
            tvNoPosition.visibility = View.GONE
            when (it.status) {
                Status.LOADING -> progress.visibility = View.VISIBLE
                Status.SUCCESS -> progress.visibility = View.GONE
                Status.FAILED -> {
                    progress.visibility = View.GONE
                    tvNoPosition.visibility = View.VISIBLE
                    tvNoPosition.text = it.msg
                }
            }
        })

        positionAdapter.onPositionViewClickListener = {view, pos ->
            positionAdapter.getItemAt(pos)?.also {
                val args = Bundle()
                args.putString("id", it.id)
                view.findNavController().navigate(R.id.action_jobPositionsFragment_to_jobPositionDetailFragment, args)
            }
        }

        init = { viewModel?.search?.value = JobPositionSearch() }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_job_positions, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_filter -> {
                view?.findNavController()?.navigate(R.id.action_jobPositionsFragment_to_jobPositionsFilterFragment)
                return true
            }

            R.id.action_refresh -> {
                viewModel?.search()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = positionAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init?.invoke()
        init = null
    }

}