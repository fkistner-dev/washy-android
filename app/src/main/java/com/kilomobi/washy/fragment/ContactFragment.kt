/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.util.convertToAddress

class ContactFragment(val merchant: Merchant? = null) : FragmentEmptyView(R.layout.fragment_contact_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            fillView(view)
            viewIsCreated = true
        }
    }

    private fun fillView(view: View) {
        if (merchant?.phone.isNullOrEmpty()) {
            view.findViewById<RelativeLayout>(R.id.phoneRl).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.phoneText).text = getString(R.string.phone_text_placeholder, merchant?.phone)
        }

        if (merchant?.workAtCustomer == true) {
            view.findViewById<RelativeLayout>(R.id.addressRl).visibility = View.GONE
        } else {
            view.findViewById<TextView>(R.id.mapText).text =
                if (merchant?.fullAddress.isNullOrEmpty())
                    getString(R.string.address_text_placeholder, merchant?.position?.convertToAddress(view.context))
                else
                    getString(R.string.address_text_placeholder, merchant?.fullAddress)
            view.findViewById<Button>(R.id.map).setOnClickListener {
                val merchantPosition = LatLng(merchant?.position!!.latitude, merchant.position!!.longitude)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+merchantPosition.latitude+","+merchantPosition.longitude+"?q="+merchantPosition.latitude+","+merchantPosition.longitude+"("+merchant.name+")"))
                startActivity(intent)
            }
        }

        view.findViewById<Button>(R.id.phone).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", merchant?.phone, null))
            startActivity(intent)
        }
    }
}