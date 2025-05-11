package com.tutar.sportsbetapp.presentation.ui.main

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.Window
import android.view.WindowInsets
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tutar.sportsbetapp.R
import com.tutar.sportsbetapp.databinding.ActivityMainBinding
import com.tutar.sportsbetapp.presentation.ui.main.basket.BetBasketBottomSheet
import com.tutar.sportsbetapp.presentation.ui.main.bulletIn.BulletInFragment
import com.tutar.sportsbetapp.presentation.ui.main.profile.ProfileFragment
import com.tutar.sportsbetapp.presentation.viewModel.BetBasketViewModel
import com.tutar.sportsbetapp.presentation.viewModel.SelectedBet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentGame = 0
    private var isSheetVisible = false

    private val betBasketViewModel: BetBasketViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        applyEdgeToEdgeInsets()
        checkForAnimation()
        setStatusBarColor(
            window,
            getColor(R.color.toolbarBackgroundColor)
        )
        setSupportActionBar(binding.customToolbar)
        supportActionBar?.title = getString(R.string.app_name)
        setupCustomBottomBar()
        navigateTo("home")
        updateBottomBarSelection("home")

        val fullText = "${currentGame} maç"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            currentGame.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTotalOdds.text = "0.00"
        binding.tvTotalGames.text = spannable

        binding.clBetBasket.setOnClickListener {
            if (!isSheetVisible) {
                isSheetVisible = true
                BetBasketBottomSheet().apply {
                    show(supportFragmentManager, "BetBasketBottomSheet")
                    this@apply.lifecycleScope.launchWhenResumed {
                        isSheetVisible = false
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                betBasketViewModel.selectedBets.collect { bets ->
                    updateCustomBottomBar(bets)
                }
            }
        }
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

    private fun checkForAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }

    private fun updateCustomBottomBar(bets: List<SelectedBet>) {
        val currentGame = bets.size

        val totalOdds = if (bets.isEmpty()) {
            BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        } else {
            bets.map { BigDecimal.valueOf(it.price) }
                .reduce { acc, odd -> acc.multiply(odd) }
                .setScale(2, RoundingMode.HALF_UP)
        }

        val fullText = "$currentGame maç"
        val spannable = SpannableString(fullText).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                currentGame.toString().length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.tvTotalGames.text = spannable
        binding.tvTotalOdds.text = totalOdds.toPlainString()
    }


    fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)

                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            window.statusBarColor = color
        }
    }

    private fun setupCustomBottomBar() {
        binding.clBottomNav.setOnClickListener(null)

        binding.tvHome.setOnClickListener { navigateTo("home") }
        binding.ivHome.setOnClickListener { navigateTo("home") }

        binding.tvProfile.setOnClickListener { navigateTo("profile") }
        binding.ivProfile.setOnClickListener { navigateTo("profile") }
    }

    private var currentTab: String = "home"

    private fun navigateTo(tab: String) {
        if (tab == currentTab) return

        currentTab = tab
        updateBottomBarSelection(tab)

        val fragment = when (tab) {
            "home" -> BulletInFragment()
            "profile" -> ProfileFragment()
            else -> return
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainNavHost, fragment)
            .commit()
    }

    private fun updateBottomBarSelection(tab: String) {
        binding.tvHome.setTextColor(getColor(R.color.blackColor))
        binding.ivHome.setColorFilter(getColor(R.color.blackColor))

        binding.tvProfile.setTextColor(getColor(R.color.blackColor))
        binding.ivProfile.setColorFilter(getColor(R.color.blackColor))

        when (tab) {
            "home" -> {
                binding.tvHome.setTextColor(getColor(R.color.greenColor))
                binding.ivHome.setColorFilter(getColor(R.color.greenColor))
            }
            "profile" -> {
                binding.tvProfile.setTextColor(getColor(R.color.greenColor))
                binding.ivProfile.setColorFilter(getColor(R.color.greenColor))
            }
        }
    }

    override fun onBackPressed() {
        if (currentTab != "home") {
            navigateTo("home")
        } else {
            super.onBackPressed()
        }
    }
}