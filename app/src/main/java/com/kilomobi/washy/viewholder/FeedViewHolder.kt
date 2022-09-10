package com.kilomobi.washy.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed

abstract class FeedViewHolder(feedView: View) : RecyclerView.ViewHolder(feedView) {

    val name: Int = R.id.merchantText
    val message: Int = R.id.feedText

    abstract fun bind(feed: Feed)
}