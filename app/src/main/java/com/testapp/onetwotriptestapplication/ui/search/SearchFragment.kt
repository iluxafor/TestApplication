package com.testapp.onetwotriptestapplication.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testapp.onetwotriptestapplication.R
import com.testapp.onetwotriptestapplication.ui.common.EmptyAdapter
import com.testapp.onetwotriptestapplication.ui.search.adapter.CompanyAdapter
import com.testapp.onetwotriptestapplication.ui.search.adapter.SearchResultAdapter
import com.testapp.onetwotriptestapplication.ui.search.model.SearchResultModel


class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    private var refreshView: SwipeRefreshLayout? = null
    private var listView: RecyclerView? = null
    private var adapter: SearchResultAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        adapter = SearchResultAdapter(object : SearchResultAdapter.OnSearchResultClickListener {
            override fun onSearchResultClick(searchResultModel: SearchResultModel) {
                AlertDialog.Builder(context!!)
                    .setAdapter(CompanyAdapter(context!!, searchResultModel)) { dialog, whitch -> }
                    .show()
            }
        })

        refreshView = view.findViewById(R.id.fragment_search_refresh_view)
        refreshView?.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        refreshView?.setOnRefreshListener { viewModel.refreshData() }

        listView = view.findViewById(R.id.fragment_search_list_view)
        listView?.layoutManager = LinearLayoutManager(context)
        listView?.adapter = adapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.getData().observe(this, Observer { data ->
            when (data?.status) {
                SearchViewModel.SearchResult.Status.LOADING -> refreshView?.isRefreshing = true
                SearchViewModel.SearchResult.Status.ERROR -> {
                    listView?.adapter = EmptyAdapter(
                        R.drawable.ic_empty_error_outline,
                        R.string.fragment_search_failed_message
                    )
                    refreshView?.isRefreshing = false
                }
                SearchViewModel.SearchResult.Status.SUCCESS -> {
                    if (data.searchResults != null) {
                        if (data.searchResults.isEmpty()) {
                            listView?.adapter = EmptyAdapter(
                                R.drawable.ic_empty_info_outline,
                                R.string.fragment_search_empty_message
                            )
                        } else {
                            if (listView?.adapter != adapter)
                                listView?.adapter = adapter
                        }
                        adapter?.setResults(data.searchResults)
                    }
                    refreshView?.isRefreshing = false
                }
            }
        })
    }
}
