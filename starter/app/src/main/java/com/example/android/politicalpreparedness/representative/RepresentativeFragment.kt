package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.checkDeviceLocationSettings
import com.example.android.politicalpreparedness.utils.isPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailFragment : Fragment() {

    // Declare ViewModel
    private val viewModel: RepresentativeViewModel by viewModel()
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                Log.d("locationPermissionRequest", "Precise location access granted.")
                getDeviceAddress()
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

        // Establish bindings
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.setState(requireContext().resources.getStringArray(R.array.states)[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            checkDeviceLocationSettings(requireActivity(), { getDeviceAddress() })
            if (isPermissionGranted(requireContext())) {
                Log.d("onCreateView", "Permissions granted")
                getDeviceAddress()
            } else {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }

        viewModel.messageInput.observe(viewLifecycleOwner) {
            it?.let {
                Log.i("DetailFragment", "Missing address input data: ${getString(it)}")
                showSnackbar(getString(it))
            }
        }

        binding.buttonSearch.setOnClickListener {
            binding.executePendingBindings()
            viewModel.searchRepresentatives()
        }

        // Define and assign Representative adapter
        val adapter = RepresentativeListAdapter(RepresentativeListener { representative ->
            logRepresentativeClicked(representative)
        })

        viewModel.representatives.observe(viewLifecycleOwner, { representativeList ->
            adapter.submitList(representativeList)
        })

        // Populate Representative adapter
        binding.representativesList.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.executePendingBindings()
        return binding.root
    }

    private fun logRepresentativeClicked(representative: Representative) {
        Log.d("logRepresentativeClicked", "Clicked representative: $representative")
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceAddress(): Unit {
        fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    try {
                        val address = geoCodeLocation(lastKnownLocation)
                        Log.d("getLastDeviceLocation", "Address is: $address")
                        setAddressInfo(address)
                    } catch (ex: Exception) {
                        Log.d("getLastDeviceLocation", "Failed to get Address from location")
                    }
                }
            } else {
                Log.d("getLastDeviceLocation", "Task failed, Failed to get Address from location")
            }
        }
    }

    private fun setAddressInfo(address: Address?) {
        if (address != null) {
            viewModel.updateAddressFields(address)
            binding.executePendingBindings()
            viewModel.searchRepresentatives()
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

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}