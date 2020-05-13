package com.shaun.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickerData (private val listener :OnDataAvailable): AsyncTask<String,Void,ArrayList<Photo>>(){
    private  val TAG="GetFlickerJsonData"
    interface OnDataAvailable{
        fun onDataAvailable(data:List<Photo>)
        fun onError(exception: Exception)
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG,"onPostExecute Starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG,"onPostExecute Ends")
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.d(TAG,"doInBackground running")
        val photoList =ArrayList<Photo>()
        try {
            val jsonData=JSONObject(params[0])
            val itemsArray=jsonData.getJSONArray("items")
            for (i in 0 until itemsArray.length()){
                val jsonPhoto= itemsArray.getJSONObject(i)
                val title =jsonPhoto.getString("title")
                val author =jsonPhoto.getString("author")
                val authorId =jsonPhoto.getString("author_id")
                var tags =jsonPhoto.getString("tags")
                val jsonMedia =jsonPhoto.getJSONObject("media")
                val photoUrl =jsonMedia.getString("m")
                val date =jsonPhoto.getString("date_taken")
                val link =photoUrl.replaceFirst("_m.jpg","_b.jpg")
//                val date=jsonPhoto.getString("date_taken")


                val photoObject=Photo(title,author,authorId,link,tags,photoUrl,date)
                photoList.add(photoObject)
                Log.d(TAG,"doInBackground $photoObject")
            }
        }catch (e:JSONException){
            e.printStackTrace()
            Log.e(TAG,"doInBackground:Error Processing Jscn data  ${e.message}")
            cancel(true)
//            Log.d(TAG,"FUCKED")
            listener.onError(e)
        }
        Log.d(TAG,"doInBackground Ends")
        return photoList
    }
}