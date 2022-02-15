package com.skysoft.nasa.view.chips

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.skysoft.nasa.databinding.FragmentChipsBinding
import com.skysoft.nasa.view.BaseFragment

class ChipsFragment : BaseFragment<FragmentChipsBinding>(FragmentChipsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.chipGroup.findViewById<Chip>(checkedId)?.let { it ->
                Toast.makeText(requireContext(), "${it.text} ${checkedId}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.chipEntry.setOnCloseIconClickListener {
            Toast.makeText(requireContext(), "chipEntry close", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChipsFragment()
    }
}