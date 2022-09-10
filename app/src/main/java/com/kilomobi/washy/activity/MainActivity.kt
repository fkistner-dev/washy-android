package com.kilomobi.washy.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.kilomobi.washy.BuildConfig
import com.kilomobi.washy.R
import com.kilomobi.washy.databinding.ActivityMainBinding
import com.kilomobi.washy.fragment.*
import com.kilomobi.washy.model.User
import com.kilomobi.washy.util.currentNavigationFragment
import com.kilomobi.washy.viewmodel.UserViewModel

class MainActivity : AppCompatActivity(),
    MainActivityDelegate, UserListener, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
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

        setupNavigationMenu()
        setupNavigationItem()
        binding.navView.setNavigationItemSelectedListener(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("TAG", token)
            Snackbar.make(view, token, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupNavigationMenu() {
        binding.navView.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setupNavigationItem() {
        val menu = binding.navView.menu
        val isConnected = !FirebaseAuth.getInstance().uid.isNullOrBlank()
        menu.clear()

        if (isConnected) {
            binding.navView.inflateMenu(R.menu.menu_nav_drawer)
            val viewModel = UserViewModel()
            FirebaseAuth.getInstance().uid?.let { userId ->
                viewModel.getUser(userId).observe(this) { user ->
                    handleWasherMenu(menu, user)
                }
            }
        } else {
            binding.navView.inflateMenu(R.menu.menu_nav_drawer_disconnected)
        }

        // AddMenu Item if debug
        if (BuildConfig.FLAVOR == "Dev") {
            val submenu = menu.addSubMenu("Admin")
            submenu.add("Debug")
            submenu.getItem(0).setIcon(R.drawable.ic_motorcycle)
            binding.navView.invalidate()
        }

        assignUserToHeader(FirebaseAuth.getInstance().currentUser)
        val headerView: View = binding.navView.getHeaderView(0)
        headerView.findViewById<ImageView>(R.id.washy).visibility = if (isConnected) View.GONE else View.VISIBLE
    }

    private fun handleWasherMenu(menu: android.view.Menu, user: User) {
        if (user.store.isEmpty()) {
            menu.findItem(R.id.action_become_washer).isVisible = true
        } else {
            menu.findItem(R.id.action_store_washer).isVisible = true
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
                    val builder = CustomTabsIntent.Builder()
                    val params = CustomTabColorSchemeParams.Builder()
                    params.setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
                    builder.setDefaultColorSchemeParams(params.build())
                    builder.setShowTitle(true)

                    builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                    builder.setExitAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                    val customBuilder = builder.build()
                    val packageName = "com.android.chrome"

                    if (this.isPackageInstalled(packageName)) {
                        // if chrome is available use chrome custom tabs
                        customBuilder.intent.setPackage(packageName)
                        customBuilder.launchUrl(this, Uri.parse(getString(R.string.privacy_url)))
                    } else {
                        // if not available use WebView to launch the url
                        navController.navigate(R.id.action_homeFragment_to_tosFragment)
                    }
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

    private fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun disconnectDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_disconnect_title))
        builder.setMessage(resources.getString(R.string.dialog_disconnect_text))
        builder.setPositiveButton(resources.getString(R.string.common_yes)) { _, _ ->
            FirebaseAuth.getInstance().signOut()
            setupNavigationItem()
            Snackbar.make(binding.navView, getString(R.string.common_disconnect_success), Snackbar.LENGTH_LONG).show()
        }
        builder.setNegativeButton(resources.getString(R.string.common_no), null)
        builder.show()
    }

    override fun onAuthenticationConnected(user: FirebaseUser) {
        setupNavigationItem()
        Snackbar.make(binding.navView, getString(R.string.common_welcome_user, user.displayName), Snackbar.LENGTH_LONG).show()
    }

    override fun onAuthenticationCancel() { }

    private fun assignUserToHeader(user: FirebaseUser?) {
        val headerView: View = binding.navView.getHeaderView(0)

        if (user != null) {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.VISIBLE
            if (user.photoUrl != null) {
                Glide.with(applicationContext)
                    .load(user.photoUrl)
                    .into(headerView.findViewById(R.id.profilePic))
            }

            headerView.findViewById<TextView>(R.id.profileText).text = user.displayName
        } else {
            headerView.findViewById<FrameLayout>(R.id.profileFrame).visibility = View.GONE
            Glide.with(applicationContext)
                .load(R.drawable.ic_account_outline)
                .into(headerView.findViewById(R.id.profilePic))
            headerView.findViewById<TextView>(R.id.profileText).text = ""
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    /*
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }*/
}