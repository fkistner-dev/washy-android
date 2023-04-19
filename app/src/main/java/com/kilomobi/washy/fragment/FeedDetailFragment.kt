package com.kilomobi.washy.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spanned
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.LayoutFeedDetailBinding
import com.kilomobi.washy.model.Feed
import de.hdodenhof.circleimageview.CircleImageView

class FeedDetailFragment : FragmentEmptyView(R.layout.layout_feed_detail) {

    private lateinit var feed: Feed
    private lateinit var binding: LayoutFeedDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val moveTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = moveTransition
        sharedElementReturnTransition = moveTransition
        postponeEnterTransition()

        super.onViewCreated(view, savedInstanceState)

        if (!viewIsCreated) {
            binding = LayoutFeedDetailBinding.bind(view)
            initialize()
            viewIsCreated = true
        }
    }

    private fun initialize() {
        if (arguments != null && requireArguments()["feed"] != null && requireArguments()["feed"] is Feed) {
            feed = requireArguments()["feed"] as Feed
        }

        fillView()
    }

    private fun fillView() {
        currentView?.let { v ->
            // Retrieve text and format with HTML tags
            v.findViewById<TextView>(R.id.title).text = feed.header
            val spanned: Spanned = HtmlCompat.fromHtml(
                getString(R.string.lorem_schnapsum_big),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            v.findViewById<TextView>(R.id.text).text = spanned

            // Set share button
            v.findViewById<AppCompatImageButton>(R.id.btn_share).setOnClickListener {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val appLink =
                    "https://play.google.com/store/apps/details?id=${requireActivity().applicationContext.packageName}"
                val shareBody = getString(R.string.intent_sharing_text, appLink)
                sharingIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.intent_sharing_title)
                )
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(
                    Intent.createChooser(
                        sharingIntent,
                        getString(R.string.intent_sharing_intent_title)
                    )
                )
            }

            // Shared image transition
            v.findViewById<ImageView>(R.id.image).apply {
                feed.photos[0].let {
                    val urlToLoad = FirebaseStorage.getInstance()
                        .getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + "feeds/" + it)
                    transitionName = "big_$it"
                    startEnterTransitionAfterLoadingImage(urlToLoad, this)
                }
            }

            v.findViewById<CircleImageView>(R.id.circle_image).apply {
                transitionName = "big_" + feed.authorPicture
            }
            v.findViewById<TextView>(R.id.top_header).apply {
                transitionName = "big_" + feed.cardviewHeader
                this.text = feed.cardviewHeader
            }
            v.findViewById<TextView>(R.id.top_text).apply {
                transitionName = "big_" + feed.cardviewText
                this.text = feed.cardviewText
            }

            // nice little animation for the footer call to action
            v.findViewById<CardView>(R.id.cardview_footer).apply {
                this.y += 200
                this.animate()
                    .setDuration(300)
                    .translationYBy(-200F)
                this.setOnClickListener {
                    // Do something
                }
            }
        }
    }

    private fun startEnterTransitionAfterLoadingImage(
        imageAddress: StorageReference,
        imageView: ImageView
    ) {
        Glide.with(this)
            .load(imageAddress)
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(imageView)
    }
}