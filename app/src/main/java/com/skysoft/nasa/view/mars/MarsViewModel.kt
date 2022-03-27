package com.skysoft.nasa.view.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skysoft.nasa.BuildConfig
import com.skysoft.nasa.R
import com.skysoft.nasa.repository.PDOError
import com.skysoft.nasa.repository.PDOErrorDetail400
import com.skysoft.nasa.repository.PDOServerResponse
import com.skysoft.nasa.repository.PictureOfTheDayAPI
import com.skysoft.nasa.utils.App
import com.skysoft.nasa.utils.hasInternet
import com.skysoft.nasa.view.MarsAppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MarsViewModel(private val liveAppState: MutableLiveData<MarsAppState> = MutableLiveData()
) : ViewModel() {

    fun getData(): LiveData<MarsAppState> {
        return liveAppState
    }

    private fun sendRequestToNasa(dateRequest: String) {
        liveAppState.postValue(MarsAppState.Loading(null))
        App().getRetrofit()?.let {
            val apodResponse = it.create(PictureOfTheDayAPI::class.java)
            apodResponse.getPictureOfTheDay(BuildConfig.NASA_API_KEY, dateRequest).enqueue(
                object : Callback<PDOServerResponse> {
                    override fun onResponse(
                        call: Call<PDOServerResponse>,
                        response: Response<PDOServerResponse>
                    ) {

                        if (response.isSuccessful && response.body() != null) {
                            response.body()?.let {
                                liveAppState.postValue(MarsAppState.Success(it))
                            }
                        } else {
                            liveAppState.postValue(
                                MarsAppState.Error(
                                    getErrorMessage(response, it)
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<PDOServerResponse>, t: Throwable) {
                        liveAppState.postValue(MarsAppState.Error(t.toString()))
                    }
                }
            )
        }
    }

    private fun getErrorMessage(response: Response<PDOServerResponse>, retrofit: Retrofit): String {
        var errorMessage = "НЛО: не опознная летающая ошибка с позывным код "
        when(response.code()){
            400->{val errorConverter = retrofit.responseBodyConverter<PDOErrorDetail400>(
                PDOErrorDetail400::class.java, arrayOfNulls(0)
            )
                errorConverter.convert(response.errorBody())?.let {
                    errorMessage = it.msg}}
            403->{val errorConverter = retrofit.responseBodyConverter<PDOError>(
                PDOError::class.java, arrayOfNulls(0)
            )
                errorConverter.convert(response.errorBody())?.let {
                    errorMessage = it.error.message}
            }
        }
        return errorMessage
    }

    fun sendRequest(dateRequest: String) {
        if (hasInternet()) {
            sendRequestToNasa(dateRequest)
        } else {
            liveAppState.postValue(MarsAppState.Error(App.getStringFromResources(R.string.not_availability_of_the_internet)))
        }
    }
}