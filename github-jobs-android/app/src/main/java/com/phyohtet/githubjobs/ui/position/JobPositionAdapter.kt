package com.phyohtet.githubjobs.ui.position

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.phyohtet.githubjobs.BR
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.NetworkState
import com.phyohtet.githubjobs.model.Status
import com.phyohtet.githubjobs.model.dto.JobPositionDTO

class JobPositionAdapter(private val retryCallBack: () -> Unit) : PagedListAdapter<JobPositionDTO, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<JobPositionDTO>() {
            override fun areItemsTheSame(oldItem: JobPositionDTO, newItem: JobPositionDTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: JobPositionDTO, newItem: JobPositionDTO): Boolean {
                return oldItem == newItem
            }
        }
    }

    var onPositionViewClickListener: ((View, Int) -> Unit)? = null

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.layout_load_more -> NetworkStateViewHolder(inflater.inflate(R.layout.layout_load_more, parent, false), retryCallBack)
            R.layout.layout_job_position -> JobPositionViewHolder(DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_job_position, parent, false))
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.layout_job_position -> (holder as JobPositionViewHolder).bind(getItem(position))
            R.layout.layout_load_more -> (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.layout_load_more
        } else {
            R.layout.layout_job_position
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun getItemAt(position: Int) = getItem(position)

    fun setNetworkState(newNetworkState: NetworkState) {
        val prevState = networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && prevState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    inner class JobPositionViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onPositionViewClickListener?.invoke(it, adapterPosition)
            }
        }

        fun bind(dto: JobPositionDTO?) {
            dto?.also {
                binding.setVariable(BR.dto, it)
                binding.executePendingBindings()
            }
        }
    }

    inner class NetworkStateViewHolder(view: View, private val retryCallBack: () -> Unit) : RecyclerView.ViewHolder(view) {

        val progress = view.findViewById<ProgressBar>(R.id.progressLoadMore)
        val retry = view.findViewById<Button>(R.id.btnRetry)

        init {
            retry.setOnClickListener {
                retryCallBack()
            }
        }

        fun bind(networkState: NetworkState?) {
            when (networkState?.status) {
                Status.LOADING -> progress.visibility = View.VISIBLE
                Status.FAILED -> retry.visibility = View.VISIBLE
                else -> {

                }
            }
        }
    }

}