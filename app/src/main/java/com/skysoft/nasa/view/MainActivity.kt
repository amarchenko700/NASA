package com.skysoft.nasa.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.nasa.R
import com.skysoft.nasa.utils.KEY_CURRENT_THEME
import com.skysoft.nasa.utils.THEME_EARTH
import com.skysoft.nasa.utils.THEME_LUNAR
import com.skysoft.nasa.utils.THEME_MARTIAN
import com.skysoft.nasa.view.picture_of_the_day.APODFragment

class MainActivity : AppCompatActivity() {

    private var currentTheme: Int? = null
    private val setAppTheme = { theme: Int ->
        currentTheme = theme
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            savedInstanceState.getInt(KEY_CURRENT_THEME).also { currentTheme = it }
            currentTheme?.let {
                when (currentTheme) {
                    THEME_LUNAR -> setTheme(R.style.Lunar)
                    THEME_MARTIAN -> setTheme(R.style.Martian)
                    THEME_EARTH -> setTheme(R.style.Earth)
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setTheme(R.style.NASADark)
            }
        }
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, APODFragment.newInstance(setAppTheme)).commit()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentTheme?.let { outState.putInt(KEY_CURRENT_THEME, it) }
    }

}