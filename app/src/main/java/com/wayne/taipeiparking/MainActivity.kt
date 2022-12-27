package com.wayne.taipeiparking

import android.graphics.Color
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.wayne.taipeiparking.databinding.ActivityMainBinding
import com.wayne.taipeiparking.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBinding: ActivityMainBinding

    private val mAppViewModel: AppViewModel by lazy {
        ViewModelProvider(this)[AppViewModel::class.java]
    }

    private val mNavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container_primary) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)

        Repository.setUpRepository(this)

        mAppViewModel.queryUser().apply {
            observe(this@MainActivity) {
                mNavHostFragment.navController.apply {
                    graph = navInflater.inflate(R.navigation.navigation_primary).apply {
                        setStartDestination(
                            if (it != null) {
                                R.id.fragment_parking
                            } else {
                                R.id.fragment_login
                            }
                        )
                    }
                }

                removeObservers(this@MainActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        window.apply {
            insetsController?.apply {
                hide(WindowInsets.Type.navigationBars())
                setDecorFitsSystemWindows(true)
                isNavigationBarContrastEnforced = false

                statusBarColor = Color.WHITE

                systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                setSystemBarsAppearance(
                    (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS),
                    (WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
                )
            }
        }
    }
}