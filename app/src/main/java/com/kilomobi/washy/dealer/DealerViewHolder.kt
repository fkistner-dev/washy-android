package com.kilomobi.washy.dealer

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kilomobi.washy.R
import com.kilomobi.washy.Service
import com.kilomobi.washy.db.dealer.Dealer
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.text.DecimalFormat


class DealerViewHolder(private val dealerView: View) : RecyclerView.ViewHolder(dealerView) {

    val name: Int = R.id.text
    val address: Int = R.id.address
    val price: Int = R.id.price
    val rating: Int = R.id.ratingBar
    val cardview: Int = R.id.card_view
    lateinit var linearLayout : LinearLayout

    fun bind(dealer: Dealer, selectedItem: Int) {
        val context = dealerView.context
        linearLayout = dealerView.findViewById(R.id.holder_services_image)

        dealerView.findViewById<TextView>(name).text = dealer.name
        dealerView.findViewById<TextView>(address).text = dealer.address
        val dec = DecimalFormat("#.00")
        dealerView.findViewById<TextView>(price).text = String.format(context.resources.getString(R.string.price_with_currency, dec.format(dealer.priceStart)))
        dealerView.findViewById<MaterialRatingBar>(rating).rating = dealer.rating

        dealerView.findViewById<MaterialCardView>(cardview).apply {
            strokeColor = if (selectedItem == adapterPosition) context.getColor(
                R.color.colorAccent
            ) else Color.WHITE
            strokeWidth = 3
        }

        linearLayout.removeAllViews()

        if (dealer.services != null) mapServices(dealer.services!!)
    }

    private fun mapServices(services: String) {
        val inflater = dealerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val list = Service.servicesToList(services)

        for (service in list) {
            if (service.isBlank()) return
            val childView = inflater.inflate(R.layout.item_service_image, null)
            val imageView = childView.findViewById<ImageView>(R.id.imageView)
            imageView.setPadding(0, 0, 12, 0)
            imageView.setImageResource(Service.retrieveImage(service))
            linearLayout.addView(childView)
        }
    }
}
