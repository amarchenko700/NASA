package com.skysoft.nasa.view

import com.skysoft.nasa.repository.PDOServerResponse

sealed class PictureOfTheDayAppState {
    data class Success(val serverResponse: PDOServerResponse) : PictureOfTheDayAppState()
    data class Error(val error: String) : PictureOfTheDayAppState()
    data class Loading(val process: Int?) : PictureOfTheDayAppState()
}