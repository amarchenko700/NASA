package com.skysoft.nasa.view

import com.skysoft.nasa.databinding.FragmentMarsBinding

class MarsFragment : BaseFragment<FragmentMarsBinding>(FragmentMarsBinding::inflate) {

    companion object {
        fun newInstance() = MarsFragment()
    }
}