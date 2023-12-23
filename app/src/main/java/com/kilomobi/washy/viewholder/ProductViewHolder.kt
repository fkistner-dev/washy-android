/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.BuildConfig
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
        val imageView = productView.findViewById<ImageView>(image)

        if (product.imageUrl.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + product.imageUrl)

            Glide.with(productView.context)
                .load(urlToLoad)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        }
    }
}