package com.kilomobi.washy.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kilomobi.washy.R
import com.kilomobi.washy.fragment.MerchantListFragment
import com.kilomobi.washy.fragment.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity(),
    MainActivityDelegate,
    MapListener {

    companion object {
        var hostFragment: Fragment? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inflater = navHostFragment.findNavController().navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        navHostFragment.findNavController().graph = graph
        hostFragment = navHostFragment
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setupNavDrawer(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Suppress the title of toolbar
        toggle.syncState()

        drawerLayout.navView.setupWithNavController(navHostFragment.findNavController())
    }

    override fun enableNavDrawer(enable: Boolean) {
        drawerLayout.isEnabled = enable
    }

    override fun notifyViewPagerChange(id: Int) {
        val merchantFragment = supportFragmentManager.findFragmentByTag("merchant") as MerchantListFragment
        merchantFragment.updateListPosition(id)
    }

    override fun notifyMapViewChange() {
        val mapFragment = supportFragmentManager.findFragmentByTag("map") as MapFragment
    }
}