package com.testapp.onetwotriptestapplication.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.testapp.onetwotriptestapplication.data.remote.CompanyApi
import com.testapp.onetwotriptestapplication.data.remote.FlightApi
import com.testapp.onetwotriptestapplication.data.remote.HotelApi
import com.testapp.onetwotriptestapplication.data.remote.Status
import com.testapp.onetwotriptestapplication.data.remote.model.Company
import com.testapp.onetwotriptestapplication.data.remote.model.Flight
import com.testapp.onetwotriptestapplication.data.remote.model.Hotel
import com.testapp.onetwotriptestapplication.data.remote.repository.CompanyRepository
import com.testapp.onetwotriptestapplication.data.remote.repository.FlightRepository
import com.testapp.onetwotriptestapplication.data.remote.repository.HotelRepository
import com.testapp.onetwotriptestapplication.data.remote.repository.SearchRepository
import com.testapp.onetwotriptestapplication.ui.search.model.SearchResultModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel : ViewModel() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.myjson.com/bins/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val data = MediatorLiveData<SearchResult>()

    fun getData(): LiveData<SearchResult> {
        return data
    }

    fun refreshData() {
        fetchData()
    }

    private fun mergeData(hotels: List<Hotel>, flights: List<Flight>, companies: List<Company>):
            ArrayList<SearchResultModel> {

        val flightsIdsMap = hashMapOf<String, Flight>()
        flights.forEach { flight ->
            flightsIdsMap[flight.id] = flight
        }

        val companiesIdsMap = hashMapOf<String, Company>()
        companies.forEach { company ->
            companiesIdsMap[company.id] = company
        }

        val searchResults = ArrayList<SearchResultModel>()
        hotels.forEach { hotel ->
            val hotelFlights = ArrayList<SearchResultModel.FlightData>()
            hotel.flights.forEach { flightId ->
                val flight = flightsIdsMap[flightId]
                val company = companiesIdsMap[flight?.companyId]
                if (flight != null && company != null) {
                    hotelFlights.add(
                        SearchResultModel.FlightData(
                            flight.id, flight.companyId, company.name, flight.departure, flight.arrival, flight.price
                        )
                    )
                }
            }

            searchResults.add(SearchResultModel(hotel.name, hotel.price, hotelFlights))
        }

        return searchResults
    }

    private fun fetchData() {

        val repository = SearchRepository(
            HotelRepository(retrofit.create(HotelApi::class.java)),
            FlightRepository(retrofit.create(FlightApi::class.java)),
            CompanyRepository(retrofit.create(CompanyApi::class.java))
        )

        data.value = SearchResult(SearchResult.Status.LOADING, null)
        data.addSource(repository.getSearchResult()) { resource ->
            if (resource != null && resource.status == Status.SUCCESS) {
                val mergedData = mergeData(
                    resource.data!!.hotels!!,
                    resource.data.flights!!,
                    resource.data.companies!!
                )
                data.value = SearchResult(SearchResult.Status.SUCCESS, mergedData)
            } else {
                data.value = SearchResult(SearchResult.Status.ERROR, null)
            }
        }
    }

    init {
        fetchData()
    }

    class SearchResult(val status: Status, val searchResults: List<SearchResultModel>?) {
        enum class Status {
            SUCCESS,
            ERROR,
            LOADING
        }
    }
}
