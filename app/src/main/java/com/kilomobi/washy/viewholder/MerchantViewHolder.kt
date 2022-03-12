package com.kilomobi.washy.viewholder

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Merchant
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class MerchantViewHolder(private val merchantView: View) : RecyclerView.ViewHolder(merchantView) {

    val type: Int = R.id.type
    val title: Int = R.id.title
    val text: Int = R.id.text
    val rating: Int = R.id.ratingBar
    private val cardview: Int = R.id.cardview

    fun bind(merchant: Merchant, selectedItem: Int) {
        val context = merchantView.context
        if (merchant.imported) {
            merchantView.findViewById<TextView>(type).visibility = View.GONE
        }
        merchantView.findViewById<TextView>(type).text = if (merchant.siren?.isNotEmpty() == true) context.getString(R.string.merchant_pro) else context.getString(R.string.merchant_part)
        merchantView.findViewById<TextView>(title).text = merchant.name
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

//        if (merchant.services != null) mapServices(merchant.services!!)
    }

//    private fun mapServices(services: String) {
//        val inflater = merchantView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val list = Service.servicesToList(services)
//
//        for (service in list) {
//            if (service.isBlank()) return
//            val childView = inflater.inflate(R.layout.item_service_image, null)
//            val imageView = childView.findViewById<ImageView>(R.id.imageView)
//            imageView.setPadding(0, 0, 12, 0)
//            imageView.setImageResource(Service.retrieveImage(service))
//            linearLayout.addView(childView)
//        }
//    }
}
