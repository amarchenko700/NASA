package com.skysoft.nasa.view.picture_of_the_day

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentApodBinding
import com.skysoft.nasa.view.BaseFragment
import com.skysoft.nasa.view.PictureOfTheDayAppState

class APODFragment : BaseFragment<FragmentApodBinding>(FragmentApodBinding::inflate) {

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequest()
    }

    private fun renderData(pictureOfTheDayAppState: PictureOfTheDayAppState) {
        when (pictureOfTheDayAppState) {
            is PictureOfTheDayAppState.Error -> {
                binding.run {
                    unavailableServer.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    tvDesriptionError.text = pictureOfTheDayAppState.error
                    root.snackbarWithAction(
                        getString(R.string.Error), getString(R.string.TryAgain), {
                            viewModel.sendRequest()
                        }
                    )
                }
            }
            is PictureOfTheDayAppState.Loading -> {
                binding.let {
                    it.unavailableServer.visibility = View.GONE
                    it.loadingLayout.visibility = View.VISIBLE
                }
            }
            is PictureOfTheDayAppState.Success -> {
                binding.let {
                    it.unavailableServer.visibility = View.GONE
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