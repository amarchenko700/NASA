package com.skysoft.nasa.view.picture_of_the_day

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
import com.skysoft.nasa.databinding.FragmentApodBinding
import com.skysoft.nasa.view.BaseFragment
import com.skysoft.nasa.view.PictureOfTheDayAppState
import com.skysoft.nasa.view.chips.SettingsFragment
import java.text.SimpleDateFormat
import java.util.*

class APODFragment() : BaseFragment<FragmentApodBinding>(FragmentApodBinding::inflate) {

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        initChipsAPOD()
        sendRequestAPOD()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appBarFAV -> {
                Toast.makeText(requireContext(), "appBarFAV", Toast.LENGTH_SHORT).show()
            }
            R.id.appBarSettings -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun setVisibilityForLayout(visibility: Boolean, layout: View) {
        if (visibility) {
            layout.visibility = View.VISIBLE
        } else {
            layout.visibility = View.GONE
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

    private fun renderData(pictureOfTheDayAppState: PictureOfTheDayAppState) {
        when (pictureOfTheDayAppState) {
            is PictureOfTheDayAppState.Error -> {
                binding.run {
                    showError(pictureOfTheDayAppState.error)
                }
            }
            is PictureOfTheDayAppState.Loading -> {
                setVisibility(false, true, false)
            }
            is PictureOfTheDayAppState.Success -> {
                binding.let {
                    setVisibility(false, false, true)
//                    it.included.bottomSheetDescriptionHeader.setText(pictureOfTheDayAppState.serverResponse.title)
//                    it.included.bottomSheetDescription.setText(pictureOfTheDayAppState.serverResponse.explanation)
                    it.imageView.load(pictureOfTheDayAppState.serverResponse.url) {
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
        fun newInstance() = APODFragment()
    }
}