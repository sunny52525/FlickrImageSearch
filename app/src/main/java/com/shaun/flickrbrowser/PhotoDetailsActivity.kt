package com.shaun.flickrbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {
    private var aboutDialog: AlertDialog?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

        val fab=findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
           showAboutDialog()
        }
        val photo = intent.extras?.getParcelable<Photo>(PHTOTO_TRANSFER) as Photo
        photo_title.text = photo.title
        photo_tags.text = "tags: "+photo.tags
        photo_author.text = photo.author
        Picasso.get().load(photo.link)
            .error(R.drawable.errorimage)
            .placeholder(R.drawable.noimageicon)
            .into(photo_image)


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
