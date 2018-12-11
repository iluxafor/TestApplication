package com.testapp.onetwotriptestapplication.data.remote.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.testapp.onetwotriptestapplication.data.remote.*
import com.testapp.onetwotriptestapplication.data.remote.model.Company
import com.testapp.onetwotriptestapplication.data.remote.model.Flight
import com.testapp.onetwotriptestapplication.data.remote.model.Hotel

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class SearchRepository(
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val companyRepository: CompanyRepository
) {

    fun getSearchResult(): LiveData<Resource<SearchResult>> {

        val searchResult = SearchResult()
        val mediatorLiveData = MediatorLiveData<Resource<SearchResult>>()

        val hotelsLiveDataSource = hotelRepository.getHotels()
        val flightsLiveDataSource = flightRepository.getFlights()
        val companiesLiveDataSource = companyRepository.getCompanies()

        fun removeSources() {
            mediatorLiveData.removeSource(hotelsLiveDataSource)
            mediatorLiveData.removeSource(flightsLiveDataSource)
            mediatorLiveData.removeSource(companiesLiveDataSource)
        }

        mediatorLiveData.addSource(hotelsLiveDataSource) { resource ->
            if (resource?.status == Status.SUCCESS) {
                searchResult.hotels = resource.data!!
                if (searchResult.isCompleted)
                    mediatorLiveData.value = Resource(Status.SUCCESS, searchResult, null)
            } else {
                mediatorLiveData.value = Resource(resource?.status ?: Status.ERROR, null, resource?.message)
                removeSources()
            }
        }
        mediatorLiveData.addSource(flightsLiveDataSource) { resource ->
            if (resource?.status == Status.SUCCESS) {
                searchResult.flights = resource.data!!
                if (searchResult.isCompleted)
                    mediatorLiveData.value = Resource(Status.SUCCESS, searchResult, null)
            } else {
                mediatorLiveData.value = Resource(resource?.status ?: Status.ERROR, null, resource?.message)
                removeSources()
            }
        }
        mediatorLiveData.addSource(companiesLiveDataSource) { resource ->
            if (resource?.status == Status.SUCCESS) {
                searchResult.companies = resource.data!!
                if (searchResult.isCompleted)
                    mediatorLiveData.value = Resource(Status.SUCCESS, searchResult, null)
            } else {
                mediatorLiveData.value = Resource(resource?.status ?: Status.ERROR, null, resource?.message)
                removeSources()
            }
        }

        return mediatorLiveData
    }

    class SearchResult {
        var hotels: List<Hotel>? = null
        var flights: List<Flight>? = null
        var companies: List<Company>? = null
        val isCompleted
            get() = hotels != null && flights != null && companies != null
    }
}