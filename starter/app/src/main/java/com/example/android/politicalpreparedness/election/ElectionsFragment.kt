package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


const val TAG = "ElectionFragment"

class ElectionsFragment : Fragment() {

    private lateinit var binding: FragmentElectionBinding
    private lateinit var upcomingAdapter: ElectionListAdapter
    private lateinit var savedAdapter: ElectionListAdapter

    //Declare ViewModel
    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Add binding values
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_election,
            container,
            false
        )

        // Add ViewModel values and create ViewModel
        binding.viewModel = viewModel

        // Initiate recycler adapters
        upcomingAdapter = ElectionListAdapter(
            ElectionListener { election ->
                navigateToSelectedElection(election)
            })


        savedAdapter = ElectionListAdapter(ElectionListener { election ->
            navigateToSelectedElection(election)
        })


        // Link elections to voter info
        viewModel.upcomingElections.observe(viewLifecycleOwner, { electionList ->
            upcomingAdapter.submitList(electionList)
        })

        viewModel.savedElections.observe(viewLifecycleOwner, { electionList ->
            savedAdapter.submitList(electionList)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("DetailFragment", "Missing address input data: ${getString(it)}")
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT47).show()
            }
        }

        // Populate recycler adapters
        binding.upcomingElementRview.adapter = upcomingAdapter
        binding.savedElementRview.adapter = savedAdapter
        return binding.root
    }

    private fun navigateToSelectedElection(election: ElectionDomainModel) {
        Log.i(
            TAG,
            "Upcoming election with ID: {${election.id}} selected with division ${election.division}"
        )
        this.findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment2(
                election.id,
                election.division
            )
        )
    }

    //TODO: Refresh adapters when fragment loads
}
