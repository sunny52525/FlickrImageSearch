package com.shaun.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK,IDLE,NOT_INITIALISED ,FAILED_OR_EMPTY ,PERMISSION_ERROR ,ERROR
}
class GetRawData (private  val listener:OndownloadComplete) : AsyncTask<String,Void,String>(){
    private  val TAG="GetRawData"
    private  var downloadStatus =DownloadStatus.IDLE

    interface OndownloadComplete{
        fun onDownloadComplete(data:String,status: DownloadStatus)
    }

    override fun onPostExecute(result: String) {
       Log.d(TAG,"onPostExecute")
        listener.onDownloadComplete(result,downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
   if(params[0]== null) {
       downloadStatus=DownloadStatus.NOT_INITIALISED
        return  "no URL specified"
   }
        try {

            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }catch (e:Exception){
            val errorMessage = when (e){
                is MalformedURLException ->{ downloadStatus=DownloadStatus.NOT_INITIALISED
                "doInBackground:Invalid URL ${e.message}"
                }
                is IOException ->{ downloadStatus=DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground:IO Exception ${e.message}"
                }
                is SecurityException ->{ downloadStatus=DownloadStatus.PERMISSION_ERROR
                    "doInBackground:Security Exception : Give the Goddamn permission ${e.message}"
                }
                else
                    ->{

                    downloadStatus=DownloadStatus.ERROR
                    "Unkown Error : ${e.message}"

                }
            }
            Log.e(TAG,errorMessage)
            return  errorMessage
        }
    }
}