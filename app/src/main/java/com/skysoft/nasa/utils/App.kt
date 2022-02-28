package com.skysoft.nasa.utils

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.skysoft.nasa.repository.PictureOfTheDayAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class App : Application() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(NASA_API_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()

    fun getRetrofitPictureOfTheDayAPI(): PictureOfTheDayAPI? {
        return getRetrofit().create(PictureOfTheDayAPI::class.java)
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        appContext = applicationContext
    }

    companion object {
        private var appInstance: App? = null
        private lateinit var appContext: Context

        fun getAppContext(): Context {
            if (appInstance == null)
                throw IllformedLocaleException("Приложение не работает!")
            return appContext
        }

        fun getStringFromResources(idString: Int) = appContext.getString(idString)
    }
}