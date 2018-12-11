package com.testapp.onetwotriptestapplication.ui.search.model

/**
 * Created by Ilya Bakerin on 12/7/18.
 */
class SearchResultModel(
    val name: String,
    val price: Int?,
    val flights: ArrayList<FlightData>
) {
    class FlightData(
        val id: String,
        val companyId: String,
        val companyName: String,
        val departure: String,
        val arrival: String,
        val price: Int
    )
}