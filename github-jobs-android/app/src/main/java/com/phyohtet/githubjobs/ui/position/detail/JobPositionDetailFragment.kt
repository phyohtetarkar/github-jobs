package com.phyohtet.githubjobs.ui.position.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.phyohtet.githubjobs.ServiceLocator
import com.phyohtet.githubjobs.databinding.JobPositionDetailBinding
import com.phyohtet.githubjobs.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_job_position_detail.*

class JobPositionDetailFragment : Fragment() {

    private var viewModel: JobPositionDetailViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = ServiceLocator.getInstance(context!!).getGitHubJobRepo()
                @Suppress("UNCHECKED_CAST")
                return JobPositionDetailViewModel(repo) as T
            }
        })[JobPositionDetailViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = JobPositionDetailBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                view?.findNavController()?.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCompanyUrl.setOnClickListener {
            val tv = it as TextView
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tv.text.toString()))
            startActivity(intent)
        }

        tvHowToApply.movementMethod = LinkMovementMethod.getInstance()
        tvDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = arguments?.getString("id")
        viewModel?.positionId?.value = id
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}