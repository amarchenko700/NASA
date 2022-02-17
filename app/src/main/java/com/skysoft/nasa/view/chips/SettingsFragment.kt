package com.skysoft.nasa.view.chips

import android.os.Bundle
import android.view.View
import com.skysoft.nasa.databinding.FragmentSettingsBinding
import com.skysoft.nasa.utils.*
import com.skysoft.nasa.view.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private var currentTheme: Int? = null

    private val chipChoiceThemeListener = { theme: Int ->
        currentTheme = theme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState!=null){
            currentTheme = savedInstanceState.getInt(KEY_CURRENT_THEME)
        }

        with(binding) {
            chipThemeLunar.setOnClickListener {
                chipChoiceThemeListener(THEME_LUNAR)
            }
            chipThemeMartian.setOnClickListener {
                chipChoiceThemeListener(THEME_MARTIAN)
            }
            chipThemeEarth.setOnClickListener {
                chipChoiceThemeListener(THEME_EARTH)
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onDetach() {
        super.onDetach()
        // Нажали на кнопку назад
        currentTheme?.let {
            requireActivity().supportFragmentManager.setFragmentResult(
                KEY_SETTINGS,
                Bundle().apply {
                    putInt(
                        KEY_SETTINGS_THEME, it
                    )
                })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentTheme?.let { outState.putInt(KEY_CURRENT_THEME, it) }

    }
}