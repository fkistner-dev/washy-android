package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.viewmodel.MerchantViewModel
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class AddRatingFragment : BaseEmptyFragment() {

    private lateinit var tvRatingHeader: TextView
    private lateinit var inputRatingBar: MaterialRatingBar
    private lateinit var inputRatingText: TextInputLayout
    private lateinit var validateButton: MaterialButton
    private lateinit var merchant: Merchant

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
            merchant = requireArguments()["merchant"] as Merchant
        }

        val view = inflater.inflate(R.layout.add_rating_layout, container, false)

        tvRatingHeader = view.findViewById(R.id.add_rating_header)
        inputRatingBar = view.findViewById(R.id.ratingBar)
        inputRatingText = view.findViewById(R.id.input_rating)
        validateButton = view.findViewById(R.id.validate)

        validateButton.setOnClickListener {
            checkFields()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvRatingHeader.text = getString(R.string.add_rating_header, merchant.name)
    }

    private fun checkFields() {
        val merchant = Merchant()
        var isValid = true

        if (inputRatingText.editText?.text?.isNotBlank() == true) {
            if (inputRatingText.editText?.text!!.count() < 5) {
                isValid = false
                inputRatingText.error = getString(R.string.input_error_generic_length_requirement)
            } else {
                inputRatingText.error = null
            }
        } else {
            isValid = false
            inputRatingText.error = getString(R.string.input_error_name)
        }

        // TODO Add Rating to DB
        if (isValid) {
            val viewModel = MerchantViewModel()

            //viewModel.addRating(merchant)

            Snackbar.make(requireView(), getString(R.string.input_merchant_create_success), Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_becomeWasherFragment_to_homeFragment)
        }
    }
}