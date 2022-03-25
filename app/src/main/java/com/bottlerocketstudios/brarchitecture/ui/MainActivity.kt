package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.ActivityMainBinding
import com.bottlerocketstudios.brarchitecture.databinding.DrawerHeaderBinding
import com.bottlerocketstudios.brarchitecture.ui.util.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private val activityViewModel: MainActivityViewModel by viewModel()

    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = activityViewModel
            lifecycleOwner = this@MainActivity
            viewToolbar.viewModel = activityViewModel
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.profileFragment, R.id.snippetsFragment), drawerLayout).let {
                appBarConfiguration = it
                navView.setupWithNavController(navController)
                DrawerHeaderBinding.bind(navView.getHeaderView(0)).apply {
                    viewModel = activityViewModel
                    lifecycleOwner = this@MainActivity
                }
                setSupportActionBar(viewToolbar.toolbar)
                setupActionBarWithNavController(navController, it)
            }
            devOptionsCta.setOnClickListener {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_to_devOptionsFragment)
                drawerLayout.close()
            }
            // Don't show the toolbar's AppCompatTextView title, show the one we style
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            this@MainActivity.hideKeyboard() // hide keyboard for every destination change - prevents need to manually hide keyboard per fragment just prior to navigating away
            activityViewModel.showToolbar.value = destination.label?.isNotEmpty() ?: false
            activityViewModel.title.value = destination.label.toString()
            binding.viewToolbar.toolbar.title = ""
            invalidateOptionsMenu()

            window.setSoftInputMode(
                when (destination.id) {
                    // R.id.loginFragment -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    else -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                }
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        appBarConfiguration?.let {
            return navigateUp(navController, it)
        }
        return false
    }
}
