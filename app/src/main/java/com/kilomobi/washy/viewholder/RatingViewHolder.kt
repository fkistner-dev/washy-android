package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Rating
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class RatingViewHolder(private val ratingView: View) : RecyclerView.ViewHolder(ratingView) {

    val title: Int = R.id.title
    val text: Int = R.id.text
    private val ratingBar: Int = R.id.ratingBar

    fun bind(rating: Rating, selectedItem: Int) {
        ratingView.findViewById<TextView>(title).text = rating.userName
        ratingView.findViewById<TextView>(text).text = rating.text
        ratingView.findViewById<MaterialRatingBar>(ratingBar).rating = rating.stars
    }
}