package com.kilomobi.washy.model

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.kilomobi.washy.R
import com.kilomobi.washy.recycler.RecyclerItem
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.recycler.BaseListAdapter
import com.kilomobi.washy.recycler.Cell
import com.kilomobi.washy.viewholder.FeedPhotoViewHolder
import com.kilomobi.washy.viewholder.FeedPromotionalViewHolder
import com.kilomobi.washy.viewholder.FeedStandardViewHolder
import com.kilomobi.washy.viewholder.FeedViewHolder
import java.io.Serializable

data class Feed(
    @Exclude var reference: String = "",
    var author: String = "",
    var authorPicture: String = "",
    var cardviewHeader: String = "",
    var cardviewText: String = "",
    var merchantId: String = "",
    var merchantName: String = "",
    var header: String = "",
    var subHeader: String = "",
    var text: String = "",
    var subText: String = "",
    var price: Long = 0,
    var like: Int = 0,
    var isPromotional: Boolean = false,
    var verified: Boolean = false,
    var discount: String = "",
    var photos: List<String> = listOf(),
    @Transient var createdAt: Timestamp = Timestamp.now(),
    @Transient var expireAt: Timestamp = Timestamp.now()
) : RecyclerItem(), AdapterClick, Serializable

object FeedCell : Cell<RecyclerItem>() {

    var itemState = -1

    private const val IS_STANDARD = 1
    private const val IS_PROMOTIONAL = 2
    private const val IS_PHOTO = 3

    override fun belongsTo(item: RecyclerItem?): Boolean {
        if (item !is Feed) return false

        itemState = when {
            item.isPromotional -> IS_PROMOTIONAL
            item.photos.isNotEmpty() -> IS_PHOTO
            else -> IS_STANDARD
        }
        return true
    }

    override fun type(): Int {
        return when (itemState) {
            IS_PROMOTIONAL -> R.layout.row_feed_item
            IS_PHOTO -> R.layout.row_feed_photo_item
            else -> R.layout.row_feed_pager_item
        }
    }

    override fun holder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return when (itemState) {
            IS_PROMOTIONAL -> FeedPromotionalViewHolder(parent.viewOf(type()))
            IS_PHOTO -> FeedPhotoViewHolder(parent.viewOf(type()))
            else -> FeedStandardViewHolder(parent.viewOf(type()))
        }
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