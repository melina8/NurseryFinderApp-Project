package com.example.nurseryfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Adapter.PhotoAdapter
import com.example.nurseryfinder.Model.PostedPhoto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class PhotosGalleryActivity : AppCompatActivity(), PhotoAdapter.OnItemClickListener {

    //δημιουργία recyclerView που θα περιέχει το φωτογραφικό υλικό του ΠΣ
    private var recyclerView: RecyclerView? = null
    private var photoAdapter: PhotoAdapter? = null
    private var myPhotos: MutableList<PostedPhoto>? = null
    private lateinit var firebaseUser: FirebaseUser
    private  var myStorage = FirebaseStorage.getInstance()
    private  var myDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Photos")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_gallery)

        supportActionBar?.title = "Gallery"

        //ο συνδεδεμένος χρήστης
        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        //σύνδεση του πεδίου recyclerView με το αντίστοιχο layout που δημιουργήσαμε
        recyclerView = findViewById(R.id.recyclerview_photogallery)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        //δημιουργία ArrayList όπου θα προστίθεται το φωτο υλικό του χρήστη
        myPhotos = ArrayList()
        //δημιουργία αντάπτορα για την σύνδεση του παραπάνω ArrayList με το recyclerView και το φωτογραφικό υλικό που αυτό περιέχει
        photoAdapter = this?.let { PhotoAdapter(it, myPhotos as ArrayList<PostedPhoto>, true, this) }
        recyclerView?.adapter = photoAdapter

        photoAdapter?.setOnItemClickListener(this@PhotosGalleryActivity)

        retrievePhotos()

    }

    //γίνεται implementation μεθόδου του interface που ορίζει τι συμβαίνει όταν ο χρήστης κάνει κλικ στη φωτό
    override fun onItemClick(position: Int) {
        Log.d("PhotoGalleryActivity", "Item $position was clicked")
        val clickedItem = (myPhotos as ArrayList<PostedPhoto>)[position]

        photoAdapter?.notifyItemChanged(position)
    }

    //γίνεται implementation μεθόδου του interface που διαγράφει φωτογραφία από την ΒΔ και το storage του firebase
   override  fun onDeleteClick(position: Int) {
       val selectedItem: PostedPhoto = myPhotos!!.get(position)
        val selectedKey: String = selectedItem.getPhotoId()
        val imageRef = myStorage.getReferenceFromUrl(selectedItem.getPostImage())
        imageRef.delete().addOnSuccessListener {
            myDatabaseRef.child(selectedKey).removeValue()
            val intent = Intent(this@PhotosGalleryActivity, AddPhotosActivity:: class.java)
            startActivity(intent)

            Toast.makeText(this@PhotosGalleryActivity, "Η φωτογραφία διαγράφηκε", Toast.LENGTH_SHORT).show()

        }
    }


    //η μέθοδος ανακτά το φωτογραφικό υλικό του συνδεδεμένου χρήστη (ΠΣ)
    private fun retrievePhotos(){

        //δημιουργία αναφοράς της βάσης στο node "Photos" του συνδεδεμένου ΠΣ
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid).child("Photos")

        ref.addValueEventListener(object: ValueEventListener {

            //προσθήκη του υλικού στο recyclerView και προβολή του
            override fun onDataChange(p0: DataSnapshot) {

                Log.d("PhotosGalleryActivity", "Showing photos in recycler view...")
                myPhotos?.clear()

                for (snapshot in p0.children){

                    val photo = snapshot.getValue(PostedPhoto::class.java)
                    //val  currentPhoto = photo?.getPhotoId()

                    if (photo != null){

                        //όλες οι φωτος της βάσης δεδομένων του συνδεδεμένου ΠΣ θα αποθηκευτούν μέσα στο myPhotos Arraylist
                        myPhotos?.add(photo)

                    }
                }

            }


            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }




}
