package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed

class FeedPhotoViewHolder(private val viewOf: View) : FeedViewHolder(viewOf) {

    private val image: Int = R.id.image

    override fun bind(feed: Feed) {
        val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + feed.photos[0])

        Glide.with(viewOf.context)
            .load(urlToLoad)
            .into(viewOf.findViewById<ImageView>(image))
    }
}
