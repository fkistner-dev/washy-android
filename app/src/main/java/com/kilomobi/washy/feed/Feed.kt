package com.kilomobi.washy.feed

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.Cell
import java.util.*

@Entity(tableName = Feed.TABLE_NAME)
data class Feed(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UID)
    val uid: Long = 0,
    @ColumnInfo(name = ID)
    override val id: String? = "0",
    @ColumnInfo(name = NAME)
    var name: String,
    @ColumnInfo(name = MESSAGE)
    var message: String? = null,
    @ColumnInfo(name = IMAGE_URL)
    var imageUrl: String? = null,
    @ColumnInfo(name = TIMESTAMP)
    var timestamp: Long? = Calendar.getInstance().timeInMillis
) : RecyclerItem, AdapterClick {
    companion object {
        const val TABLE_NAME = "feed"
        const val UID = "uid"
        const val ID = "id"
        const val NAME = "name"
        const val MESSAGE = "message"
        const val IMAGE_URL = "imageUrl"
        const val TIMESTAMP = "timestamp"
    }
}

object FeedCell : Cell<RecyclerItem>() {

    override fun belongsTo(item: RecyclerItem?): Boolean {
        return item is Feed
    }

    override fun type(): Int {
        return R.layout.row_feed_item
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return FeedViewHolder(parent.viewOf(type()))
    }

    override fun bind(
        holder: RecyclerView.ViewHolder,
        item: RecyclerItem?,
        selectedPosition: Int,
        listener: AdapterListener?
    ) {

        if (holder is FeedViewHolder && item is Feed) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                listener?.listen(item)
            }
        }
    }

}

open class FeedAdapter(listener: AdapterListener) : BaseListAdapter(
    FeedCell,
    listener = listener
)