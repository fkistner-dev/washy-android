/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

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