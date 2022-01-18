package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter

const val TAG = "ElectionFragment"

class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionBinding
    private lateinit var upcomingAdapter: ElectionListAdapter
    private lateinit var savedAdapter: ElectionListAdapter

    //TODO: Declare ViewModel
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Add ViewModel values and create ViewModel

        // Add binding values
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)


        //TODO: Link elections to voter info

        //Initiate recycler adapters
        upcomingAdapter = ElectionListAdapter(ElectionListener { election ->
            Log.i(TAG, "Upcoming election with ID: {${election.id}} selected")
        })
        binding.upcomingElementRview.adapter = upcomingAdapter

        savedAdapter = ElectionListAdapter(ElectionListener { election ->
            Log.i(TAG, "saved election with ID: {${election.id}} selected")
        })
        binding.savedElementRview.adapter = savedAdapter




        //TODO: Populate recycler adapters

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}