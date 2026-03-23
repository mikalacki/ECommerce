package com.nd.ecommerce.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.nd.ecommerce.R
import com.nd.ecommerce.databinding.ActivityMainBinding
import com.nd.ecommerce.ui.utils.showBottomBarAnimated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        setupBnbVisibilityObserver()
    }

    private fun setupBnbVisibilityObserver() {
        mainViewModel.bnbVisibility.observe(this) {
            binding.bottomNavigation.showBottomBarAnimated(it)
        }
    }
}