package com.phyohtet.githubjobs.ui.position

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.phyohtet.githubjobs.ServiceLocator
import com.phyohtet.githubjobs.databinding.JobPositionsFilterBinding
import kotlinx.android.synthetic.main.fragment_filter_job_positions.*

class JobPositionsFilterFragment : DialogFragment() {

    private var viewModel: JobPositionsViewModel? = null

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = JobPositionsFilterBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSearch.setOnClickListener {
            viewModel?.search()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}