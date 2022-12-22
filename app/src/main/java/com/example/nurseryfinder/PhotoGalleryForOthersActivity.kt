package com.example.nurseryfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Adapter.PhotoAdapterForOthers
import com.example.nurseryfinder.Model.PostedPhoto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PhotoGalleryForOthersActivity : AppCompatActivity() {

    //δημιουργία recyclerView που θα περιέχει το φωτογραφικό υλικό του ΠΣ
    private var recyclerView: RecyclerView? = null
    private var photoAdapterForOthers: PhotoAdapterForOthers? = null
    private var myPhotosForOthers: MutableList<PostedPhoto>? = null
    private lateinit var nUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery_for_others)

        //αποδοχή του uid του σταθμού που επέλεξε ο χρήστης από το NurseryListActivity, ώστε να προβληθεί το φωτο υλικό αυτού του σταθμού
        var nurseryuid = intent.getStringExtra(NurseryProfileActivity.USER_KEY)
        this.nUID = nurseryuid!!

        supportActionBar?.title= "Gallery Φωτογραφιών"

        //σύνδεση του πεδίου recyclerView με το αντίστοιχο layout που δημιουργήσαμε
        recyclerView = findViewById(R.id.recyclerview_photogallery_forOthers)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        //δημιουργία ArrayList όπου θα προστίθεται το φωτο υλικό του χρήστη
        myPhotosForOthers = ArrayList()
        //δημιουργία αντάπτορα για την σύνδεση του παραπάνω ArrayList με το recyclerView και το φωτογραφικό υλικό που αυτό περιέχει
        photoAdapterForOthers = this?.let { PhotoAdapterForOthers(it, myPhotosForOthers as ArrayList<PostedPhoto>, true) }
        recyclerView?.adapter = photoAdapterForOthers

        retrievePhotos()

        // delete_button_photos_presentation.setOnClickListener{
        // deleteImage("myImage"){
        // }

    }


    //η μέθοδος ανακτά το φωτογραφικό υλικό του τρέχοντος ΠΣ
    private fun retrievePhotos(){

        //δημιουργία αναφοράς της βάσης στο node "Photos" του ΠΣ που επιλέχθηκε στο NurseryListActivity
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(nUID).child("Photos")
        ref.addValueEventListener(object: ValueEventListener {

            //προσθήκη του υλικού στο recyclerView και προβολή του
            override fun onDataChange(p0: DataSnapshot) {

                myPhotosForOthers?.clear()

                for (snapshot in p0.children){

                    val photo = snapshot.getValue(PostedPhoto::class.java)


                    if (photo != null){

                        //όλες οι φωτος της βάσης δεδομένων του επιλεγμένου ΠΣ θα αποθηκευτούν μέσα στο myPhotosForOthers Arraylist
                        myPhotosForOthers?.add(photo)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
