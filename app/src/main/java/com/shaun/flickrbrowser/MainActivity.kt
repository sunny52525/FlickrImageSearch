package com.shaun.flickrbrowser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.BuildConfig
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity(), GetRawData.OndownloadComplete,
    GetFlickerData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {
    private val TAG = "MainActivity"
        private val flickerRecylclerViewAdapter = FlickrRecylclerViewAdapter(ArrayList())
    private var aboutDialog: AlertDialog?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)

        val fab=findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            showAboutDialog()
        }


        if (isNetworkAvailable(this))
            Toast.makeText(this@MainActivity, "Downloading", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(
                this@MainActivity,
                "Please Connect To Internet and Restart the App",
                Toast.LENGTH_SHORT
            )
                .show()


        }
        val url = createUri(
            "https://www.flickr.com/services/feeds/photos_public.gne",
            "lana+del+ray",
            "en-use",
            true
        )
//        var listener=View.OnClickListener()

        recyler_view.layoutManager = LinearLayoutManager(this)
        recyler_view.addOnItemTouchListener(RecyclerItemClickListener(this, recyler_view, this))

        recyler_view.adapter = flickerRecylclerViewAdapter



        Log.d(TAG, "onCreate ends")
    }


    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, "CreateUri starts")
        return Uri.parse(baseURL).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionMenu Called")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected Called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            R.id.info ->{
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete Called")
            val getFlickerJsonData = GetFlickerData(this)
            getFlickerJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadCompleted failed with status $status . Error msg is $data")
        }
    }



//    companion object{
//        private  val TAG="MainActivity"
//    }
var count=0
    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable Called")
        if(data.size==0 ){

            progress.visibility=View.GONE
            Toast.makeText(this,"No result Found",Toast.LENGTH_SHORT).show()
        }
        else{
            if(count==0){
        Toast.makeText(this@MainActivity, "Download Complete", Toast.LENGTH_SHORT).show()}
            else
                count++
         }
        flickerRecylclerViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.d(TAG, "onError Called with ${exception.message}")

    }



    override fun onItemClick(view: View, postion: Int) {
        Log.d(TAG, "onItemClick Starts")
        val photo = flickerRecylclerViewAdapter.getPhoto(postion)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHTOTO_TRANSFER, photo)
            startActivity(intent)
        }

    }




    override fun onItemLongClick(view: View, postion: Int) {
        Log.d(TAG, "onItemLongClick Starts")

    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    override fun onResume() {
        super.onResume()

        val sharedpref=PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryresult=sharedpref.getString(FLICKR_QUERY,"")
        if(queryresult!!.isNotEmpty())
        {
            Toast.makeText(this, "If Results are not appropriate then its not my fault, its Flickr fault", Toast.LENGTH_SHORT).show()
count=0
            val url = createUri(
                "https://www.flickr.com/services/feeds/photos_public.gne",
                queryresult,
                "en-use",
                true
            )
            val getRawData = GetRawData(this)
            getRawData.execute(url)



        }else
            progress.visibility=View.GONE

    }
    @SuppressLint("InflateParams")
    private fun showAboutDialog(){
        val messgView = layoutInflater.inflate(R.layout.about,null,false)
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        aboutDialog=builder.setView(messgView).create()
        aboutDialog?.setCanceledOnTouchOutside(true)
        val aboutVersion =messgView.findViewById(R.id.about_version) as TextView
        aboutVersion.text = BuildConfig.VERSION_NAME
        aboutDialog?.show()
    }
}


