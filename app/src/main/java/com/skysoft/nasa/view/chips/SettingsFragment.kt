package com.skysoft.nasa.view.chips

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentSettingsBinding
import com.skysoft.nasa.utils.*

class SettingsFragment() : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val binding: FragmentSettingsBinding get() = _binding!!

    private val chipChoiceThemeListener = { ->

        setCurrentThemeLocal(NUMBER_CURRENT_THEME)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()

        with(binding) {
            chipThemeLunar.isChecked = false
            chipThemeMartian.isChecked = false
            chipThemeEarth.isChecked = false
            when (NUMBER_CURRENT_THEME) {
                1 -> chipThemeLunar.isChecked = true
                2 -> chipThemeMartian.isChecked = true
                3 -> chipThemeEarth.isChecked = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context: Context =
            ContextThemeWrapper(activity, getThemeForNumber(getCurrentThemeLocal()))
        val localInflater = inflater.cloneInContext(context)
        _binding =
            FragmentSettingsBinding.inflate(localInflater) // здесь inflater созданный нами на месте
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            NUMBER_CURRENT_THEME = savedInstanceState.getInt(KEY_CURRENT_THEME)
        }

        initChipClickListener()
        binding.applyThemeBtn.setOnClickListener {
            requireActivity().recreate()
        }
    }

    private fun initChipClickListener() {
        with(binding) {
            chipThemeLunar.setOnClickListener {
                NUMBER_CURRENT_THEME = THEME_LUNAR
                chipChoiceThemeListener()
            }
            chipThemeMartian.setOnClickListener {
                NUMBER_CURRENT_THEME = THEME_MARTIAN
                chipChoiceThemeListener()
            }
            chipThemeEarth.setOnClickListener {
                NUMBER_CURRENT_THEME = THEME_EARTH
                chipChoiceThemeListener()
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

    override fun onDetach() {
        super.onDetach()
        // Нажали на кнопку назад
        with(requireActivity().supportFragmentManager) {
            NUMBER_CURRENT_THEME.let {
                setFragmentResult(
                    KEY_SETTINGS,
                    Bundle().apply {
                        putInt(
                            KEY_SETTINGS_THEME, it
                        )
                    })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_THEME, NUMBER_CURRENT_THEME)
    }

    private fun setCurrentThemeLocal(currentTheme: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SHARED_PREFERENCE_TAG,
                AppCompatActivity.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME_SP, currentTheme)
        editor.apply()
    }

    private fun getCurrentThemeLocal(): Int {
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                SHARED_PREFERENCE_TAG,
                AppCompatActivity.MODE_PRIVATE
            )
        return sharedPreferences.getInt(KEY_CURRENT_THEME_SP, getDefaultTheme())
    }
}