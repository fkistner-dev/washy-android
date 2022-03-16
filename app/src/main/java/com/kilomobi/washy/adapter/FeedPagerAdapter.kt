package com.kilomobi.washy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.util.GlideApp
import com.kilomobi.washy.util.MyAppGlideModule
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class FeedPagerAdapter(val context: Context, private val items: ArrayList<Feed>) :
    RecyclerView.Adapter<FeedPagerAdapter.FeedViewHolder>() {

    companion object {
        const val FIRESTORE_BUCKET = "gs://washy-dev.appspot.com/"
    }

    class FeedViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.text)
        var image: ImageView = itemView.findViewById(R.id.image)
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
        holder.title.text = items[position].merchantName
        if (items[position].photos.isNotEmpty()) {
            val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(FIRESTORE_BUCKET + "feeds/" + items[position].photos[0])

            GlideApp.with(context)
                .load(urlToLoad)
                .into(holder.image)
        }
    }

    private fun getDateTime(timestamp: Long): String? {
        val date = Date(timestamp)
        return DateFormat.getDateInstance().format(date)
    }
}