package com.phyohtet.githubjobs.ui.position

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
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
import com.phyohtet.githubjobs.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_job_positions.*

class JobPositionsFragment : Fragment() {

    companion object {
        private const val TAG = "JobPositionsFragment"
        private const val FILTER_DIALOG_TAG = "JobPositionsFilter"
    }

    private var viewModel: JobPositionsViewModel? = null
    private val positionAdapter: JobPositionAdapter by lazy { JobPositionAdapter { viewModel?.retry() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.let {
             ViewModelProviders.of(it, object : ViewModelProvider.Factory {
                 override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                     val repo = ServiceLocator.getInstance(it).getGitHubJobRepo()
                     @Suppress("UNCHECKED_CAST")
                     return JobPositionsViewModel(repo) as T
                 }
             })[JobPositionsViewModel::class.java]
        }

        viewModel?.positions?.observe(this, Observer {
            positionAdapter.submitList(it)
            recyclerView.smoothScrollToPosition(0)

        })

        viewModel?.networkState?.observe(this, Observer {
            positionAdapter.setNetworkState(it)
        })

        viewModel?.refreshState?.observe(this, Observer {
            tvMessage.visibility = View.GONE
            tvMessage.text = null
            when (it.status) {
                Status.LOADING -> swipeRefreshLayout.isRefreshing = true
                Status.SUCCESS -> swipeRefreshLayout.isRefreshing = false
                Status.NOT_FOUND -> {
                    swipeRefreshLayout.isRefreshing = false
                    tvMessage.visibility = View.VISIBLE
                    tvMessage.setText(R.string.no_position_found)
                }
                Status.FAILED -> {
                    swipeRefreshLayout.isRefreshing = false
                    tvMessage.text = it.msg
                    tvMessage.visibility = View.VISIBLE
                }
            }
        })

        positionAdapter.onPositionViewClickListener = {view, pos ->
            positionAdapter.getItemAt(pos)?.also {
                val args = bundleOf("id" to it.id)
                view.findNavController().navigate(R.id.action_jobPositionsFragment_to_jobPositionDetailFragment, args)
            }
        }

        viewModel?.search?.value = JobPositionSearch()

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
                val ft = fragmentManager?.beginTransaction()
                val prev = fragmentManager?.findFragmentByTag(FILTER_DIALOG_TAG)
                if (prev != null) {
                    ft?.remove(prev)
                }
                ft?.addToBackStack(null)
                val dialogFragment = JobPositionsFilterFragment()
                dialogFragment.show(ft, FILTER_DIALOG_TAG)
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

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel?.search()
        }

        (activity as MainActivity?)?.toolbarTitle?.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.toolbarTitle?.setOnClickListener(null)
    }

}