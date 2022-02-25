package com.example.android.politicalpreparedness.election

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.checkDeviceLocationSettings
import com.example.android.politicalpreparedness.utils.isPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: VoterInfoViewModel by viewModel()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.d("locationPermissionRequest", "Precise location access granted.")
                getLastDeviceLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
                Log.w("locationPermissionRequest", "Warning No location access granted.")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Add binding values
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_voter_info,
            container,
            false
        )
        binding.lifecycleOwner = this

        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        Log.d(
            "VoterInfoFragment",
            "Received election id:${args.argElectionId}, division:${args.argDivision}"
        )

        // Add ViewModel values and create ViewModel
        binding.viewModel = viewModel

        // Handle loading of URLs
        binding.stateBallot.setOnClickListener {
            if (viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl != null) {
                viewModel.voterInfo.value!!.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl?.let { url ->
                    loadURLIntents(url)
                }
            } else {
                Log.d("VoterInfoFragment", "Able to get state ballot url")
            }
        }

        binding.stateLocations.setOnClickListener {
            if (viewModel.voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl != null) {
                Log.d("VoterInfoFragment", "Able to get state voting location url")
                viewModel.voterInfo.value!!.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl?.let { url ->
                    loadURLIntents(url)
                }
            } else {
                Log.d("VoterInfoFragment", "Unable to get voting location url")
            }
        }
        viewModel.getElectionMainInfo(args.argElectionId)


        // Handle save button UI state
        // cont'd Handle save button clicks
        binding.saveElectionButton.setOnClickListener {
            viewModel.election.value?.saved.let {
                if (it == true) {
                    viewModel.unfollowElection()
                } else {
                    viewModel.followElection()
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return (binding.root)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        ////    To display the system permissions dialog when necessary, call the launch() method on the instance of ActivityResultLauncher that you saved in the previous step.
        //// Before you perform the actual permission request, check whether your app
        //// already has the permissions, and whether your app needs to show a permission
        //// rationale dialog. For more details, see Request permissions.
        checkDeviceLocationSettings(requireActivity(), { getLastDeviceLocation() })
        if (isPermissionGranted(requireContext())) {
            Log.d("onCreateView", "Permissions granted")
            getLastDeviceLocation()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation():Unit{
        var address: Address? = null
        fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    try {
                        address =
                            Geocoder(requireContext()).getFromLocation(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude,
                                1
                            )
                                .firstOrNull()
                        Log.d("getLastDeviceLocation", "Address is: $address")
                        setVoterInfo(address)
                    } catch (ex: Exception) {
                        Log.d("getLastDeviceLocation", "Failed to get Address from location")
                    }
                }
            } else {
                Log.d("getLastDeviceLocation", "Task failed, Failed to get Address from location")
            }
        }
    }

    private fun setVoterInfo(address: Address?) {
        if (address != null) {
            viewModel.getVoterInfo(address)
        } else {
            Log.d(
                "VoterInfoFragment",
                getString(R.string.error_failed_to_get_address_from_location)
            )
            showSnackbar(getString(R.string.error_failed_to_get_address_from_location))

        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    // Create method to load URL intents
    private fun loadURLIntents(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
