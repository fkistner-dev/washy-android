package com.kilomobi.washy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.model.Feed
import com.kilomobi.washy.util.GlideApp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FeedPagerAdapter(val context: Context, private val items: ArrayList<Feed>) :
    RecyclerView.Adapter<FeedPagerAdapter.FeedViewHolder>() {

    class FeedViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById(R.id.header)
        var subHeader: TextView = itemView.findViewById(R.id.subheader)
        var text: TextView = itemView.findViewById(R.id.text)
        var subText: TextView = itemView.findViewById(R.id.subtext)
        var date: TextView = itemView.findViewById(R.id.date)
        var badge: TextView = itemView.findViewById(R.id.badge)
        var like: TextView = itemView.findViewById(R.id.like)
        var image: ImageView = itemView.findViewById(R.id.image)
        var action: Button = itemView.findViewById(R.id.call_to_action)
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
        holder.header.text = feed.merchantName
        holder.subHeader.text = feed.subHeader
        holder.text.text = feed.text
        holder.subText.text = feed.subText
        holder.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(feed.createdAt.toDate())
        holder.badge.text = feed.discount
        //holder.action.text = context.resources.getString(R.string.cardview_call_to_action)
        if (feed.like == 0) {
            holder.like.visibility = View.INVISIBLE
        } else {
            holder.like.text = context.resources.getString(R.string.cardview_like, feed.like)
        }

        if (feed.photos.isNotEmpty()) {
            val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + "feeds/" + feed.photos[0])

            GlideApp.with(context)
                .load(urlToLoad)
                .into(holder.image)
        }
    }

    private fun getDateTime(timestamp: Long): String? {
        val date = Date(timestamp)
        return DateFormat.getDateInstance().format(date)
    }

    fun getDate(dateStr: String) {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val mDate = formatter.parse(dateStr)
            return
        } catch (e: Exception) { }
    }
}