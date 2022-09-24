package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed

class FeedPhotoViewHolder(private val viewOf: View) : FeedViewHolder(viewOf) {

    private val image: Int = R.id.image

    override fun bind(feed: Feed) {
        Glide.with(viewOf.context)
            .load(feed.photos[0])
            .into(viewOf.findViewById<ImageView>(image))
    }
}
