package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionElementBinding
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

class ElectionListAdapter(private val clickListener: ElectionListener):
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    private val elections = arrayListOf(Election(id=1, name="hideki", electionDay = Date(0), division = Division("a", "b", "c")))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    // Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election:Election = elections[position]
        holder.bind(clickListener, election)
    }

    override fun getItemCount(): Int {
        return elections.size
    }

    // Create ElectionViewHolder
    class ElectionViewHolder private  constructor(private val electionBinding: ElectionElementBinding) :RecyclerView.ViewHolder(electionBinding.root){
        fun bind(
            clickListener: ElectionListener,
            currentElection: Election
        ){
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
class ElectionDiffCallback : DiffUtil.ItemCallback<Election>(){
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}
// Create ElectionListener

class ElectionListener(val clickListener: (election: Election) -> Unit){
    fun onClick(election: Election) = clickListener(election)
}
