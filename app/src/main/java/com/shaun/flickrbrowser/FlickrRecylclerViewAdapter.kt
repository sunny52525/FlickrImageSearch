package com.shaun.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.photo_title)

}


class FlickrRecylclerViewAdapter(private var photolist: List<Photo>) :
    RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "FlickRecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        Log.d(TAG, "onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
//        Log.d(TAG,".getItemCount called")
        return if (photolist.isNotEmpty()) photolist.size else 1
    }

    fun loadNewData(newPhotos: List<Photo>) {
        photolist = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photolist.isNotEmpty()) photolist[position] else null
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //called by layout manager (needs existing view)

        if(photolist.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.errorimage)
                holder.title.setText("No Photo Found.\n\nTry Another Keyword")
        }
        else{

            val photoItem = photolist[position]
//        Log.d(TAG,".onBindViewHolder ${photoItem.title} -> $position")
            Picasso.get().load(photoItem.image)
                .error(R.drawable.errorimage)
                .placeholder(R.drawable.noimageicon)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }



    }
}