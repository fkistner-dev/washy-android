package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Product

class ProductViewHolder(private val productView: View) : RecyclerView.ViewHolder(productView) {

    val title: Int = R.id.title
    val text: Int = R.id.text
    val price: Int = R.id.price
    val duration: Int = R.id.duration
    val image: Int = R.id.image

    fun bind(product: Product, selectedItem: Int) {
        productView.findViewById<TextView>(title).text = product.title
        productView.findViewById<TextView>(text).text = product.description
        productView.findViewById<TextView>(price).text = product.price.toString() + " €"
        productView.findViewById<TextView>(duration).text = product.duration
        val imageView = productView.findViewById<ImageView>(image)

        if (product.imageUrl.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            Glide.with(productView.context)
                .load(product.imageUrl)
                .into(imageView)
        }
    }
}