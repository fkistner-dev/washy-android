package com.kilomobi.washy.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kilomobi.washy.R
import com.kilomobi.washy.fragment.*
import com.kilomobi.washy.util.currentNavigationFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.nav_host_fragment.*
import kotlinx.android.synthetic.main.nav_host_fragment.view.*

class MainActivity : AppCompatActivity(),
    MainActivityDelegate, UserListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_controller_fragment)
//        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.onboardingFragment
            ),
            drawer_layout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        setupNavigationMenu()
        setupNavigationItem()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun setupNavigationMenu() {
        nav_view.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun invalidateOptionsMenu() {
        super.invalidateOptionsMenu()
        setupNavigationItem()
    }

    private fun setupNavigationItem() {
        val menu = nav_view.menu
        val isConnected = !FirebaseAuth.getInstance().uid.isNullOrBlank()

        menu.clear()

        if (isConnected) {
            nav_view.inflateMenu(R.menu.menu_nav_drawer)
        } else {
            nav_view.inflateMenu(R.menu.menu_nav_drawer_disconnected)
        }

        assignUserToHeader(FirebaseAuth.getInstance().currentUser)

        val headerView: View = nav_view.getHeaderView(0)
        headerView.findViewById<ImageView>(R.id.washy).visibility = if (isConnected) View.GONE else View.VISIBLE
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun enableNavDrawer(enable: Boolean) {
        drawer_layout.isEnabled = enable
    }

    override fun setFullscreen(enable: Boolean) {
        if (enable) supportActionBar?.hide()
        else supportActionBar?.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.action_map -> {
                if (supportFragmentManager.currentNavigationFragment !is MapFragment) {
                    navController.navigate(R.id.action_homeFragment_to_mapFragment)
                }
                true
            }
            R.id.action_feed -> {
                if (supportFragmentManager.currentNavigationFragment !is FeedFragment) {
                    navController.navigate(R.id.action_homeFragment_to_feedFragment)
                }
                true
            }
            R.id.action_photolab -> {
                if (supportFragmentManager.currentNavigationFragment !is PhotoLabFragment) {
                    navController.navigate(R.id.action_homeFragment_to_photolabFragment)
                }
                true
            }
            R.id.action_become_washer -> {
                if (supportFragmentManager.currentNavigationFragment !is BecomeWasherFragment) {
                    navController.navigate(R.id.action_homeFragment_to_becomeWasherFragment)
                }
                true
            }
            R.id.action_connect -> {
                if (supportFragmentManager.currentNavigationFragment !is LoginFragmentHelper) {
                    navController.navigate(R.id.action_homeFragment_to_identificationFragment)
                }
                true
            }
            R.id.action_disconnect -> {
                disconnectDialog()
                true
            }
            R.id.action_tos -> {
                if (supportFragmentManager.currentNavigationFragment !is TermOfServiceFragment) {
                    navController.navigate(R.id.action_homeFragment_to_tosFragment)
                }
                true
            }
            R.id.action_debug -> {
                if (supportFragmentManager.currentNavigationFragment !is RepositoryTesterFragment) {
                    navController.navigate(R.id.action_homeFragment_to_testerFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun disconnectDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_disconnect_title))
        builder.setMessage(resources.getString(R.string.dialog_disconnect_text))
        builder.setPositiveButton(resources.getString(R.string.common_yes)) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            invalidateOptionsMenu()
            Snackbar.make(nav_view, getString(R.string.common_disconnect_success), Snackbar.LENGTH_LONG).show()
        }
        builder.setNegativeButton(resources.getString(R.string.common_no), null)
        builder.show()
    }

    override fun onConnected(user: FirebaseUser) {
        invalidateOptionsMenu()
        assignUserToHeader(user)
    }

    private fun assignUserToHeader(user: FirebaseUser?) {
        val headerView: View = nav_view.getHeaderView(0)

        if (user != null) {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.VISIBLE
            if (user.photoUrl != null) {
                Glide.with(applicationContext)
                    .load(user.photoUrl)
                    .into(headerView.findViewById<ImageView>(R.id.profilePic))
            }

            headerView.findViewById<TextView>(R.id.profileText).text = user.displayName
        } else {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.GONE
            Glide.with(applicationContext)
                .load(R.drawable.ic_account_outline)
                .into(headerView.findViewById<ImageView>(R.id.profilePic))
            headerView.findViewById<TextView>(R.id.profileText).text = ""
        }

    }
}
