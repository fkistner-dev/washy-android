package com.kilomobi.washy.dealer

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kilomobi.washy.R
import com.kilomobi.washy.db.dealer.Dealer
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class DealerViewHolder(private val dealerView: View) : RecyclerView.ViewHolder(dealerView) {

    val name: Int = R.id.text
    val subtext: Int = R.id.subtext
    val price: Int = R.id.price
    val rating: Int = R.id.ratingBar
    val cardview: Int = R.id.card_view

    fun bind(dealer: Dealer, selectedItem: Int) {
        dealerView.findViewById<TextView>(name).text = dealer.name
        dealerView.findViewById<TextView>(subtext).text = dealer.presentation
        dealerView.findViewById<TextView>(price).text = dealer.priceRange
        dealerView.findViewById<MaterialRatingBar>(rating).rating = dealer.rating

        dealerView.findViewById<MaterialCardView>(cardview).apply {
            strokeColor = if (selectedItem == adapterPosition) dealerView.context.getColor(
                R.color.colorAccent
            ) else Color.WHITE
            strokeWidth = 3
        }
    }
}
