package com.kilomobi.washy.feed

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.R

class FeedViewHolder(private val feedView: View) : RecyclerView.ViewHolder(feedView) {

    val name: Int = R.id.dealerText
    val message: Int = R.id.feedText

    fun bind(feed: Feed) {
        feedView.findViewById<TextView>(name).text = feed.name
        feedView.findViewById<TextView>(message).text = feed.message
    }
}