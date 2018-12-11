package com.testapp.onetwotriptestapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.testapp.onetwotriptestapplication.ui.search.SearchFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }
}
