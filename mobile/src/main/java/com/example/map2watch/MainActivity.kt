package com.example.map2watch

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.map2watch.utils.checkSelfPermissionCompat
import com.example.map2watch.utils.requestPermissionsCompat
import com.example.map2watch.utils.shouldShowRequestPermissionRationaleCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_main.*


const val PERMISSION_REQUEST_LOCATION = 1


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks {

    private lateinit var placesClient:PlacesClient
    private lateinit var  locationClient:GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGoogleAPI()
        grandPermission()
        layout


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for location permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                // todo
            } else {
                // Permission request was denied.
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun grandPermission() {
        // Check if the Location permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start map preview
            Snackbar.make(layout, getString(R.string.permission_granted), Snackbar.LENGTH_LONG).show();

        } else {
            // Permission is missing and must be requested.
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() =// Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar.make(layout, getString(R.string.requires_permission), Snackbar.LENGTH_LONG).show()
            requestPermissionsCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_REQUEST_LOCATION)
            } else {
            requestPermissionsCompat(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        }
    private fun initGoogleAPI() {
        // Initialize Places.
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        // Create a new Places client instance.
        placesClient = Places.createClient(this)

        // create location client
        locationClient = GoogleApiClient.Builder(this)
            .enableAutoManage(
                this /* FragmentActivity */,
                GoogleApiClient.OnConnectionFailedListener { connectionResult ->
                    Log.i("Connection failed", connectionResult.errorMessage)
                } /* OnConnectionFailedListener */
            )
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .build()
    }

    override fun onConnectionSuspended(p0: Int) {

        Log.i("Connection suspended", p0.toString())
    }

    override fun onConnected(p0: Bundle?) {

        Log.i("Connected", p0.toString())
    }

}
