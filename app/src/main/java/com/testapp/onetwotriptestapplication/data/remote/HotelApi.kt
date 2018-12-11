package com.testapp.onetwotriptestapplication.data.remote

import com.testapp.onetwotriptestapplication.data.remote.model.HotelsResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Ilya Bakerin on 12/7/18.
 */
interface HotelApi {
    @GET("12q3ws")
    fun getHotels(): Call<HotelsResponse>
}