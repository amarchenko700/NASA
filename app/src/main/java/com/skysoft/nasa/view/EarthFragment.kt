package com.skysoft.nasa.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentEarthBinding
import com.skysoft.nasa.databinding.FragmentMarsBinding

class EarthFragment : BaseFragment<FragmentEarthBinding>(FragmentEarthBinding::inflate) {

    companion object {
        fun newInstance() = EarthFragment()
    }
}