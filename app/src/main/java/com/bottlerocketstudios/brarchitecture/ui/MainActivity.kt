package com.bottlerocketstudios.brarchitecture.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bottlerocketstudios.brarchitecture.BitbucketApplication
import com.bottlerocketstudios.brarchitecture.R
import com.bottlerocketstudios.brarchitecture.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProviders.of(this, (application as BitbucketApplication).factory).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            viewModel = activityViewModel
        }
    }
}