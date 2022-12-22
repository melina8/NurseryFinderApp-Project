package com.example.nurseryfinder.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nurseryfinder.MainActivity
import com.example.nurseryfinder.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_delete_account.view.*


class DeleteAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_delete_account, container, false)
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!

        //με την επιλογή του κουμπιού "Οριστική Διαγραφή Λογαριασμού" διαγράφεται ο τρέχων χρήστης από το Firebase
        // καθώς και όλα τα στοιχεία του από τη βάση δεδομένων
        view.confirmdeletion_button_frag_del.setOnClickListener {
            val mRef = FirebaseDatabase.getInstance().reference.child("Users")
            // Realtime database
            mRef.child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
                .addOnSuccessListener {
                    FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {
                        //με Intent οδηγείται ο χρήστης μετά τη διαγραφή στην MainActivity
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

        }

        return view

    }

}
