package com.testapp.onetwotriptestapplication.data.remote.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.testapp.onetwotriptestapplication.data.remote.FlightApi
import com.testapp.onetwotriptestapplication.data.remote.Resource
import com.testapp.onetwotriptestapplication.data.remote.Status
import com.testapp.onetwotriptestapplication.data.remote.model.Flight
import com.testapp.onetwotriptestapplication.data.remote.model.FlightsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class FlightRepository(private val flightApi: FlightApi) {
    fun getFlights(): LiveData<Resource<List<Flight>>> {
        val data = MutableLiveData<Resource<List<Flight>>>()
        flightApi.getFlights()
            .enqueue(object : Callback<FlightsResponse> {
                override fun onResponse(call: Call<FlightsResponse>, response: Response<FlightsResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        data.value = Resource(Status.SUCCESS, response.body()!!.flights, null)
                    } else {
                        data.value = Resource(Status.ERROR, null, "Corrupted response body")
                    }
                }

                override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                    data.value = Resource(Status.ERROR, null, t.message)
                }
            })
        return data
    }
}