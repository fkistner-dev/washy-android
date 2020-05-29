package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed

class FeedViewHolder(private val feedView: View) : RecyclerView.ViewHolder(feedView) {

    val name: Int = R.id.merchantText
    val message: Int = R.id.feedText

    fun bind(feed: Feed) {
        feedView.findViewById<TextView>(name).text = feed.merchantName
        feedView.findViewById<TextView>(message).text = feed.text
    }
}