package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.viewmodel.MerchantViewModel
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class AddRatingFragment : FragmentEmptyView(R.layout.add_rating_layout) {

    private lateinit var tvRatingHeader: TextView
    private lateinit var inputRatingBar: MaterialRatingBar
    private lateinit var inputRatingText: TextInputLayout
    private lateinit var validateButton: MaterialButton
    private lateinit var merchant: Merchant
    private var userRating: Rating? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            if (arguments != null && requireArguments()["merchant"] != null && requireArguments()["merchant"] is Merchant) {
                merchant = requireArguments()["merchant"] as Merchant
                userRating = requireArguments()["rating"] as? Rating
            }

            tvRatingHeader = view.findViewById(R.id.add_rating_header)
            inputRatingBar = view.findViewById(R.id.ratingBar)
            inputRatingText = view.findViewById(R.id.input_rating)
            validateButton = view.findViewById(R.id.validate)

            validateButton.setOnClickListener {
                checkFields()
            }

            if (userRating != null) {
                tvRatingHeader.text = getString(R.string.add_rating_header, getString(R.string.modify_rating_action), merchant.name)
                inputRatingBar.rating = userRating!!.stars
                inputRatingText.editText?.setText(userRating!!.text)
                validateButton.setText(R.string.modify_rating_action)
            } else {
                tvRatingHeader.text = getString(R.string.add_rating_header, getString(R.string.add_rating_action), merchant.name)
            }
        }
    }

    private fun checkFields() {
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

        if (isValid) {
            val viewModel = MerchantViewModel()
            val user = FirebaseAuth.getInstance().currentUser

            val rating = Rating()
            rating.userId = user?.uid ?: ""
            rating.userName = user?.displayName ?: getString(R.string.common_user)
            rating.text = inputRatingText.editText?.text.toString()
            rating.createdAt = Timestamp.now()
            rating.stars = inputRatingBar.rating
            rating.language = "fr"
            if (userRating == null) {
                viewModel.addRating(merchant.reference!!, rating)
            } else {
                userRating?.let {
                    it.text = rating.text
                    it.stars = rating.stars
                    it.editedAt = Timestamp.now()
                    viewModel.modifyRating(merchant.reference!!, it)
                }
            }

            val snackText = if (userRating == null) getString(R.string.add_rating_action_publish) else getString(R.string.modify_rating_action_publish)
            Snackbar.make(requireView(), getString(R.string.input_rating_create_success, snackText), Snackbar.LENGTH_LONG).show()

            // Hack to avoid making a call to Firebase
            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set("rating", rating)
            navController.previousBackStackEntry?.savedStateHandle?.set("previousRating", userRating)
            navController.popBackStack()
        }
    }
}