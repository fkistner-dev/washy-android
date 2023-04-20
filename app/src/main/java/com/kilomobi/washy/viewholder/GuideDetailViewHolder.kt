package com.kilomobi.washy.viewholder

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.model.InnerGuide
import com.kilomobi.washy.util.textOrHide

class GuideDetailViewHolder(private val guideView: View) : RecyclerView.ViewHolder(guideView) {

    val header: Int = R.id.header
    val text: Int = R.id.text
    val warning: Int = R.id.warning
    val image: Int = R.id.image
    private val rootGroup: Int = R.id.root

    fun bind(innerGuide: InnerGuide, selectedItem: Int) {
        if (this.adapterPosition == 0) {
            val cardView = guideView.findViewById<CardView>(rootGroup)
            val param = cardView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,0,0)
            cardView.layoutParams = param
        }

        val spannedText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(innerGuide.text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(innerGuide.text)
        }
        guideView.findViewById<TextView>(text).textOrHide(spannedText)
        val warningText: String = if (!TextUtils.isEmpty(innerGuide.warning)) {
            guideView.context.getString(R.string.emoji_right_finger_pointing).plus(" " + innerGuide.warning)
        } else {
            ""
        }
        guideView.findViewById<TextView>(warning).textOrHide(warningText)
        val imageView = guideView.findViewById<ImageView>(image)

        if (innerGuide.photo.isEmpty()) {
            imageView.visibility = View.GONE
        } else {
            val urlToLoad = FirebaseStorage.getInstance().getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + innerGuide.photo)

            Glide.with(guideView.context)
                .load(urlToLoad)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .into(imageView)
        }
    }
}