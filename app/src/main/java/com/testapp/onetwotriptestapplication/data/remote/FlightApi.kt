package com.testapp.onetwotriptestapplication.data.remote

import com.testapp.onetwotriptestapplication.data.remote.model.FlightsResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Ilya Bakerin on 12/7/18.
 */
interface FlightApi {
    @GET("zqxvw")
    fun getFlights(): Call<FlightsResponse>
}