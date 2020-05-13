package com.shaun.flickrbrowser

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
internal  const val FLICKR_QUERY="FLICKR QUERY"
internal  const val PHTOTO_TRANSFER="PHOTO_TRANSFER"

@SuppressLint("Registered")
open  class BaseActivity:AppCompatActivity() {
    private  val TAG="Base Activity"

    internal fun activateToolbar(enableHome:Boolean){
        Log.d(TAG,"Activate toolbar")
        val toolbar=findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}