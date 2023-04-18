package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.InnerGuide

class GuideDetailViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
    val prerequisite: Int = R.id.prerequisite
    val text: Int = R.id.text
    val warning: Int = R.id.warning
    val image: Int = R.id.image

    fun bind(innerGuide: InnerGuide, selectedItem: Int) {
        guideView.findViewById<TextView>(header).text = innerGuide.header
        guideView.findViewById<TextView>(prerequisite).text = innerGuide.prerequisite
        guideView.findViewById<TextView>(text).text = innerGuide.text
        guideView.findViewById<TextView>(warning).text = innerGuide.warning
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