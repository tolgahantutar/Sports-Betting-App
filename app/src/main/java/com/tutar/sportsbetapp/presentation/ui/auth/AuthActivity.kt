package com.tutar.sportsbetapp.presentation.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        applyEdgeToEdgeInsets()
        setSupportActionBar(binding.customToolbar)
        setupNavigation()
    }

    private fun applyEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.authNavHost) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.tvToolbarTitle.text = getString(R.string.login)
                }

                R.id.signUpFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.tvToolbarTitle.text = getString(R.string.sign_up)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}