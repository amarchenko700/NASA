package com.skysoft.nasa.view.earth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentEarthBinding
import com.skysoft.nasa.utils.setVisibilityForLayout
import com.skysoft.nasa.view.BaseFragment
import com.skysoft.nasa.view.EarthAppState
import java.text.SimpleDateFormat
import java.util.*

class EarthFragment : BaseFragment<FragmentEarthBinding>(FragmentEarthBinding::inflate) {

    private val viewModel: EarthViewModel by lazy {
        ViewModelProvider(this).get(EarthViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })

        initChipsAPOD()
        sendRequestAPOD()
    }

    private fun initChipsAPOD() {
        binding.let {
            it.chipToday.isChecked = true
            it.chipToday.setOnClickListener { sendRequestAPOD() }
            it.chipYesterday.setOnClickListener { sendRequestAPOD() }
            it.chipDayBeforeYesterday.setOnClickListener { sendRequestAPOD() }
        }
    }

    private fun sendRequestAPOD() {
        with(binding) {
            when {
                chipToday.isChecked -> {
                    viewModel.sendRequest(getDateForRequest(0))
                }
                chipYesterday.isChecked -> {
                    viewModel.sendRequest(getDateForRequest(-1))
                }
                chipDayBeforeYesterday.isChecked -> {
                    viewModel.sendRequest(getDateForRequest(-2))
                }
            }
        }
    }

    private fun getDateForRequest(dateShift: Int): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        calendar.add(Calendar.DATE, dateShift)
        return formatter.format(calendar.time)
    }

    private fun setVisibility(isError: Boolean, isLoading: Boolean, isSuccess: Boolean) {
        binding.let {
            setVisibilityForLayout(isError, it.unavailableServer)
            setVisibilityForLayout(isLoading, it.loadingLayout)
            setVisibilityForLayout(isSuccess, it.serverData)
        }
    }

    private fun showError(error: String) {
        binding.let {
            setVisibility(true, false, false)
            it.tvDesriptionError.setText(error)
            it.root.snackbarWithAction(
                getString(R.string.Error), getString(R.string.try_again), {
                    sendRequestAPOD()
                }
            )
        }
    }

    private fun renderData(earthAppState: EarthAppState) {
        when (earthAppState) {
            is EarthAppState.Error -> {
                binding.run {
                    showError(earthAppState.error)
                }
            }
            is EarthAppState.Loading -> {
                setVisibility(false, true, false)
            }
            is EarthAppState.Success -> {
                binding.let {
                    setVisibility(false, false, true)
//                    it.included.bottomSheetDescriptionHeader.setText(pictureOfTheDayAppState.serverResponse.title)
//                    it.included.bottomSheetDescription.setText(pictureOfTheDayAppState.serverResponse.explanation)
                    it.imageView.load(earthAppState.serverResponse.url) {
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
        }
    }

    private fun View.snackbarWithAction(
        textSnackbar: String, textAction: String, funAction: () -> Unit
    ) {
        Snackbar.make(this, textSnackbar, Snackbar.LENGTH_LONG)
            .setAction(textAction) {
                funAction()
            }.show()
    }

    companion object {
        fun newInstance() = EarthFragment()
    }
}