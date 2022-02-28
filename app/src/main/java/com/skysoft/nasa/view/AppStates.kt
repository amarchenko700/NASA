package com.skysoft.nasa.view

import com.skysoft.nasa.repository.PDOServerResponse

sealed class PictureOfTheDayAppState {
    data class Success(val serverResponse: PDOServerResponse) : PictureOfTheDayAppState()
    data class Error(val error: String) : PictureOfTheDayAppState()
    data class Loading(val process: Int?) : PictureOfTheDayAppState()
}

sealed class SystemAppState {
    data class Success(val serverResponse: PDOServerResponse) : SystemAppState()
    data class Error(val error: String) : SystemAppState()
    data class Loading(val process: Int?) : SystemAppState()
}

sealed class MarsAppState {
    data class Success(val serverResponse: PDOServerResponse) : MarsAppState()
    data class Error(val error: String) : MarsAppState()
    data class Loading(val process: Int?) : MarsAppState()
}

sealed class EarthAppState {
    data class Success(val serverResponse: PDOServerResponse) : EarthAppState()
    data class Error(val error: String) : EarthAppState()
    data class Loading(val process: Int?) : EarthAppState()
}