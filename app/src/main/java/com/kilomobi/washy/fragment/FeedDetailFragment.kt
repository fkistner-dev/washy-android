package com.kilomobi.washy.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
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
import kotlinx.android.synthetic.main.layout_feed_detail.view.*

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
        val explodeTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.explode)
        val fadeTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        val topTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_top)
        val bottomTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        val rightTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_right)
        val leftTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_left)

        sharedElementEnterTransition = ChangeBounds().apply {
            enterTransition = explodeTransition
            duration = 750
        }
        sharedElementReturnTransition= ChangeBounds().apply {
            enterTransition = explodeTransition
            duration = 750
        }

        // Add these two lines below
        setSharedElementTransitionOnEnter()
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

            Log.d(TAG, "Feed photoURI from DetailFragment = " + feed.photos[0])
            Log.d(TAG, "Feed hashcode from DetailFragment = " + feed.hashCode().toString())

            // Shared image transition
            v.findViewById<ImageView>(R.id.image).apply {
                feed.photos[0].let {
                    val urlToLoad = FirebaseStorage.getInstance()
                        .getReferenceFromUrl(BuildConfig.FIRESTORE_BUCKET + "feeds/" + it)
//                    ViewCompat.setTransitionName(this, it)
                    transitionName = it
                    startEnterTransitionAfterLoadingImage(urlToLoad, this)
                }
            }

            v.findViewById<CircleImageView>(R.id.circle_image).apply {
                transitionName = feed.authorPicture
                //this.setImageResource(feed.authorPicture);
            }
            v.findViewById<TextView>(R.id.top_header).apply {
                ViewCompat.setTransitionName(this, feed.cardviewHeader)
                transitionName = feed.cardviewHeader
                this.text = feed.cardviewHeader
            }
            v.findViewById<TextView>(R.id.top_text).apply {
                ViewCompat.setTransitionName(this, feed.cardviewText)
                transitionName = feed.cardviewText
                this.text = feed.cardviewText
            }

//            v.findViewById<RelativeLayout>(R.id.item_rl).apply {
////                ViewCompat.setTransitionName(this, feed.hashCode().toString())
//                transitionName = feed.hashCode().toString()
//                v.findViewById<TextView>(R.id.top_header).text = feed.cardviewHeader
//                v.findViewById<TextView>(R.id.top_text).text = feed.cardviewText
//
//            }
        }
    }

    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_element_transition)
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