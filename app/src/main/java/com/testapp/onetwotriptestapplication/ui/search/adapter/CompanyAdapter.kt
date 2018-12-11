package com.testapp.onetwotriptestapplication.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.testapp.onetwotriptestapplication.R
import com.testapp.onetwotriptestapplication.ui.search.model.SearchResultModel

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class CompanyAdapter(context: Context, private val searchResultModel: SearchResultModel) :
    ArrayAdapter<SearchResultModel.FlightData>(context, R.layout.adapter_company, searchResultModel.flights) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.adapter_company, null)
            convertView.tag = ViewHolder(
                convertView!!.findViewById(R.id.adapter_company_name_view) as TextView,
                convertView.findViewById(R.id.adapter_company_price_view) as TextView
            )
        }
        viewHolder = convertView.tag as ViewHolder

        val flightData = getItem(position)
        if (flightData != null) {
            viewHolder.nameView.text = flightData.companyName
            viewHolder.priceView.text = String.format(
                context.getString(R.string.adapter_search_result_company_price),
                ((searchResultModel.price ?: 0) + flightData.price)
            )
        }

        return convertView!!
    }

    inner class ViewHolder(val nameView: TextView, val priceView: TextView)
}