/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.kilomobi.washy.R
import com.kilomobi.washy.fragment.RatingListFragment.Companion.STACK_CURRENT_RATING
import com.kilomobi.washy.fragment.RatingListFragment.Companion.STACK_IS_DELETE_RATING
import com.kilomobi.washy.fragment.RatingListFragment.Companion.STACK_PREVIOUS_RATING
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Rating
import com.kilomobi.washy.util.WashyAuth
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
                val menuHost: MenuHost = requireActivity()
                menuHost.addMenuProvider(object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        // Add menu items here
                        menuInflater.inflate(R.menu.menu_delete_rating, menu)
                        menu.findItem(R.id.action_delete_rating).isVisible = true
                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                        // Handle the menu selection
                        return when (menuItem.itemId) {
                            R.id.action_delete_rating -> {
                                // Show confirm dialog
                                deleteDialog()
                                true
                            }
                            else -> false
                        }
                    }
                }, viewLifecycleOwner, Lifecycle.State.RESUMED)
                tvRatingHeader.text = getString(R.string.add_rating_header, getString(R.string.modify_rating_action), merchant.name)
                inputRatingBar.rating = userRating!!.stars
                inputRatingText.editText?.setText(userRating!!.text)
                validateButton.setText(R.string.modify_rating_action)
            } else {
                tvRatingHeader.text = getString(R.string.add_rating_header, getString(R.string.add_rating_action), merchant.name)
            }
        }
    }

    private fun deleteDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.delete_rating_title))
        builder.setMessage(resources.getString(R.string.delete_rating_text))
        builder.setPositiveButton(resources.getString(R.string.common_yes)) { _, _ ->
            userRating?.let {
                MerchantViewModel().deleteRating(merchant.reference!!, it)
            }
            finishRating(null)

            Snackbar.make(requireView(), getString(R.string.input_rating_manage_success, getString(R.string.delete_rating_action_publish)), Snackbar.LENGTH_LONG).show()
        }
        builder.setNegativeButton(resources.getString(R.string.common_no), null)
        builder.show()
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
            val user = WashyAuth.getUid()

            val rating = Rating()
            rating.userId = user ?: ""
            rating.userName = getString(R.string.common_user)
            rating.text = inputRatingText.editText?.text.toString()
            rating.createdAt = Timestamp.now()
            rating.stars = inputRatingBar.rating
            rating.language = "fr"
            if (userRating == null) {
                viewModel.addRating(merchant.reference!!, rating)
                finishRating(rating)
            } else {
                userRating?.let {
                    it.text = rating.text
                    it.stars = rating.stars
                    it.editedAt = Timestamp.now()
                    viewModel.modifyRating(merchant.reference!!, it)
                    finishRating(it)
                }
            }

            val snackText = if (userRating == null) getString(R.string.add_rating_action_publish) else getString(R.string.modify_rating_action_publish)
            Snackbar.make(requireView(), getString(R.string.input_rating_manage_success, snackText), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun finishRating(rating: Rating?) {
        // Hack to avoid making a call to Firebase
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(STACK_CURRENT_RATING, rating)
        navController.previousBackStackEntry?.savedStateHandle?.set(STACK_PREVIOUS_RATING, userRating)
        navController.previousBackStackEntry?.savedStateHandle?.set(STACK_IS_DELETE_RATING, rating == null)
        navController.popBackStack()
    }
}