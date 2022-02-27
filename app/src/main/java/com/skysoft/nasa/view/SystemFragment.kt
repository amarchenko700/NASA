package com.skysoft.nasa.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentApodBinding
import com.skysoft.nasa.databinding.FragmentSystemBinding

class SystemFragment : BaseFragment<FragmentSystemBinding>(FragmentSystemBinding::inflate) {
    companion object {
        fun newInstance() = SystemFragment()
    }
}