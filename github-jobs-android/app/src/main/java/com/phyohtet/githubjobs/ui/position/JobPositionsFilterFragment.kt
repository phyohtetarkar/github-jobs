package com.phyohtet.githubjobs.ui.position

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.phyohtet.githubjobs.databinding.JobPositionsFilterBinding
import kotlinx.android.synthetic.main.fragment_filter_job_positions.*

class JobPositionsFilterFragment : DialogFragment() {

    private lateinit var viewModel: JobPositionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.also {
            viewModel = ViewModelProviders.of(it).get(JobPositionsViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = JobPositionsFilterBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSearch.setOnClickListener {
            viewModel.find()
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}