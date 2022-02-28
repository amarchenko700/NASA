package com.skysoft.nasa.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skysoft.nasa.R
import com.skysoft.nasa.view.picture_of_the_day.APODFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, APODFragment.newInstance()).commit()
        }

    }
}