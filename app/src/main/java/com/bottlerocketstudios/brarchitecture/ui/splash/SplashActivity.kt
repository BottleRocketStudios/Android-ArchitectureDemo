package com.bottlerocketstudios.brarchitecture.ui.splash

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.ui.BaseActivity
import com.bottlerocketstudios.brarchitecture.ui.auth.LoginActivity
import com.bottlerocketstudios.brarchitecture.ui.home.HomeActivity

class SplashActivity : BaseActivity() {
    private val splashViewModel: SplashActivityViewModel by lazy {
        getProvidedViewModel(SplashActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        DataBindingUtil.setContentView<com.bottlerocketstudios.brarchitecture.databinding.SplashActivityBinding>(this, R.layout.splash_activity).apply {
            viewModel = splashViewModel
            setLifecycleOwner(this@SplashActivity)
            splashViewModel.authenticated.observe(this@SplashActivity, Observer { authenticated ->
                if (authenticated) {
                    startActivity(HomeActivity.newIntent(this@SplashActivity))
                    finish()
                } else {
                    startActivity(LoginActivity.newIntent(this@SplashActivity))
                    finish()
                }
            })
        }
    }
}
