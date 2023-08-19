package com.kilomobi.washy.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.databinding.FragmentMapCardviewBinding
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.util.awaitCurrentLocation
import com.kilomobi.washy.viewmodel.MerchantListViewModel
import kotlinx.coroutines.launch
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
    private lateinit var binding: FragmentMapCardviewBinding

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
            binding = FragmentMapCardviewBinding.bind(view)

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            // Try to obtain the map from the SupportMapFragment.
//            val mapFragment = SupportMapFragment.newInstance()
            val mapFragment = childFragmentManager.findFragmentById(binding.fragmentMapLayoutId.id) as SupportMapFragment
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

    @SuppressLint("MissingPermission")
    private fun onPermissionGranted() {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (LocationManagerCompat.isLocationEnabled(lm)) {
            // You can do this your own way, eg. from a viewModel
            // But here is where you wanna start the coroutine.
            // Choose your priority based on the permission you required
            val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            lifecycleScope.launch {
                val location = LocationServices
                    .getFusedLocationProviderClient(requireContext())
                    .awaitCurrentLocation(priority)
                // Do whatever with this location, notice that it's nullable
                // Add the user's mark (blue dot)
                map.isMyLocationEnabled = true
                // Center to France
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(46.227638, 2.213749), 15f))

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

                    setMerchantOnMap()
                    setNearestMerchant(location)
                }
            }
        } else {
            displayCustomTile(getString(R.string.perm_no_geolocation_title), getString(R.string.perm_no_geolocation_message), false)
        }
    }

    // Need to register this anywhere before onCreateView, ideally as a field
    private val permissionRequester = registerForActivityResult(
        // You can use RequestPermission() contract if you only need 1 permission
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        // If you requested 1 permission, change `map` to `isGranted`
        // Keys are permissions Strings, values are isGranted Booleans
        // An easy way to check if "any" permission was granted is map.containsValue(true)
        // You can use your own logic for multiple permissions,
        // but they have to follow the same checks here:
        val response = map.entries.first()
        val permission = response.key
        val isGranted = response.value
        when {
            isGranted -> {
                hidePermissionTile()
                onPermissionGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission) -> {
                // Permission denied but not permanently, telling user why we need it
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.perm_request_geolocation_title)
                    .setMessage(R.string.perm_request_geolocation_message)
                    .setPositiveButton(R.string.perm_request_geolocation_again) { _, _ ->
                        requirePermission()
                    }
                    .setNegativeButton(R.string.perm_request_geolocation_nop, null)
                    .create()
                    .show()
            }
            else -> {
                // Permission permanently denied
                displayCustomTile(getString(R.string.perm_request_geolocation_title), getString(R.string.perm_request_geolocation_message), true)
            }
        }
    }

    private fun requirePermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            // optional: Manifest.permission.ACCESS_COARSE_LOCATION
        )
        permissionRequester.launch(permissions)
    }

    private fun setUpMap() {
        if (viewModel == null)
            viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MerchantListViewModel::class.java]

        requirePermission()

        // Hide cardview
        binding.rowMerchantItemLayoutId.cardview.visibility = View.INVISIBLE
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
//        val context: Context = requireActivity()
//        val inflater = LayoutInflater.from(context)

        // Manage marker visibility
        previousMarker?.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker))
        marker.setIcon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_map_marker_highlight))

        val cardview: MaterialCardView = binding.rowMerchantItemLayoutId.cardview
        val merchant = merchantList.find { it.reference == marker.snippet }
        cardview.visibility = View.VISIBLE
        binding.rowMerchantItemLayoutId.header.text = merchant?.name
        binding.rowMerchantItemLayoutId.subheader.text = merchant?.fullAddress
        if (merchant?.description.isNullOrEmpty()) {
            binding.rowMerchantItemLayoutId.text.visibility = View.GONE
            binding.rowMerchantItemLayoutId.subtext.visibility = View.GONE
        }
        binding.rowMerchantItemLayoutId.text.text = merchant?.description
        val currentLatLng = GeoPoint(lastLocation.latitude, lastLocation.longitude)
        val merchantLatLng = merchant?.position

        if (merchantLatLng != null)
            binding.rowMerchantItemLayoutId.distance.text = String.format(distance(currentLatLng, merchantLatLng) + " km")

        merchant?.avgRating?.let { binding.rowMerchantItemLayoutId.ratingBar.rating = it }

        cardview.setOnClickListener {
            if (merchant != null) {
                val bundle = bundleOf("merchant" to merchant)
                currentView?.let { view -> navigate(view, R.id.action_mapFragment_to_merchantDetailFragment, bundle) }
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
            val chipGroup = binding.rowMerchantItemLayoutId.chipGroup
            binding.rowMerchantItemLayoutId.serviceHorizontalScroll.visibility = View.VISIBLE
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

    private fun displayCustomTile(title: String, message: String, isPermission: Boolean) {
        binding.rowMerchantItemLayoutId.cardview.visibility = View.VISIBLE
        binding.rowMerchantItemLayoutId.header.text = title
        binding.rowMerchantItemLayoutId.text.text = message

        binding.rowMerchantItemLayoutId.cardview.setOnClickListener {
            if (isPermission) {
                // Launch app's info screen
                // User need to manually set permission there
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.data = uri
                startForResult.launch(intent)
            } else {
                null
            }
        }
    }

    private fun hidePermissionTile() {
        binding.rowMerchantItemLayoutId.cardview.visibility = View.INVISIBLE
        binding.rowMerchantItemLayoutId.header.text = ""
        binding.rowMerchantItemLayoutId.text.text = ""
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            requirePermission()
        }
    }
}
