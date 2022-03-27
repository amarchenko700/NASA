package com.skysoft.nasa.view.chips

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.skysoft.nasa.databinding.FragmentSettingsBinding
import com.skysoft.nasa.utils.*

class SettingsFragment() : Fragment() {

    private var numberCurrentTheme = 0
    private var _binding: FragmentSettingsBinding? = null
    val binding: FragmentSettingsBinding get() = _binding!!

    private val chipChoiceThemeListener = { themeNasa: Int, chip: Chip ->
        numberCurrentTheme = themeNasa
        setChekkedChip()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        numberCurrentTheme = getCurrentThemeLocal()
        val context: Context =
            ContextThemeWrapper(activity, getThemeForNumber(numberCurrentTheme))
        val localInflater = inflater.cloneInContext(context)
        _binding =
            FragmentSettingsBinding.inflate(localInflater) // здесь inflater созданный нами на месте
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            numberCurrentTheme = savedInstanceState.getInt(KEY_CURRENT_THEME)
        }

        initChipClickListener()
        binding.applyThemeBtn.setOnClickListener {
            setCurrentThemeLocal(numberCurrentTheme)
            requireActivity().recreate()
        }
        setChekkedChip()
    }

    private fun setChekkedChip() {
        with(binding) {
            if (chipThemeLunar.isChecked && numberCurrentTheme != 1)
                chipThemeLunar.isChecked = false
            if (chipThemeMartian.isChecked && numberCurrentTheme != 2)
                chipThemeMartian.isChecked = false
            if (chipThemeEarth.isChecked && numberCurrentTheme != 3)
                chipThemeEarth.isChecked = false
            when (numberCurrentTheme) {
                1 -> chipThemeLunar.isChecked = true
                2 -> chipThemeMartian.isChecked = true
                3 -> chipThemeEarth.isChecked = true
            }
        }
    }


    private fun initChipClickListener() {
        with(binding) {
            chipThemeLunar.setOnClickListener {
                chipChoiceThemeListener(THEME_LUNAR, chipThemeLunar)
            }
            chipThemeMartian.setOnClickListener {
                chipChoiceThemeListener(THEME_MARTIAN, chipThemeMartian)
            }
            chipThemeEarth.setOnClickListener {
                chipChoiceThemeListener(THEME_EARTH, chipThemeEarth)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_THEME, numberCurrentTheme)
    }

    private fun setCurrentThemeLocal(currentTheme: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SHARED_PREFERENCE_TAG,
                AppCompatActivity.MODE_PRIVATE
            )
        sharedPreferences.edit().apply{
            this.putInt(KEY_CURRENT_THEME, currentTheme)
            this.apply()
        }

    }

    private fun getCurrentThemeLocal(): Int {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SHARED_PREFERENCE_TAG,
                AppCompatActivity.MODE_PRIVATE
            )
        return sharedPreferences.getInt(KEY_CURRENT_THEME, getDefaultTheme())
    }
}