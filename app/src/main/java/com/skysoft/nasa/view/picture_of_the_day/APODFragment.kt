package com.skysoft.nasa.view.picture_of_the_day

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.skysoft.nasa.R
import com.skysoft.nasa.databinding.FragmentApodBinding
import com.skysoft.nasa.utils.*
import com.skysoft.nasa.view.BaseFragment
import com.skysoft.nasa.view.MainActivity
import com.skysoft.nasa.view.PictureOfTheDayAppState
import com.skysoft.nasa.view.chips.SettingsFragment
import java.text.SimpleDateFormat
import java.util.*

class APODFragment (): BaseFragment<FragmentApodBinding>(FragmentApodBinding::inflate),
    FragmentResultListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }
    private var isMain = true
    private var setAppTheme = {theme:Int->}

    constructor(setAppThemeFun: (theme: Int) -> Unit):this(){
        setAppTheme = setAppThemeFun
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })

        requireActivity().supportFragmentManager.setFragmentResultListener(
            KEY_SETTINGS, viewLifecycleOwner,
            this
        )

        sendRequestAPOD()

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        initChipsAPOD()
        initBottomSheetBehavior()
        initFAB()

        (requireActivity() as MainActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
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

    private fun initFAB() {
        binding.let { b ->
            b.fab.setOnClickListener {
                if (isMain) {
                    b.bottomAppBar.navigationIcon = null
                    b.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    b.fab.setImageResource(R.drawable.ic_back_fab)
                    b.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                } else {
                    b.bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                    b.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    b.fab.setImageResource(R.drawable.ic_plus_fab)
                    b.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                }
                isMain = !isMain
            }
        }
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING ->
                        Toast.makeText(requireContext(), "STATE_DRAGGING", Toast.LENGTH_SHORT)
                            .show()
                    BottomSheetBehavior.STATE_COLLAPSED ->
                        Toast.makeText(requireContext(), "STATE_COLLAPSED", Toast.LENGTH_SHORT)
                            .show()
                    BottomSheetBehavior.STATE_EXPANDED ->
                        Toast.makeText(requireContext(), "STATE_EXPANDED", Toast.LENGTH_SHORT)
                            .show()
                    BottomSheetBehavior.STATE_HALF_EXPANDED ->
                        Toast.makeText(requireContext(), "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT)
                            .show()
                    BottomSheetBehavior.STATE_HIDDEN ->
                        Toast.makeText(requireContext(), "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                    BottomSheetBehavior.STATE_SETTLING ->
                        Toast.makeText(requireContext(), "STATE_SETTLING", Toast.LENGTH_SHORT)
                            .show()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("mylogs", "slideOffset $slideOffset")
            }

        })
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
                    it.included.bottomSheetDescriptionHeader.setText(pictureOfTheDayAppState.serverResponse.title)
                    it.included.bottomSheetDescription.setText(pictureOfTheDayAppState.serverResponse.explanation)
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
        fun newInstance(setAppTheme: (theme: Int) -> Unit) = APODFragment(setAppTheme)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        if(requestKey == KEY_SETTINGS){
            setAppTheme(result.getInt(KEY_SETTINGS_THEME))
        }
    }
}