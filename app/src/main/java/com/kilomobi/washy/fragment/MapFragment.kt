package com.kilomobi.washy.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import kotlinx.android.synthetic.main.row_merchant_item.*
import kotlinx.android.synthetic.main.row_merchant_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MapFragment : FragmentEmptyView(R.layout.fragment_map_cardview), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var animateFromArgs = false
    private lateinit var mainActivityDelegate: MainActivityDelegate
    private var viewModel: MerchantListViewModel? = null
    private var enablePosition = false
    private var merchantList: List<Merchant> = listOf()
    private var previousMarker: Marker? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setHasOptionsMenu(true)
        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            // Try to obtain the map from the SupportMapFragment.
            val mapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction().replace(R.id.listMap, mapFragment).commit()

            mapFragment.getMapAsync(this)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            viewIsCreated = true
        }
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
        map.uiSettings.isMyLocationButtonEnabled = false
        map.uiSettings.isZoomControlsEnabled = false

        map.setOnMarkerClickListener(this)
        setUpMap()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (viewModel == null)
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MerchantListViewModel::class.java]

        if (hasPermission()) {
            // Add the user's mark (blue dot)
            map.isMyLocationEnabled = true
            // Center to France
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(46.227638, 2.213749), 15f))

            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lastLocation = location
                    enablePosition = true
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    if (!animateFromArgs) map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            13f
                        )
                    )
                    setNearestMerchant(location)
                }
            }

            setMerchantOnMap()
            view!!.cardview.visibility = View.INVISIBLE
        }
    }

    private fun hasPermission(): Boolean {
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
            return false
        }
        return true
    }

    private fun setMerchantOnMap() {
        if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
            animateFromArgs = true

            val merchant = requireArguments()["merchant"] as Merchant
            val merchantPosition =
                LatLng(merchant.position!!.latitude, merchant.position!!.longitude)

            map.addMarker(
                MarkerOptions()
                    .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker))
                    .position(merchantPosition)
                    .snippet(merchant.reference)
            )

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(merchantPosition, 16f))
        }
    }

    private fun setNearestMerchant(location: Location) {
        if (merchantList.isNotEmpty()) {
            onListReceived(merchantList)
        } else {
            viewModel!!.getNearestMerchant(location.latitude, location.longitude).observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    onListReceived(it)
                } else {
                    displayEmptyView()
                }
            }
        }
    }

    private fun onListReceived(merchantList: List<Merchant>) {
        this.merchantList = merchantList

        for (merchant in merchantList) {
            val merchantPosition =
                LatLng(merchant.position!!.latitude, merchant.position!!.longitude)
            map.addMarker(
                MarkerOptions()
                    .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker))
                    .position(merchantPosition)
                    .snippet(merchant.reference)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_geolocalisation -> {
                if (enablePosition) {
                    val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                } else {
                    Snackbar.make(requireView(), R.string.position_not_ready, Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val context: Context = requireActivity()
        val inflater = LayoutInflater.from(context)

        // Manage marker visibility
        previousMarker?.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker))
        marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker_highlight))

        val cardview: View = if (view?.cardview == null) {
            inflater.inflate(R.layout.row_merchant_item, null, false)
        } else {
            view!!.cardview
        }

        val merchant = merchantList.find { it.reference == marker.snippet }
        cardview.visibility = View.VISIBLE
        cardview.header.text = merchant?.name
        cardview.subheader.text = merchant?.fullAddress
        cardview.text.text = merchant?.description
        val currentLatLng = GeoPoint(lastLocation.latitude, lastLocation.longitude)
        val merchantLatLng = merchant?.position

        if (merchantLatLng != null)
            cardview.distance.text = String.format(distance(currentLatLng, merchantLatLng) + " km")

        merchant?.avgRating?.let { cardview.ratingBar.rating = it }

        cardview.setOnClickListener {
            if (merchant != null) {
                val bundle = bundleOf("merchant" to merchant)
                findNavController().navigate(R.id.action_mapFragment_to_merchantDetailFragment, bundle)
            } else {
                marker.remove()
                Snackbar.make(requireView(), getString(R.string.error_merchant_marker), Snackbar.LENGTH_SHORT).show()
            }
        }

        previousMarker = marker
        handleServices(merchant)

        return true
    }

    private fun handleServices(merchant: Merchant?) {
        if (merchant != null && merchant.services.isNotEmpty()) {
            val chipGroup = cardview.findViewById(R.id.chip_group) as ChipGroup
            val llService = cardview.findViewById(R.id.ll_service_holder) as LinearLayout
            llService.visibility = View.VISIBLE
            chipGroup.removeAllViews()

            for (service in merchant.services) {
                if (service.isNotBlank()) {
                    val chip = Chip(context)
                    chip.setEnsureMinTouchTargetSize(false)
                    chip.chipIcon =
                        Service.retrieveImage(service)
                            ?.let { ContextCompat.getDrawable(requireContext(), it) }
                    chip.chipStartPadding = 0f
                    chip.chipEndPadding = 0f
                    chip.textEndPadding = 0f
                    chip.closeIconEndPadding = 0f
                    chip.closeIconStartPadding = 0f
                    chip.textStartPadding = 0f
                    chip.iconEndPadding = 0f
                    chip.iconStartPadding = 0f
                    chip.setPadding(0, 0, 0, 0)
                    chip.setChipIconSizeResource(R.dimen.text_cardview_chip_size)
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.setChipIconTintResource(R.color.colorPrimary)

                    chipGroup.addView(chip)
                }
            }
        }
    }

    private fun distance(p1: GeoPoint, p2: GeoPoint): String {
        val theta = p1.longitude - p2.longitude
        var dist = (sin(deg2rad(p1.latitude))
                * sin(deg2rad(p2.latitude))
                + (cos(deg2rad(p1.latitude))
                * cos(deg2rad(p2.latitude))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist /= 0.62137

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        return df.format(dist)
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
