/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class FeedPagerAdapter(val context: Context, private val items: ArrayList<Feed>, private val cellClickListener: FeedPagerListener) :
    RecyclerView.Adapter<FeedPagerAdapter.FeedViewHolder>() {

    class FeedViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView = itemView.findViewById(R.id.cardview)
        var circleImage: CircleImageView = cardView.findViewById(R.id.circle_image)
        var header: TextView = cardView.findViewById(R.id.header)
        var text: TextView = cardView.findViewById(R.id.text)
        var image: ImageView = cardView.findViewById(R.id.image)
        var darken: View = cardView.findViewById(R.id.view_dark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.row_feed_pager_item, parent, false)
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = items[position]
        holder.header.text = feed.cardviewHeader
        holder.text.text = feed.cardviewText

        // Update later when we handle profile picture for feed item
        holder.circleImage.visibility = View.GONE
        if (feed.photos.isNotEmpty()) {
            val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + feed.photos[0])

            Glide.with(context)
                .load(urlToLoad)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.image)

            holder.cardView.setOnClickListener {
                cellClickListener.listen(holder, feed)
            }
        }
    }

    interface FeedPagerListener {
        fun listen(holder: FeedViewHolder, feed: Feed)
    }
}