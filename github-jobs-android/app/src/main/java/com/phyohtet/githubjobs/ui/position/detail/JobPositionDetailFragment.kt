package com.phyohtet.githubjobs.ui.position.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.phyohtet.githubjobs.databinding.JobPositionDetailBinding
import com.phyohtet.githubjobs.model.DataSource
import kotlinx.android.synthetic.main.fragment_job_position_detail.*

class JobPositionDetailFragment : Fragment() {

    private lateinit var viewModel: JobPositionDetailViewModel
    private lateinit var binding: JobPositionDetailBinding
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(JobPositionDetailViewModel::class.java)

        id = arguments?.getString("id")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = JobPositionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCompanyUrl.setOnClickListener {
            val tv = it as TextView
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tv.text.toString()))
            startActivity(intent)
        }

        tvDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.position.observe(this, Observer {

            when (it?.status) {
                DataSource.Status.LOADING -> {
                }
                DataSource.Status.ERROR -> {
                }
                DataSource.Status.SUCCESS -> {
                    binding.dto = it.data
                    binding.executePendingBindings()
                    layoutProgress.visibility = View.GONE
                }
            }

        })

        viewModel.positionId.value = id

    }

}