package com.testapp.onetwotriptestapplication.data.remote.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.testapp.onetwotriptestapplication.data.remote.CompanyApi
import com.testapp.onetwotriptestapplication.data.remote.Resource
import com.testapp.onetwotriptestapplication.data.remote.Status
import com.testapp.onetwotriptestapplication.data.remote.model.CompaniesResponse
import com.testapp.onetwotriptestapplication.data.remote.model.Company
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class CompanyRepository(private val companyApi: CompanyApi) {
    fun getCompanies(): LiveData<Resource<List<Company>>> {
        val data = MutableLiveData<Resource<List<Company>>>()
        companyApi.getCompanies()
            .enqueue(object : Callback<CompaniesResponse> {
                override fun onResponse(call: Call<CompaniesResponse>, response: Response<CompaniesResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        data.value = Resource(Status.SUCCESS, response.body()!!.companies, null)
                    } else {
                        data.value = Resource(Status.ERROR, null, "Corrupted response body")
                    }
                }

                override fun onFailure(call: Call<CompaniesResponse>, t: Throwable) {
                    data.value = Resource(Status.ERROR, null, t.message)
                }
            })
        return data
    }
}