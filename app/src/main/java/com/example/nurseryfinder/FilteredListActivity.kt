package com.example.nurseryfinder

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nurseryfinder.Adapter.NurseryAdapter
import com.example.nurseryfinder.Model.Nursery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FilteredListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: NurseryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_list)

        supportActionBar?.title = "Επιλέξτε Παιδικό Σταθμό"


        //γίνεται αποδοχή της φιλτραρισμένης λίστας από την FiltersActivity
        val filteredList = this.intent.getParcelableArrayListExtra<Parcelable>("FilteredList")
        filteredList?.toArrayList()

        if (filteredList != null) {
            for (index: Int in filteredList!!.indices) {
                Log.d("FilteredListActivity", "Transferred list has : ${filteredList.size} items")
            }
        }


        //συνδέεται ο adapter με το recycler view και την φιλτραρισμένη λίστα
        recyclerView = findViewById(R.id.recyclerview_filtered_nurserylist)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)


        userAdapter = this?.let { NurseryAdapter(it, filteredList as ArrayList<Nursery> , true) }
        recyclerView?.adapter = userAdapter
        userAdapter?.notifyDataSetChanged()


    }  // end of onCreate



    // μέθοδος μετατροπής λίστας σε ArrayList
    fun <T> List<T>.toArrayList(): ArrayList<T>{
        return ArrayList(this)
    }


}  //end of class