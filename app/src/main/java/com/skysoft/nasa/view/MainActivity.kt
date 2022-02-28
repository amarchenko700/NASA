package com.skysoft.nasa.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.ActivityMainBinding
import com.skysoft.nasa.utils.*
import com.skysoft.nasa.view.chips.SettingsFragment
import com.skysoft.nasa.view.earth.EarthFragment
import com.skysoft.nasa.view.mars.MarsFragment
import com.skysoft.nasa.view.picture_of_the_day.APODFragment
import com.skysoft.nasa.view.system.SystemFragment

class MainActivity : AppCompatActivity() {

    private var numberCurrentTheme = 0

    private var _binding: ActivityMainBinding? = null
    val binding: ActivityMainBinding get() = _binding!!

    private var idScreen = 0

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            idScreen = savedInstanceState.getInt(KEY_CURRENT_SCREEN)
        }
        setAppThemeMain(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()

        when (idScreen) {
            0 -> openFragment(APODFragment.newInstance(), FRAGMENT_APOD_TAG)
            1 -> openFragment(EarthFragment.newInstance(), FRAGMENT_EARTH_TAG)
            2 -> openFragment(MarsFragment.newInstance(), FRAGMENT_MARS_TAG)
            3 -> openFragment(SystemFragment.newInstance(), FRAGMENT_SYSTEM_TAG)
            4 -> openFragment(SettingsFragment.newInstance(), FRAGMENT_SETTINGS_TAG, true)
        }
    }

    private fun initBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_view_home -> idScreen = 0
                R.id.bottom_view_earth -> idScreen = 1
                R.id.bottom_view_mars -> idScreen = 2
                R.id.bottom_view_system -> idScreen = 3
                R.id.bottom_view_settings -> idScreen = 4
            }
            openScreen()
        }
    }

    private fun setAppThemeMain(savedInstanceState: Bundle?) {
        sp = getSharedPreferences(SHARED_PREFERENCE_TAG, Context.MODE_PRIVATE)
        val currentThemeSP = sp.getInt(KEY_CURRENT_THEME, 0)
        if (currentThemeSP != 0) {
            numberCurrentTheme = currentThemeSP
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (savedInstanceState != null) {
            numberCurrentTheme =
                savedInstanceState.getInt(KEY_CURRENT_THEME).also { numberCurrentTheme = it }
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setTheme(getThemeForNumber(numberCurrentTheme))
    }

    fun openScreen(): Boolean {
        when (idScreen) {
            0 -> openFragment(APODFragment.newInstance(), FRAGMENT_APOD_TAG)
            1 -> openFragment(EarthFragment(), FRAGMENT_EARTH_TAG)
            2 -> openFragment(MarsFragment(), FRAGMENT_MARS_TAG)
            3 -> openFragment(SystemFragment(), FRAGMENT_SYSTEM_TAG)
            4 -> openFragment(SettingsFragment(), FRAGMENT_SETTINGS_TAG)
            else -> return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_SCREEN, idScreen)
        outState.putInt(KEY_CURRENT_THEME, numberCurrentTheme)
    }

    private fun openFragment(fragment: Fragment, tag: String, addToBackStack: Boolean = false) {
        var fragmentToOpen = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentToOpen == null) {
            fragmentToOpen = fragment
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragmentToOpen, tag)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}