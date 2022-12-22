package com.example.nurseryfinder.Adapter


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Model.Nursery
import com.example.nurseryfinder.NurseryFilteredProfileActivity
import com.example.nurseryfinder.R
import com.squareup.picasso.Picasso


class NurseryAdapter(private var myContext: Context,
                     private var myNursery: List<Nursery>,
                     private var isFragment: Boolean = false): RecyclerView.Adapter<NurseryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NurseryAdapter.ViewHolder {
        return ViewHolder (LayoutInflater.from(myContext).inflate(R.layout.nursery_row_layout, parent, false))

    }

    override fun getItemCount(): Int {
       return myNursery.size
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    override fun onBindViewHolder(holder: NurseryAdapter.ViewHolder, position: Int) {
        val nursery =  myNursery [position]
        holder.nametextview.text = nursery.name
        holder.nomostextview.text = nursery.prefecture
        holder.dimostextview.text = nursery.municipality
        Picasso.get().load(nursery.image).placeholder(R.drawable.ic_action_profile).into(holder.profileimageview)

        //όταν ο χρήστης κάνει κλικ σε ΠΣ από το recyclerview οδηγείται στo προφίλ του σταθμού
        holder.itemView.setOnClickListener{
            Log.d("FilteredListActivity", "nursery ${nursery.name} was clicked")
            val intent = Intent(myContext, NurseryFilteredProfileActivity::class.java)
            //μεταφέρεται το uid του ΠΣ στην NurseryFilteredProfileActivity
            intent.putExtra("profileId", nursery.uid)
            myContext.startActivity(intent)
        }
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var nametextview: TextView = itemView.findViewById(R.id.nurseryname_textview_row)
        var nomostextview: TextView = itemView.findViewById(R.id.nomos_textview_row)
        var dimostextview: TextView = itemView.findViewById(R.id.dimos_textview_row)
        var profileimageview: ImageView = itemView.findViewById(R.id.profilephoto_imageview_row)

    }
}



