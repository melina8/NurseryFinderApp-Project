package com.example.nurseryfinder.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Model.PostedPhoto
import com.example.nurseryfinder.R
import com.squareup.picasso.Picasso

class PhotoAdapterForOthers (private var myContext: Context,
                             private var myPhotos: List<PostedPhoto>,
                             private var isFragment: Boolean = false): RecyclerView.Adapter<PhotoAdapterForOthers.ViewHolder>() {



    // private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.photos_presentation_for_others_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myPhotos.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //firebaseUser = FirebaseAuth.getInstance().currentUser

        val photos =  myPhotos [position]
        holder.descriptionText.text = photos.getDescription()
        Picasso.get().load(photos.getPostImage()).into(holder.postImage)


    }



    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){


        var postImage: ImageView = itemView.findViewById(R.id.post_image_photos_presentation_for_others)
        var descriptionText: TextView = itemView.findViewById(R.id.description_photos_presentation_for_others)

    }
}


