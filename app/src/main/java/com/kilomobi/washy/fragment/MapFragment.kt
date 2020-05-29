package com.kilomobi.washy.fragment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kilomobi.washy.activity.MapListener
import com.kilomobi.washy.R

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var mapListener: MapListener

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        try {
            mapListener = context as MapListener
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Try to obtain the map from the SupportMapFragment.
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.listMap, mapFragment).commit()

        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Add the user's mark (blue dot)
        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                mockPlaces()
//                setInfoBubble()
            }
        }
    }

//    private fun setInfoBubble() {
//        map.setInfoWindowAdapter(object : InfoWindowAdapter {
//            override fun getInfoWindow(marker: Marker): View? {
//                return null
//            }
//
//            override fun getInfoContents(marker: Marker): View {
//                val ctx: Context = requireActivity()
//
//                val inflater = LayoutInflater.from(ctx)
//                val view = inflater.inflate(R.layout.marker_info_layout, null, false)
//                view.header.text = marker.title
//                view.subheader.text = marker.snippet
//                view.text.text = getString(R.string.mockup_data_lorem_ipsum)
//
//                return view
//            }
//        })
//    }

    private fun mockPlaces() {
        val myPlace1 = LatLng(48.605, 7.73945)
        map.addMarker(MarkerOptions()
            .position(myPlace1)
            .snippet("1"))

        val myPlace2 = LatLng(48.596748, 7.727842)
        map.addMarker(MarkerOptions()
            .position(myPlace2)
            .snippet("2"))

        val myPlace3 = LatLng(48.593612, 7.749664)
        map.addMarker(MarkerOptions()
            .position(myPlace3)
            .snippet("3"))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        mapListener.notifyViewPagerChange(p0!!.snippet.toInt())
        return true
    }
}
