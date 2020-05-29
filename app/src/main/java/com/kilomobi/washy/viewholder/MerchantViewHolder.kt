package com.kilomobi.washy.viewholder

import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.GeoPoint
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Service
import com.kilomobi.washy.model.Merchant
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.io.IOException
import java.text.DecimalFormat
import java.util.*


class MerchantViewHolder(private val merchantView: View) : RecyclerView.ViewHolder(merchantView) {

    val name: Int = R.id.text
    val address: Int = R.id.address
    val price: Int = R.id.price
    val rating: Int = R.id.ratingBar
    val cardview: Int = R.id.card_view
    lateinit var linearLayout : LinearLayout

    fun bind(merchant: Merchant, selectedItem: Int) {
        val context = merchantView.context
        linearLayout = merchantView.findViewById(R.id.holder_services_image)

        merchantView.findViewById<TextView>(name).text = merchant.name
        merchantView.findViewById<TextView>(address).text = geopointToAddress(merchantView.context, merchant.position!!)
        val dec = DecimalFormat("#.00")
        merchantView.findViewById<TextView>(price).text = String.format(context.resources.getString(R.string.price_with_currency, dec.format(merchant.priceStart)))
        merchantView.findViewById<MaterialRatingBar>(rating).rating = merchant.avgRating

        merchantView.findViewById<MaterialCardView>(cardview).apply {
            strokeColor = if (selectedItem == adapterPosition) context.getColor(
                R.color.colorAccent
            ) else Color.WHITE
            strokeWidth = 3
        }

        linearLayout.removeAllViews()

        if (merchant.services != null) mapServices(merchant.services!!)
    }

    private fun mapServices(services: String) {
        val inflater = merchantView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

    private fun geopointToAddress(context: Context, geoPoint: GeoPoint): String {
        val geoCoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: List<Address> = geoCoder.getFromLocation(
                geoPoint.latitude,
                geoPoint.longitude, 1)
            var add = ""
            if (addresses.isNotEmpty()) {
                add += addresses[0].getAddressLine(0).toString()
            }
            return add
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return geoPoint.toString()
    }
}
