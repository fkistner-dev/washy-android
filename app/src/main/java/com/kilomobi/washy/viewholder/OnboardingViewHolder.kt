/*
 * Created by fkistner.
 * fabrice.kistner.pro@gmail.com
 * Last modified on 16/12/2023 19:40.
 * Copyright (c) 2023.
 * All rights reserved.
 */

package com.kilomobi.washy.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kilomobi.washy.model.OnboardingItem
import com.kilomobi.washy.R

class OnboardingViewHolder(private val onboardingView: View) : RecyclerView.ViewHolder(onboardingView) {

    val background: Int = R.id.onboardingBackground
    val image: Int = R.id.onboardingImage
    val title: Int = R.id.title
    val text: Int = R.id.description

    fun bind(onboarding: OnboardingItem) {
        onboardingView.findViewById<ImageView>(background).setImageResource(onboarding.background)
        onboarding.image?.let { onboardingView.findViewById<ImageView>(image).setImageResource(it) }
        onboardingView.findViewById<TextView>(title).text = onboarding.title
        onboardingView.findViewById<TextView>(text).text = onboarding.description
    }
}