package com.example.android.politicalpreparedness.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.android.politicalpreparedness.election.TAG
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest


const val REQUEST_LOCATION_PERMISSION = 1
const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
@TargetApi(29)
fun isPermissionGranted(context:Context): Boolean {
    return (
            PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) && PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
}

fun checkDeviceLocationSettings(
    activity: Activity,
    myfunc: () -> Unit ,
    resolve: Boolean = true) {
    val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_LOW_POWER
    }
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val settingsClient = LocationServices.getSettingsClient(activity)
    val locationSettingsResponseTask =
        settingsClient.checkLocationSettings(builder.build())
    locationSettingsResponseTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException && resolve) {
            try {
                exception.startResolutionForResult(
                    activity,
                    REQUEST_TURN_DEVICE_LOCATION_ON
                )
            } catch (sendEx: IntentSender.SendIntentException) {
                Log.d(TAG, "Error getting location settings resolution: " + sendEx.message)
            }
        } else {
            Log.d("checkDeviceLocationSettings", "Device location is off, required to turn on")
//            showSnackbar("Device location is OFF, turn it on to get voter detailed data")
        }
    }
    locationSettingsResponseTask.addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("checkDeviceLocationSettings", "Succeeded Device location is ON")
            myfunc()
        }
    }
}
