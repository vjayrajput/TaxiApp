package com.taxi.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.taxi.app.R
import com.taxi.app.data.local.prefs.UserPreferenceProvider
import com.taxi.app.databinding.ActivityMainBinding
import com.taxi.app.databinding.NavHeaderBinding
import com.taxi.app.ui.dashboard.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    private val viewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var userPref: UserPreferenceProvider

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var headerBinding: NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration =
            AppBarConfiguration(navController.graph, binding.drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            supportActionBar?.show()

            when (destination.id) {
                R.id.fragmentSplash -> {
                    supportActionBar?.hide()
                }
                R.id.loginFragment -> {
                    /**
                     * hide back button from login fragment here
                     */
                    binding.toolbar.navigationIcon = null
                }
                R.id.dashboardFragment -> {
                    /**
                     * hide back button from login fragment here
                     */
                    binding.toolbar.navigationIcon =
                        AppCompatResources.getDrawable(this, R.drawable.ic_baseline_menu_24)
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        setupDrawerLayout()
    }

    private fun setupDrawerLayout() {

        headerBinding = NavHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        headerBinding.viewModel = viewModel

        binding.navigationView.setNavigationItemSelectedListener(this)

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (userPref.isLoggedIn) {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    navController.popBackStack()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawers()
        when (item.itemId) {
            R.id.menu_logout -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                userPref.clearUserSharedPreference()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        }
        return true
    }
}