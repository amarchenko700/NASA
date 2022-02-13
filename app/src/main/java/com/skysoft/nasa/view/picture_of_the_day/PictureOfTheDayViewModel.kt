package com.skysoft.nasa.view.picture_of_the_day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.nasa.BuildConfig
import com.skysoft.nasa.repository.PDOServerResponse
import com.skysoft.nasa.utils.App
import com.skysoft.nasa.view.PictureOfTheDayAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PictureOfTheDayViewModel(
    private val liveAppState: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData()
) : ViewModel() {
    fun getData(): LiveData<PictureOfTheDayAppState> {
        return liveAppState
    }

    fun sendRequest() {
        liveAppState.postValue(PictureOfTheDayAppState.Loading(null))
        App().getRetrofit()?.let {
            it.getPictureOfTheDay(BuildConfig.NASA_API_KEY).enqueue(
                object : Callback<PDOServerResponse> {
                    override fun onResponse(
                        call: Call<PDOServerResponse>,
                        response: Response<PDOServerResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.let {
                                liveAppState.postValue(PictureOfTheDayAppState.Success(it))
                            }
                        } else {
                            liveAppState.postValue(
                                PictureOfTheDayAppState.Error(
                                    response.errorBody().toString()
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<PDOServerResponse>, t: Throwable) {
                        liveAppState.postValue(PictureOfTheDayAppState.Error(t.toString()))
                    }
                }
            )
        }
    }
}