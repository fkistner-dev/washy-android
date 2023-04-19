package com.kilomobi.washy.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.util.textOrHide

class GuideListViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
    val text: Int = R.id.text
    val image: Int = R.id.image
    val cardview: Int = R.id.cardview
    private val sponsor: Int = R.id.sponsor

    fun bind(guide: Guide, selectedItem: Int) {
        if (this.adapterPosition == 0) {
            val cardView = guideView.findViewById<CardView>(cardview)
            val param = cardView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(cardView.marginLeft,0, cardView.marginRight, cardView.marginBottom)
            cardView.layoutParams = param
        }

        guideView.findViewById<TextView>(header).text = guide.title
        guideView.findViewById<TextView>(text).textOrHide(guide.description)
        guideView.findViewById<Chip>(sponsor).visibility = if (guide.isPromotional) View.VISIBLE else View.GONE
        val imageView = guideView.findViewById<ImageView>(image)

        if (guide.photo.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            Glide.with(guideView.context)
                .load(guide.photo)
                .into(imageView)
        }
    }
}