package com.kilomobi.washy.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.kilomobi.washy.R

class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorDoneText(Color.BLACK)
        setColorSkipButton(Color.BLACK)

        // Make sure you don't call setContentView!
        addSlide(
            AppIntroFragment.newInstance(
                title = "Bienvenu sur Washy",
                description = "1ère plateforme Made In France sur le lavage véhicule à la main",
                backgroundDrawable = R.drawable.car_at_night
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Un bon lavage",
                description = "Se fait à la main, avec les bons produits, et la technique d'un passionné.",
                backgroundDrawable = R.drawable.empty_road
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Retrouver ici",
                description = "Les personnes qui prendront soin de votre auto",
                backgroundDrawable = R.drawable.portrait_pace
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        onboardingComplete()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        onboardingComplete()
    }

    private fun onboardingComplete() {
        MainActivity.hostFragment?.findNavController()?.navigate(R.id.action_intro_activity_to_map_fragment)
    }
}