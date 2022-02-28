package com.skysoft.nasa.view.mars

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentMarsBinding
import com.skysoft.nasa.utils.setVisibilityForLayout
import com.skysoft.nasa.view.BaseFragment
import com.skysoft.nasa.view.MarsAppState
import com.skysoft.nasa.view.PictureOfTheDayAppState
import com.skysoft.nasa.view.chips.SettingsFragment
import com.skysoft.nasa.view.picture_of_the_day.BottomNavigationDrawerFragment
import com.skysoft.nasa.view.picture_of_the_day.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : BaseFragment<FragmentMarsBinding>(FragmentMarsBinding::inflate) {

    private val viewModel: MarsViewModel by lazy {
        ViewModelProvider(this).get(MarsViewModel::class.java)
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
                    viewModel.sendRequest(getDateForRequestAPOD(0))
                }
                chipYesterday.isChecked -> {
                    viewModel.sendRequest(getDateForRequestAPOD(-1))
                }
                chipDayBeforeYesterday.isChecked -> {
                    viewModel.sendRequest(getDateForRequestAPOD(-2))
                }
            }
        }
    }

    private fun getDateForRequestAPOD(dateShift: Int): String {
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

    private fun renderData(marsAppState: MarsAppState) {
        when (marsAppState) {
            is MarsAppState.Error -> {
                binding.run {
                    showError(marsAppState.error)
                }
            }
            is MarsAppState.Loading -> {
                setVisibility(false, true, false)
            }
            is MarsAppState.Success -> {
                binding.let {
                    setVisibility(false, false, true)
//                    it.included.bottomSheetDescriptionHeader.setText(pictureOfTheDayAppState.serverResponse.title)
//                    it.included.bottomSheetDescription.setText(pictureOfTheDayAppState.serverResponse.explanation)
                    it.imageView.load(marsAppState.serverResponse.url) {
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
        fun newInstance() = MarsFragment()
    }
}