package com.example.nurseryfinder.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nurseryfinder.EditProfileActivity
import com.example.nurseryfinder.Model.Nursery
import com.example.nurseryfinder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var firebaseUser: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        //ο συνδεδεμένος χρήστης
        firebaseUser = FirebaseAuth.getInstance().currentUser!!


        //αν ο χρήστης επιλέξει "Επεξεργασία" οδηγείται στην EditProfileActivity μέσω Intent
        //προκειμένου να επεξεργαστεί το προφίλ του
        view.editprofile_button_profile_frag.setOnClickListener{
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }
        nurseryInfo()

        return view

    }


    //το προφίλ ενημερώνεται μέσω της βάσης με τα τρέχοντα στοιχεία (που προέκυψαν από αλλαγές ή μη που γίνονται στην "Επεξεργασία Προφίλ")
    private fun nurseryInfo(){

        //δημιουργία αναφοράς της βάσης στον συνδεδεμένο χρήστη
        val nurseryRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        nurseryRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val nursery = p0.getValue<Nursery>(Nursery::class.java)
                    Picasso.get().load(nursery!!.image).placeholder(R.drawable.ic_action_profile).into(view?.profile_image_profile)
                    view?.name_textview_profile_frag?.text = nursery!!.name
                    view?.nomos_spinner_profile?.text = nursery!!.prefecture
                    view?.dimos_spinner_profile?.text = nursery!!.municipality
                    view?.address_textview_profile?.text = nursery!!.address
                    view?.email_textview_profile_frag?.text = nursery!!.email
                    view?.phone_textview_profile_frag?.text = nursery!!.phone
                    view?.activities_textview_profile_frag?.text = nursery!!.activities
                    view?.ages_spinner_profile?.text = nursery!!.ages
                    view?.espa_spinner_profile?.text = nursery!!.espa
                    view?.food_spinner_profile?.text = nursery!!.food
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    //κύκλος ζωής fragment...
    override fun onStop() {
        super.onStop()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onPause() {
        super.onPause()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}
