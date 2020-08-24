package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.ActivityMainBinding
import com.bottlerocketstudios.brarchitecture.databinding.DrawerHeaderBinding
import kotlinx.android.synthetic.main.view_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private val activityViewModel: MainActivityViewModel by viewModel()

    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = activityViewModel
            viewToolbar.viewModel = activityViewModel
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.userFragment), drawerLayout).let {
                appBarConfiguration = it
                navView.setupWithNavController(navController)
                DrawerHeaderBinding.bind(navView.getHeaderView(0)).viewModel = activityViewModel
                setSupportActionBar(toolbar)
                setupActionBarWithNavController(navController, it)
            }
            lifecycleOwner = this@MainActivity
            // Don't show the toolbar's AppCompatTextView title, show the one we style
        }
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            activityViewModel.showToolbar(destination.label?.isNotEmpty() ?: false)
            activityViewModel.setTitle(destination.label.toString())
            binding.viewToolbar.toolbar.title = ""
            invalidateOptionsMenu()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        appBarConfiguration?.let {
            return navigateUp(navController, it)
        }
        return false
    }
}
