package com.skysoft.nasa.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.nasa.R
import com.skysoft.nasa.utils.*
import com.skysoft.nasa.view.picture_of_the_day.APODFragment

class MainActivity : AppCompatActivity() {

    private var currentTheme: Int? = null
    private lateinit var sp : SharedPreferences
    private val setAppTheme = { theme: Int ->
        currentTheme = theme
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences(TAG_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val currentThemeSP = sp.getInt(KEY_CURRENT_THEME_SP, 0)
        if (savedInstanceState != null) {
            savedInstanceState.getInt(KEY_CURRENT_THEME).also { currentTheme = it }
            setTheme(getThemeForNumber(currentTheme))
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setTheme(R.style.NASADark)
            } else if (currentThemeSP != 0) {
                setTheme(getThemeForNumber(currentThemeSP))
            }
        }
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, APODFragment.newInstance(setAppTheme)).commit()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentTheme?.let {
            outState.putInt(KEY_CURRENT_THEME, it)
            sp.edit().putInt(KEY_CURRENT_THEME_SP, it).apply()
        }
    }

}