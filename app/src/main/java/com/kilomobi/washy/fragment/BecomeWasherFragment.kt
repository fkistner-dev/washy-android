package com.kilomobi.washy.fragment

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.viewmodel.MerchantViewModel
import org.imperiumlabs.geofirestore.GeoLocation
import org.imperiumlabs.geofirestore.core.GeoHash
import java.util.*
import kotlin.collections.ArrayList

class BecomeWasherFragment : Fragment() {

    private lateinit var mainActivityDelegate: MainActivityDelegate
    private lateinit var inputName: TextInputLayout
    private lateinit var inputDescription: TextInputLayout
    private lateinit var inputPhone: TextInputLayout
    private lateinit var inputAddress: TextInputLayout
    private lateinit var inputCity: TextInputLayout
    private lateinit var inputSiren: TextInputLayout
    private lateinit var chipGroupServices: ChipGroup
    private lateinit var chipHeadlight: Chip
    private lateinit var chipInterior: Chip
    private lateinit var chipWindshield: Chip
    private lateinit var chipEcoFriendly: Chip
    private lateinit var chipCb: Chip
    private lateinit var chipCoffee: Chip
    private lateinit var chipWifi: Chip
    private lateinit var chipElectric: Chip
    private lateinit var chipTransport: Chip
    private lateinit var chipMotorcycle: Chip
    private lateinit var radioGroupWorkAtHome: RadioGroup
    private lateinit var radioGroupIsPro: RadioGroup
    private lateinit var radioProYes: RadioButton
    private lateinit var radioProNo: RadioButton
    private lateinit var validateButton: MaterialButton
    private var chipArrayList: ArrayList<Chip> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_become_washer, container, false)

        setHasOptionsMenu(true)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        inputName = view.findViewById<TextInputLayout>(R.id.input_name)
        inputDescription = view.findViewById<TextInputLayout>(R.id.input_description)
        inputPhone = view.findViewById<TextInputLayout>(R.id.input_phone)
        inputAddress = view.findViewById<TextInputLayout>(R.id.input_address)
        inputCity = view.findViewById<TextInputLayout>(R.id.input_city)
        inputSiren = view.findViewById<TextInputLayout>(R.id.input_siren)
        chipGroupServices = view.findViewById<ChipGroup>(R.id.input_chip_group)

        // Each Chip
        chipHeadlight = view.findViewById<Chip>(R.id.chip_headlight)
        chipInterior = view.findViewById<Chip>(R.id.chip_interior)
        chipWindshield = view.findViewById<Chip>(R.id.chip_windshield)
        chipEcoFriendly = view.findViewById<Chip>(R.id.chip_eco)
        chipCb = view.findViewById<Chip>(R.id.chip_cb)
        chipCoffee = view.findViewById<Chip>(R.id.chip_coffee)
        chipWifi = view.findViewById<Chip>(R.id.chip_wifi)
        chipElectric = view.findViewById<Chip>(R.id.chip_electric)
        chipTransport = view.findViewById<Chip>(R.id.chip_transport)
        chipMotorcycle = view.findViewById<Chip>(R.id.chip_motorbike)

        radioGroupWorkAtHome = view.findViewById<RadioGroup>(R.id.group_work_at_home)
        radioGroupIsPro = view.findViewById<RadioGroup>(R.id.group_is_pro)
        radioProYes = view.findViewById<RadioButton>(R.id.radio_pro_yes)
        radioProNo = view.findViewById<RadioButton>(R.id.radio_pro_no)

        validateButton = view.findViewById<MaterialButton>(R.id.validate)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initToolbar(toolbar, true)

        val chipListener = View.OnClickListener {
            val chipView: Chip = it as Chip

            if (chipArrayList.contains(chipView)) {
                chipView.setChipIconTintResource(R.color.colorGrey500)
                chipView.setChipStrokeColorResource(R.color.colorGrey500)
                chipArrayList.remove(chipView)
            } else {
                chipView.setChipIconTintResource(R.color.colorPrimary)
                chipView.setChipStrokeColorResource(R.color.colorPrimary)
                chipArrayList.add(chipView)
            }
        }

        chipHeadlight.setOnClickListener(chipListener)
        chipInterior.setOnClickListener(chipListener)
        chipWindshield.setOnClickListener(chipListener)
        chipWifi.setOnClickListener(chipListener)
        chipEcoFriendly.setOnClickListener(chipListener)
        chipCb.setOnClickListener(chipListener)
        chipCoffee.setOnClickListener(chipListener)
        chipElectric.setOnClickListener(chipListener)
        chipTransport.setOnClickListener(chipListener)
        chipMotorcycle.setOnClickListener(chipListener)

        val radioListener = CompoundButton.OnCheckedChangeListener { group, checkedId ->
            if (radioProYes.isChecked) {
                inputSiren.visibility = View.VISIBLE
            } else {
                inputSiren.visibility = View.GONE
            }
        }

        radioProYes.setOnCheckedChangeListener(radioListener)
        radioProNo.setOnCheckedChangeListener(radioListener)

        validateButton.setOnClickListener {
            checkFields()
        }
    }

    private fun checkFields() {
        val merchant = Merchant()
        var isValid = true
        var isAddressComplete = true

        if (inputName.editText?.text?.isNotBlank() == true) {
            if (inputName.editText?.text!!.count() < 5) {
                isValid = false
                inputName.error = getString(R.string.input_error_name_requirement)
            } else {
                inputName.error = null
                merchant.name = inputName.editText?.text.toString()
            }
        } else {
            isValid = false
            inputName.error = getString(R.string.input_error_name)
        }

        if (inputDescription.editText?.text?.isNotBlank() == true) {
            if (inputDescription.editText?.text!!.count() < 15) {
                isValid = false
                inputDescription.error = getString(R.string.input_error_description_requirement)
            } else {
                inputDescription.error = null
                merchant.description = inputDescription.editText?.text.toString()
            }
        } else {
            isValid = false
            inputDescription.error = getString(R.string.input_error_description)
        }

        if (inputPhone.editText?.text?.isNotBlank() == true) {
            if (inputPhone.editText?.text!!.count() != 10) {
                isValid = false
                inputPhone.error = getString(R.string.input_error_phone_requirement)
            } else {
                inputPhone.error = null
                merchant.phone = inputPhone.editText?.text.toString()
            }
        } else {
            isValid = false
            inputPhone.error = getString(R.string.input_error_phone)
        }

        if (inputAddress.editText?.text?.isNotBlank() == true) {
            if (inputAddress.editText?.text!!.count() < 6) {
                isValid = false
                isAddressComplete = false
                inputAddress.error = getString(R.string.input_error_address_requirement)
            } else {
                inputAddress.error = null
                merchant.fullAddress = inputAddress.editText?.text.toString()
            }
        } else {
            isValid = false
            isAddressComplete = false
            inputAddress.error = getString(R.string.input_error_address)
        }

        if (inputCity.editText?.text?.isNotBlank() == true) {
            inputCity.error = null
            merchant.fullAddress += " " +inputCity.editText?.text.toString()
        } else {
            isValid = false
            isAddressComplete = false
            inputCity.error = getString(R.string.input_error_city)
        }

        if (isAddressComplete) {
            merchant.position = getLatLongFromAddress(inputAddress.editText!!.text.toString() + " " + inputCity.editText!!.text.toString())
        }

        if (chipArrayList.isNullOrEmpty()) {
            Snackbar.make(requireView(), getString(R.string.input_error_no_service), Snackbar.LENGTH_LONG).show()
            isValid = false
        } else {
            merchant.services = Service.servicesToDatabase(requireContext(), chipArrayList)
        }

        if (radioGroupIsPro.checkedRadioButtonId == -1) {
            Snackbar.make(requireView(), getString(R.string.input_error_no_radio), Snackbar.LENGTH_LONG).show()
            isValid = false
        } else {
            if (radioGroupIsPro.checkedRadioButtonId == 0) {
                if (inputSiren.editText?.text?.isNotBlank() == true) {
                    inputSiren.error = null
                    merchant.siren = inputSiren.editText?.text.toString()
                } else {
                    isValid = false
                    inputSiren.error = getString(R.string.input_error_siren)
                }
            }
        }

        if (radioGroupWorkAtHome.checkedRadioButtonId == -1) {
            Snackbar.make(requireView(), getString(R.string.input_error_no_radio), Snackbar.LENGTH_LONG).show()
            isValid = false
        } else {
            merchant.workAtCustomer = radioGroupIsPro.checkedRadioButtonId == 0
        }

        val geoHash = GeoHash(GeoLocation(merchant.position!!.latitude, merchant.position!!.longitude))
        if (!TextUtils.isEmpty(geoHash.geoHashString)) {
            merchant.geohash = geoHash.geoHashString
        } else {
            Snackbar.make(requireView(), getString(R.string.input_error_no_geohash), Snackbar.LENGTH_LONG).show()
        }
        
        if (isValid) {
            val viewModel = MerchantViewModel()

            merchant.active = true
            FirebaseAuth.getInstance().uid.let {
                if (it != null) {
                    merchant.adminId = it
                }
            }

            viewModel.addMerchant(merchant)

            Snackbar.make(requireView(), getString(R.string.input_merchant_create_success), Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_becomeWasherFragment_to_homeFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().popBackStack()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getLatLongFromAddress(address: String) : GeoPoint? {
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses: List<Address> =
                geoCoder.getFromLocationName(address, 1)
            if (addresses.isNotEmpty()) {
                return GeoPoint(addresses[0].latitude, addresses[0].longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return GeoPoint(0.0,0.0)
    }
}