package com.testapp.onetwotriptestapplication.data.remote

import com.testapp.onetwotriptestapplication.data.remote.model.CompaniesResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Ilya Bakerin on 12/8/18.
 */
interface CompanyApi {
    @GET("8d024")
    fun getCompanies(): Call<CompaniesResponse>
}