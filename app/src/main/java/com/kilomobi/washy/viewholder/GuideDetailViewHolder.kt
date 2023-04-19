package com.kilomobi.washy.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.InnerGuide
import com.kilomobi.washy.util.textOrHide

class GuideDetailViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
    val prerequisite: Int = R.id.prerequisite
    val text: Int = R.id.text
    val warning: Int = R.id.warning
    val image: Int = R.id.image
    private val rootGroup: Int = R.id.root

    fun bind(innerGuide: InnerGuide, selectedItem: Int) {
        if (this.adapterPosition == 0) {
            val cardView = guideView.findViewById<CardView>(rootGroup)
            val param = cardView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,0,0)
            cardView.layoutParams = param
        }

        guideView.findViewById<TextView>(header).textOrHide(innerGuide.header)
        guideView.findViewById<TextView>(prerequisite).textOrHide(innerGuide.prerequisite)
        guideView.findViewById<TextView>(text).textOrHide(innerGuide.text)
        guideView.findViewById<TextView>(warning).textOrHide(innerGuide.warning)
        val imageView = guideView.findViewById<ImageView>(image)

        if (innerGuide.photo.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            Glide.with(guideView.context)
                .load(innerGuide.photo)
                .into(imageView)
        }
    }
}