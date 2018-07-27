package com.phyohtet.githubjobs.ui.position

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phyohtet.githubjobs.BR
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.dto.JobPositionDTO

class JobPositionAdapter : RecyclerView.Adapter<JobPositionAdapter.JobPositionViewHolder>() {

    var list = mutableListOf<JobPositionDTO?>()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] != null) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPositionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> JobPositionViewHolder(inflater.inflate(R.layout.layout_load_more, parent, false))
            else -> JobPositionViewHolder(DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.layout_job_position, parent, false))
        }
    }

    override fun onBindViewHolder(holder: JobPositionViewHolder, position: Int) {
        list[position]?.also {
            holder.bind(it)
        }

    }

    fun submitList(list: List<JobPositionDTO>?) {
        list?.also {
            this.list = it.toMutableList()
            notifyDataSetChanged()
        }
    }

    fun appendList(list: List<JobPositionDTO>?) {
        list?.also {
            val offset = itemCount
            this.list.addAll(offset, it)
            notifyItemRangeInserted(offset, it.size)
        }
    }

    fun append(dto: JobPositionDTO?) {
        val offset = itemCount
        this.list.add(offset, dto)
        notifyItemInserted(offset)
    }

    fun remove(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class JobPositionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var binding: ViewDataBinding? = null

        constructor(binding: ViewDataBinding) : this(binding.root) {
            this.binding = binding
        }

        fun bind(dto: JobPositionDTO) {
            binding?.setVariable(BR.dto, dto)
            binding?.executePendingBindings()
        }

    }

}