package com.testapp.onetwotriptestapplication.data.remote.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.testapp.onetwotriptestapplication.data.remote.HotelApi
import com.testapp.onetwotriptestapplication.data.remote.Resource
import com.testapp.onetwotriptestapplication.data.remote.Status
import com.testapp.onetwotriptestapplication.data.remote.model.Hotel
import com.testapp.onetwotriptestapplication.data.remote.model.HotelsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class HotelRepository(private val hotelApi: HotelApi) {
    fun getHotels(): LiveData<Resource<List<Hotel>>> {
        val data = MutableLiveData<Resource<List<Hotel>>>()
        hotelApi.getHotels()
            .enqueue(object : Callback<HotelsResponse> {
                override fun onResponse(call: Call<HotelsResponse>, response: Response<HotelsResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        data.value = Resource(Status.SUCCESS, response.body()!!.hotels, null)
                    } else {
                        data.value = Resource(Status.ERROR, null, "Corrupted response body")
                    }
                }

                override fun onFailure(call: Call<HotelsResponse>, t: Throwable) {
                    data.value = Resource(Status.ERROR, null, t.message)
                }
            })
        return data
    }
}