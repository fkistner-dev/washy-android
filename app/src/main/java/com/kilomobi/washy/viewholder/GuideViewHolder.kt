package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Guide
import com.kilomobi.washy.util.textOrHide

class GuideViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
//    val subheader: Int = R.id.subheader
    val text: Int = R.id.text
    val image: Int = R.id.image
//    val subtext: Int = R.id.subtext
    val cardview: Int = R.id.cardview

    fun bind(guide: Guide, selectedItem: Int) {
        guideView.findViewById<TextView>(header).text = guide.header
//        guideView.findViewById<TextView>(subheader).text = guide.subHeader
        guideView.findViewById<TextView>(text).textOrHide(guide.text)
        val imageView = guideView.findViewById<ImageView>(image)

        if (guide.photos[0].isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            Glide.with(guideView.context)
                .load(guide.photos[0])
                .into(imageView)
        }
    }
}