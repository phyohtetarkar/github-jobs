package com.phyohtet.githubjobs.ui.position

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.phyohtet.githubjobs.databinding.JobPositionDTOBinding
import com.phyohtet.githubjobs.model.dto.JobPositionDTO

class JobPositionAdapter : RecyclerView.Adapter<JobPositionAdapter.JobPositionViewHolder>() {

    var positions = mutableListOf<JobPositionDTO>()

    override fun getItemCount(): Int {
        return positions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPositionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobPositionDTOBinding.inflate(inflater, parent, false)
        return JobPositionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobPositionViewHolder, position: Int) {
        holder.bind(positions[position])
    }

    fun submitList(list: MutableList<JobPositionDTO>?) {
        list?.also {
            this.positions = it
            notifyDataSetChanged()
        }
    }

    fun appendList(list: MutableList<JobPositionDTO>?) {
        list?.also {
            val offset = itemCount
            this.positions.addAll(offset, it)
            notifyItemRangeChanged(offset, it.size)
        }
    }

    inner class JobPositionViewHolder(private val binding: JobPositionDTOBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dto: JobPositionDTO) {
            binding.dto = dto
        }

    }

}