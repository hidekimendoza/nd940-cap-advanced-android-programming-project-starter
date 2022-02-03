package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import org.koin.androidx.viewmodel.ext.android.viewModel


const val TAG = "ElectionFragment"

class ElectionsFragment: Fragment() {

    private lateinit var binding: FragmentElectionBinding
    private lateinit var upcomingAdapter: ElectionListAdapter
    private lateinit var savedAdapter: ElectionListAdapter

    //Declare ViewModel
    private val viewModel: ElectionsViewModel by viewModel()
//    private val viewModel: ElectionsViewModel by lazy {ViewModelProvider(this, ElectionsViewModelFactory(requireActivity().application, electionRepository)).get(
//        ElectionsViewModel::class.java)
//    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Add binding values
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false)

        binding.viewModel = viewModel

        // Initiate recycler adapters
        upcomingAdapter = ElectionListAdapter(
            ElectionListener { election ->
                Log.i(TAG, "Upcoming election with ID: {${election.id}} selected")
            })


        savedAdapter = ElectionListAdapter(ElectionListener { election ->
            Log.i(TAG, "saved election with ID: {${election.id}} selected")
        })


        // Link elections to voter info

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer { electionList ->
            upcomingAdapter.submitList(electionList)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer { electionList ->
            upcomingAdapter.submitList(electionList)
        })

        // Add ViewModel values and create ViewModel

        // Populate recycler adapters
        binding.upcomingElementRview.adapter = upcomingAdapter
        binding.savedElementRview.adapter = savedAdapter

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads
}