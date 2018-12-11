package com.testapp.onetwotriptestapplication.ui.search.adapter

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testapp.onetwotriptestapplication.R
import com.testapp.onetwotriptestapplication.ui.search.model.SearchResultModel

/**
 * Created by Ilya Bakerin on 12/7/18.
 */
class SearchResultAdapter(val clickListener: OnSearchResultClickListener) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val items: ArrayList<SearchResultModel> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context)
                .inflate(R.layout.adapter_search_result, p0, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        fun chooseEnding(number: Int, @StringRes form1: Int, @StringRes form2: Int, @StringRes form5: Int): Int {
            var number = number % 100
            return if (number in 11..19) {
                form5
            } else {
                number %= 10
                when (number) {
                    1 -> form1
                    2, 3, 4 -> form2
                    else -> form5
                }
            }
        }

        val resources = p0.itemView.resources

        with(items[p1]) {

            val priceValue = (price ?: 0).plus(flights.asSequence().map { x -> x.price }.min() ?: 0)
            val priceString = String.format(
                resources.getString(
                    if (flights.size == 1) R.string.adapter_search_result_price_single
                    else R.string.adapter_search_result_price_from
                ),
                priceValue
            )

            val flightsString =
                if (flights.size == 0) resources.getString(R.string.adapter_search_result_flights_none)
                else String.format(
                    resources.getString(
                        chooseEnding(
                            flights.size,
                            R.string.adapter_search_result_flights_form1,
                            R.string.adapter_search_result_flights_form2,
                            R.string.adapter_search_result_flights_form5
                        ), flights.size
                    )
                )

            p0.nameView?.text = name
            p0.priceView?.text = priceString
            p0.flightsView?.text = flightsString
        }


    }

    fun setResults(items: List<SearchResultModel>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView? = itemView.findViewById(R.id.adapter_search_result_name_view)
        val flightsView: TextView? = itemView.findViewById(R.id.adapter_search_result_flights_view)
        val priceView: TextView? = itemView.findViewById(R.id.adapter_search_result_price_view)

        init {
            itemView.setOnClickListener {
                val searchResult = items[adapterPosition]
                clickListener.onSearchResultClick(searchResult)
            }
        }
    }

    interface OnSearchResultClickListener {
        fun onSearchResultClick(searchResultModel: SearchResultModel)
    }
}