package com.kilomobi.washy.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kilomobi.washy.R
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class FeedPagerAdapter(val context: Context, private val items: ArrayList<Feed>) :
    RecyclerView.Adapter<FeedPagerAdapter.FeedViewHolder>() {

    class FeedViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.text)
        var tvDate: TextView = itemView.findViewById(R.id.date)
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
        holder.tvName.text = items[position].name
        holder.tvDate.text = items[position].timestamp?.let { getDateTime(it).toString() }

        Glide.with(context)
            .load(items[position].imageUrl)
            .into(holder.image)
    }

    private fun getDateTime(timestamp: Long): String? {
        val date = Date(timestamp)
        return DateFormat.getDateInstance().format(date)
    }
}

object ExampleFeedPagerData {

    fun createList(): ArrayList<Feed> {
        val list = ArrayList<Feed>()

        val date = Date().time

        list.add(
            Feed(
                id = "1",
                name = "Nett Auto'",
                imageUrl = "https://scontent-frt3-2.cdninstagram.com/v/t51.2885-15/e35/85248777_197953478111089_4455077596656151746_n.jpg?_nc_ht=scontent-frt3-2.cdninstagram.com&_nc_cat=101&_nc_ohc=JepJaytaOuYAX_AVHIY&oh=42a52ad451a43841e168b8fe885915b6&oe=5EF4CC2B",
                timestamp = date - 100
            )
        )

        list.add(
            Feed(
                id = "2",
                name = "Total Wash",
                imageUrl = "https://scontent-frt3-1.cdninstagram.com/v/t51.2885-15/e35/82179981_712136375986051_7445858551959714530_n.jpg?_nc_ht=scontent-frt3-1.cdninstagram.com&_nc_cat=107&_nc_ohc=d5rfPBnCbsAAX9pREWR&oh=c85bae3e65c7114fe385903ad56b0cd5&oe=5EF5759E",
                timestamp = date - 300
            )
        )

        list.add(
            Feed(
                id = "3",
                name = "JL Lavage",
                imageUrl = "https://scontent-frt3-1.cdninstagram.com/v/t51.2885-15/e35/84693514_524605288162782_8817558855297671390_n.jpg?_nc_ht=scontent-frt3-1.cdninstagram.com&_nc_cat=106&_nc_ohc=mWDRYGttEKEAX9ISDRZ&oh=244c25e963cfeb933641252186874910&oe=5EF5FA21",
                timestamp = date - 1020
            )
        )

        return list
    }

}