package com.example.nurseryfinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nurseryfinder.Model.Nursery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_nursery_list.*
import kotlinx.android.synthetic.main.nursery_row_layout.view.*


class NurseryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_list)

        supportActionBar?.title = "Επιλέξτε Παιδικό Σταθμό"


        retrieveNurseries()

    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    //ανακτά όλους τους παιδικούς σταθμούς που είναι εγγεγραμμένοι στη βάση
    private fun retrieveNurseries(){

        //δημιουργία αναφοράς στη βάση στην τοποθεσία αποθήκευσης των χρηστών (ΠΣ)
        val ref = FirebaseDatabase.getInstance().getReference("/Users")
            .orderByChild("prefecture")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{


            //καλείται κάθε φορά που ανακτούμε όλους τους ΠΣ της βάσης
            //το p0 περιέχει όλα τα δεδομένα
            override fun onDataChange(p0: DataSnapshot) {
                //δημιουργία adapter τύπου GroupAdapter
                val adapter = GroupAdapter<GroupieViewHolder>()

                //προσθήκη κάθε σταθμού της βάσης στο adapter
                p0.children.forEach {
                    val nursery = it.getValue(Nursery::class.java)
                    if (nursery!= null){
                        adapter.add(NurseryItem(nursery))
                    }
                }

                //κάθε φορά που ο χρήστης πατάει πάνω σε κάποιο αντικείμενο (ΠΣ) της λίστας
                //οδηγείται μέσω Intent στο αντίστοιχο προφίλ του ΠΣ
                adapter.setOnItemClickListener {item, view ->

                    val nurseryItem = item as NurseryItem


                    val intent = Intent(view.context, NurseryProfileActivity::class.java)
                    //το userId του σταθμού που επιλέχθηκε μεταφέρεται στο NurseryProfileActivity
                    intent.putExtra(USER_KEY, nurseryItem.nursery.uid)
                    startActivity(intent)
                }


                //σύνδεση του adapter που δημιουργήσαμε με το recyclerview του NurseryListActivity
                recyclerview_nurserylist.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}

//κλάση που χρησιμοποιείται για την ένωση  πληροφοριών recyclerview-στοιχείων ΠΣ
class NurseryItem (val nursery: Nursery): Item<GroupieViewHolder>(){

    //καλείται για κάθε NurseryObject στη λίστα
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.nurseryname_textview_row.text = nursery.name
        viewHolder.itemView.nomos_textview_row.text = nursery.prefecture
        viewHolder.itemView.dimos_textview_row.text = nursery.municipality
        Picasso.get().load(nursery.image).into(viewHolder.itemView.profilephoto_imageview_row)

    }

    //σύνδεση του recyclerview με το layout παρουσίασης των ΠΣ
    override fun getLayout(): Int {
        return R.layout.nursery_row_layout
    }
}






