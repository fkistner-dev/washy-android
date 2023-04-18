package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.util.textOrHide

class GuideListViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
    val text: Int = R.id.text
    val image: Int = R.id.image
    val cardview: Int = R.id.cardview

    fun bind(guide: Guide, selectedItem: Int) {
        guideView.findViewById<TextView>(header).text = guide.title
//        guideView.findViewById<TextView>(subheader).text = guide.subHeader
        guideView.findViewById<TextView>(text).textOrHide(guide.description)
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