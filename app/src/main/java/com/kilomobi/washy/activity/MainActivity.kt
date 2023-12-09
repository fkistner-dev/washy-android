package com.kilomobi.washy.activity

import android.content.pm.ActivityInfo
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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.ActivityMainBinding
import com.kilomobi.washy.fragment.*
import com.kilomobi.washy.model.User
import com.kilomobi.washy.util.ChromeUtils
import com.kilomobi.washy.util.WashyAuth
import com.kilomobi.washy.util.currentNavigationFragment
import com.kilomobi.washy.viewmodel.UserViewModel

class MainActivity : AppCompatActivity(),
    MainActivityDelegate, UserListener, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val STACK_USER_STORE_ID = "userStoreId"
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        firebaseAnalytics = Firebase.analytics

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.onboardingFragment
            ),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Firebase.messaging.isAutoInitEnabled = true

        setupNavigationMenu()
        setupNavigationItem()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupNavigationMenu() {
        binding.navView.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setupNavigationItem() {
        val menu = binding.navView.menu
        val isConnected = !WashyAuth.getUid().isNullOrBlank()
        menu.clear()

        if (isConnected) {
            binding.navView.inflateMenu(R.menu.menu_nav_drawer)
            val viewModel = UserViewModel()
            WashyAuth.getUid()!!.let { userId ->
                viewModel.getUser(userId).observe(this) { user ->
                    handleWasherMenu(menu, user)
                    navController.currentBackStackEntry?.arguments?.putString(STACK_USER_STORE_ID, user?.store)
                }
            }
        } else {
            binding.navView.inflateMenu(R.menu.menu_nav_drawer_disconnected)
        }

        assignUserToHeader(WashyAuth.getUid())
        val headerView: View = binding.navView.getHeaderView(0)
        headerView.findViewById<ImageView>(R.id.washy).visibility = if (isConnected) View.GONE else View.VISIBLE
    }

    private fun handleWasherMenu(menu: android.view.Menu, user: User?) {
        if (user == null || user.store.isEmpty()) {
            menu.findItem(R.id.action_become_washer).isVisible = true
            menu.findItem(R.id.action_store_washer).isVisible = false
        } else {
            menu.findItem(R.id.action_store_washer).isVisible = true
            menu.findItem(R.id.action_become_washer).isVisible = false
        }
    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun enableNavDrawer(enable: Boolean) {
        binding.drawerLayout.isEnabled = enable
    }

    override fun setFullscreen(enable: Boolean) {
        if (enable) supportActionBar?.hide()
        else supportActionBar?.show()
    }

    override fun getAnalytics(): FirebaseAnalytics {
        return firebaseAnalytics
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId) {
            R.id.action_map -> {
                if (supportFragmentManager.currentNavigationFragment !is MapFragment) {
                    navController.navigate(R.id.action_homeFragment_to_mapFragment)
                }
                true
            }
            R.id.action_guide_list -> {
                if (supportFragmentManager.currentNavigationFragment !is GuideListFragment) {
                    navController.navigate(R.id.action_homeFragment_to_guideListFragment)
                }
                true
            }
            R.id.action_become_washer -> {
                if (supportFragmentManager.currentNavigationFragment !is BecomeWasherFragment) {
                    navController.navigate(R.id.action_homeFragment_to_becomeWasherFragment)
                }
                true
            }
            R.id.action_store_washer -> {
                if (supportFragmentManager.currentNavigationFragment !is BecomeWasherFragment) {
                    navController.navigate(R.id.action_homeFragment_to_storeWasherFragment)
                }
                true
            }
            R.id.action_connect -> {
                onAuthenticationConnected(WashyAuth.generateUid())
                true
            }
            R.id.action_disconnect -> {
                disconnectDialog()
                true
            }
            R.id.action_tos -> {
                if (supportFragmentManager.currentNavigationFragment !is WebFragment) {
                    ChromeUtils.openChromeTab(this, view, getString(R.string.privacy_url))
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
            WashyAuth.resetUid()
            setupNavigationItem()
            Snackbar.make(binding.navView, getString(R.string.common_disconnect_success), Snackbar.LENGTH_LONG).show()
        }
        builder.setNegativeButton(resources.getString(R.string.common_no), null)
        builder.show()
    }

    override fun onAuthenticationConnected(user: String) {
        setupNavigationItem()
        Snackbar.make(binding.navView, getString(R.string.common_welcome_user, user), Snackbar.LENGTH_LONG).show()
    }

    override fun onAuthenticationCancel() {
        Snackbar.make(binding.navView, getString(R.string.common_auth_failed), Snackbar.LENGTH_LONG).show()
    }

    private fun assignUserToHeader(user: String?) {
        val headerView: View = binding.navView.getHeaderView(0)
        if (user != null) {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.VISIBLE
            // Disable FirebaseAuth in order to publish the app in open beta on Google Play
            /*if (user.photoUrl != null) {
                Glide.with(applicationContext)
                    .load(user.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(headerView.findViewById(R.id.profilePic))
            }*/

            headerView.findViewById<TextView>(R.id.profileText).text = user
        } else {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.GONE
            Glide.with(applicationContext)
                .load(R.drawable.ic_account_outline)
                .into(headerView.findViewById(R.id.profilePic))
            headerView.findViewById<TextView>(R.id.profileText).text = ""
        }
    }
}