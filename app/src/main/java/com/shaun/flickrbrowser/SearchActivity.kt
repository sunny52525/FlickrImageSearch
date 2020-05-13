package com.shaun.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView

class SearchActivity : BaseActivity() {
    private val TAG="Search Activity"
    private var searchView: SearchView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"oncreate starts")
        setContentView(R.layout.activity_search)
        activateToolbar(true)
        Log.d(TAG,"onCreateEnds")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchManager =getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView=menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo= searchManager.getSearchableInfo(componentName)

            searchView?.setSearchableInfo(searchableInfo)
        searchView?.isIconified=false
            searchView?.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {

                   val sharedpref=PreferenceManager.getDefaultSharedPreferences(applicationContext)
                     sharedpref.edit().putString(FLICKR_QUERY,query).apply()
                    searchView?.clearFocus()

                    finish()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

        searchView?.setOnCloseListener {
            finish()
            false
        }




        return true
    }
}
