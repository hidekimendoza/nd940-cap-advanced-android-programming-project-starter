package com.example.android.politicalpreparedness.election.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionElementBinding
import com.example.android.politicalpreparedness.election.TAG
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

class ElectionListAdapter(private val clickListener: ElectionListener):
    ListAdapter<ElectionDomainModel, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election:ElectionDomainModel = getItem(position)
        holder.bind(clickListener, election)
    }

    // Create ElectionViewHolder
    class ElectionViewHolder private  constructor(private val electionBinding: ElectionElementBinding) :RecyclerView.ViewHolder(electionBinding.root){
        fun bind(
            clickListener: ElectionListener,
            currentElection: ElectionDomainModel
        ){
            Log.d(TAG, "Binding election $currentElection")
            electionBinding.election = currentElection
            electionBinding.executePendingBindings()
            electionBinding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                // Inflate layout for view holder
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ElectionElementBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }
}

// Create ElectionDiffCallback
class ElectionDiffCallback : DiffUtil.ItemCallback<ElectionDomainModel>(){
    override fun areItemsTheSame(oldItem: ElectionDomainModel, newItem: ElectionDomainModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ElectionDomainModel, newItem: ElectionDomainModel): Boolean {
        return oldItem == newItem
    }

}
// Create ElectionListener
class ElectionListener(val clickListener: (election: ElectionDomainModel) -> Unit){
    fun onClick(election: ElectionDomainModel) = clickListener(election)
}
