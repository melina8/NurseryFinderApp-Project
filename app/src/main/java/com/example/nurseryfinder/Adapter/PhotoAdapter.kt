package com.example.nurseryfinder.Adapter

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Model.PostedPhoto
import com.example.nurseryfinder.R
import com.squareup.picasso.Picasso

class PhotoAdapter (private var myContext: Context,
                    private var myPhotos: List<PostedPhoto>,
                    private var isFragment: Boolean = false,
                    private var myListener: OnItemClickListener
): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.photos_presentation_layout, parent, false)
        return ViewHolder(view)


    }

    override fun getItemCount(): Int {
        return myPhotos.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentPhoto =  myPhotos [position]
        holder.descriptionText.text = currentPhoto.getDescription()
        Picasso.get().load(currentPhoto.getPostImage()).into(holder.postImage)

        //όταν ο χρήστης επιλέξει το εικονίδιο "x" εμφανίζεται μενού με επιλογές: Διαγραφή κ Ακύρωση
        holder.deleteImage.setOnClickListener{
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.options_menu)


            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

                //όταν ο χρήστης κάνει κλικ στην επιλογή "Διαγραφή" του popup menu η φωτογραφία διαγράφεται
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    if (position != RecyclerView.NO_POSITION) {
                        when (item.getItemId()) {
                            R.id.item1 -> {
                                myListener.onDeleteClick(position)
                                return true
                            }
                            else -> return false
                        }
                    }
                    return false
                }

            })
            popup.show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener  /*, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener */{

        var postImage: ImageView = itemView.findViewById(R.id.post_image_photos_presentation)
        var descriptionText: TextView = itemView.findViewById(R.id.description_photos_presentation)
        var deleteImage: ImageButton =  itemView.findViewById(R.id.delete_button_photos_presentation)

        init {
            itemView.setOnClickListener(this)
             }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                myListener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

}


