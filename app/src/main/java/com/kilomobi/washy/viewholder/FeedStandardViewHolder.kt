package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.TextView
import com.kilomobi.washy.model.Feed

class FeedStandardViewHolder(private val feedView: View) : FeedViewHolder(feedView) {

    override fun bind(feed: Feed) {
        feedView.findViewById<TextView>(name).text = feed.merchantName
        feedView.findViewById<TextView>(message).text = feed.text
    }
}