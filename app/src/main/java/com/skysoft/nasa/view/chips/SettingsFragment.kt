package com.skysoft.nasa.view.chips

import android.content.Context
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
import com.skysoft.nasa.view.BaseFragment

class SettingsFragment() : Fragment() {

    private var currentTheme: Int? = null

    private var _binding: FragmentSettingsBinding? = null
    val binding: FragmentSettingsBinding get() = _binding!!

    constructor(themeId:Int):this(){
        currentTheme = themeId
    }

    private val chipChoiceThemeListener = { theme: Int ->
        currentTheme = theme
        setCurrentThemeLocal(theme)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment(currentTheme!!))
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context: Context = ContextThemeWrapper(activity, getThemeForNumber(getCurrentThemeLocal()))
        val localInflater = inflater.cloneInContext(context)
        _binding = FragmentSettingsBinding.inflate(localInflater) // здесь inflater созданный нами на месте
        return binding.root
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

    private fun setCurrentThemeLocal(currentTheme: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(TAG_SHARED_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME_SP, currentTheme)
        editor.apply()
    }

    private fun getCurrentThemeLocal(): Int {
        val sharedPreferences =
            requireActivity().getSharedPreferences(TAG_SHARED_PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME_SP, getDefaultTheme())
    }
}