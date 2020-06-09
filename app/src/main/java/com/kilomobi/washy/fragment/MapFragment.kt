package com.kilomobi.washy.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import kotlinx.android.synthetic.main.marker_info_layout.view.*

class MapFragment : FragmentEmptyView(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var animateFromArgs = false
    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var viewModel: MerchantListViewModel

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        setHasOptionsMenu(true)

        try {
            mainActivityDelegate = context as MainActivityDelegate
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
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
                if (!animateFromArgs) map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        12f
                    )
                )
            }
        }

        setMerchantOnMap()
    }

    private fun setMerchantOnMap() {
        setInfoBubble()

        if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
            animateFromArgs = true

            val merchant = requireArguments()["merchant"] as Merchant
            val merchantPosition =
                LatLng(merchant.position!!.latitude, merchant.position!!.longitude)

            map.addMarker(
                MarkerOptions()
                    .position(merchantPosition)
                    .snippet((merchant.reference as DocumentReference).id)
            )


            map.animateCamera(CameraUpdateFactory.newLatLngZoom(merchantPosition, 16f))
        } else {
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                MerchantListViewModel::class.java
            )

            viewModel.getAllMerchants().observe(viewLifecycleOwner, Observer<List<Merchant>> {
                if (it != null && it.isNotEmpty()) {
                    onListReceived(it)
                } else {
                    displayEmptyView()
                }
            })
        }
    }

    private fun onListReceived(merchantList: List<Merchant>) {
        for (merchant in merchantList) {
            val merchantPosition =
                LatLng(merchant.position!!.latitude, merchant.position!!.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(merchantPosition)
                    .snippet((merchant.reference as DocumentReference).id)
            )
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    private fun setInfoBubble() {
        map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val ctx: Context = requireActivity()

                val inflater = LayoutInflater.from(ctx)
                val view = inflater.inflate(R.layout.marker_info_layout, null, false)

                val merchant = viewModel.getAllMerchants().value?.find { (it.reference as DocumentReference).id == marker.snippet }
                Glide.with(requireContext()).asBitmap()
                    .load(merchant?.imgUrl)
                    .override(50,50)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if(dataSource != null && !dataSource.equals(DataSource.MEMORY_CACHE))
                                marker.showInfoWindow()
                            return false
                        }

                    }).into(view.image)
                view.header.text = merchant?.name
                view.subheader.text = merchant?.fullAddress
                view.text.text = merchant?.description

                return view
            }
        })

        // Set a listener for info window events.
        map.setOnInfoWindowClickListener { marker ->
            val merchant = viewModel.getAllMerchants().value?.find { (it.reference as DocumentReference).id == marker.snippet }
            val bundle = bundleOf("merchant" to merchant)
            findNavController().navigate(R.id.action_mapFragment_to_merchantDetailFragment, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_geolocalisation -> {
                val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
