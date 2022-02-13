package com.skysoft.nasa.repository

import com.skysoft.nasa.utils.App

class PictureOfTheDayRetrofitImpl {

    fun getRetrofitImpl(): PictureOfTheDayAPI? {
        return App().getRetrofitPictureOfTheDayAPI()
    }
}