package com.kilomobi.washy.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.kilomobi.washy.model.OnboardingItem
import com.kilomobi.washy.R
import com.kilomobi.washy.activity.MainActivityDelegate
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.adapter.AdapterListener
import com.kilomobi.washy.adapter.OnboardingAdapter
import com.kilomobi.washy.fragment.StartFragment.Companion.COMPLETED_ONBOARDING_PREF_NAME
import kotlinx.android.synthetic.main.layout_onboarding.*

class OnboardingFragment : Fragment(), AdapterListener {

    private lateinit var mainActivityDelegate: MainActivityDelegate
    private val listAdapter by lazy {
        OnboardingAdapter(
            this
        )
    }
    private lateinit var layoutOnboardingIndicator: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_onboarding, container, false)
        layoutOnboardingIndicator = view.findViewById(R.id.onboardingLayout)

        try {
            mainActivityDelegate = context as MainActivityDelegate
        } catch (e: ClassCastException) {
            throw ClassCastException()
        }

        arguments?.getBoolean("isFullscreen")?.let { mainActivityDelegate.setFullscreen(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onboardingPager = view.findViewById<ViewPager2>(R.id.onboardingViewPager)

        val onboardingItems = listOf(
            OnboardingItem(
                1,
                R.drawable.onboarding_01,
                R.drawable.background_onboarding_01,
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_description_1)
            ),
            OnboardingItem(
                2,
                R.drawable.onboarding_02,
                R.drawable.background_onboarding_02,
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_description_2)
            ),
            OnboardingItem(
                3,
                background = R.drawable.background_onboarding_03,
                title = getString(R.string.onboarding_title_3),
                description = getString(R.string.onboarding_description_3)
            )
        )

        listAdapter.submitList(onboardingItems)

        onboardingPager.adapter = listAdapter
        setupIndicators()
        setCurrentIndicator(0)

        onboardingPager.registerOnPageChangeCallback(object :
        ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                onboardingButton.text = if (position + 1 < listAdapter.itemCount) getString(R.string.onboarding_button_continue) else getString(R.string.onboarding_button_finish)
            }
        })

        onboardingButton.setOnClickListener {
            if (onboardingPager.currentItem + 1 < listAdapter.itemCount) {
                onboardingPager.currentItem += 1
            } else {
                // User has seen OnboardingSupportFragment, so mark our SharedPreferences
                // flag as completed so that we don't show our OnboardingSupportFragment
                // the next time the user launches the app.
                PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                    putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true)
                    apply()
                }

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.onboardingFragment, true)
                    .build()
                findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment, null, navOptions)
            }
        }
    }

    override fun listen(click: AdapterClick?) {
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(listAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.oboarding_indicator_inactive))
                this?.layoutParams = layoutParams
            }
            layoutOnboardingIndicator.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = layoutOnboardingIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = layoutOnboardingIndicator[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.onboarding_indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.oboarding_indicator_inactive))
            }
        }
    }
}