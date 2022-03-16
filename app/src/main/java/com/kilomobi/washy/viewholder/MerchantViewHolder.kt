package com.kilomobi.washy.viewholder

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Merchant
import com.kilomobi.washy.model.Service
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MerchantViewHolder(private val merchantView: View) : RecyclerView.ViewHolder(merchantView) {

    val header: Int = R.id.header
    val title: Int = R.id.subheader
    val text: Int = R.id.text
    val rating: Int = R.id.ratingBar
    private val llService: Int = R.id.ll_service_holder
    private val cardview: Int = R.id.cardview

    fun bind(merchant: Merchant, selectedItem: Int) {
        val context = merchantView.context
        if (merchant.imported) {
            merchantView.findViewById<TextView>(header).visibility = View.GONE
        }
        merchantView.findViewById<TextView>(header).text = merchant.name
        merchantView.findViewById<TextView>(title).text = if (merchant.siren?.isNotEmpty() == true) context.getString(R.string.merchant_pro) else context.getString(R.string.merchant_part)
        if (merchant.description.isNullOrBlank()) {
            merchantView.findViewById<TextView>(text).visibility = View.GONE
        } else {
            merchantView.findViewById<TextView>(text).text = merchant.description
        }
        merchantView.findViewById<MaterialRatingBar>(rating).rating = merchant.avgRating

        merchantView.findViewById<MaterialCardView>(cardview).apply {
            strokeColor = if (selectedItem == adapterPosition) context.getColor(
                R.color.colorAccent
            ) else Color.WHITE
            strokeWidth = 3
        }

        if (merchant.services.isNotEmpty()) {
            merchantView.findViewById<LinearLayout>(llService).visibility = View.VISIBLE
            val chipGroup = merchantView.findViewById(R.id.chip_group) as ChipGroup

            for (service in merchant.services) {
                if (service.isBlank()) return
                val chip = Chip(context)
                chip.setEnsureMinTouchTargetSize(false)
                chip.chipIcon =
                    Service.retrieveImage(service)
                        ?.let { ContextCompat.getDrawable(context, it) }
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
