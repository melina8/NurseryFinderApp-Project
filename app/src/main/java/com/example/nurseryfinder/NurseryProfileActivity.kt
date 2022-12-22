package com.example.nurseryfinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nurseryfinder.Model.Nursery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_nursery_profile.*


class NurseryProfileActivity : AppCompatActivity() {

    private lateinit var nUID: String
    private val REQUEST_CALL = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nursery_profile)

        //αποδοχή του uid του σταθμού που επέλεξε ο χρήστης από το NurseryListActivity, ώστε να προβληθούν τα στοιχεία αυτού του σταθμού
        var nurseryuid = intent.getStringExtra(NurseryListActivity.USER_KEY)
        this.nUID = nurseryuid!!

        supportActionBar?.title= "Προφίλ Παιδικού Σταθμού"

        //όταν ο χρήστης πατάει το κουμπί "Φωτογραφικό Υλικό" μεταφέρεται στο αντίστοιχο Activity
        photos_button_profile_nursery.setOnClickListener {
            val photoIntent = Intent(this@NurseryProfileActivity, PhotoGalleryForOthersActivity::class.java)
            //μεταφέρουμε το uid του επιλεγμένου χρήστη στην νέα Activity ώστε να προβάλλουμε το φωτογραφικό υλικό του
            photoIntent.putExtra(USER_KEY, nUID)
            startActivity(photoIntent)
        }

        nurseryInfo()

        //οδηγεί στην αποστολή email στον ΠΣ από την συσκευή του χρήστη
        email_icon_nursery.setOnClickListener {

            sendEmail()
        }

        //οδηγεί στην κλήση τηλεφωνικού αριθμού από την συσκευή του χρήστη
        phone_icon_nursery.setOnClickListener {

            makePhoneCall()

        }

    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun sendEmail(){

        //δημιουργία αναφοράς του επιλεγμένου ΠΣ στη βάση
        val user = FirebaseDatabase.getInstance().reference.child("/Users").child(nUID)
        user.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val nursery =  p0.getValue<Nursery>(Nursery::class.java)
                //το email του επιλεγμένου ΠΣ
                val theEmail: String = nursery?.email.toString()
                Log.d("NurseryProfileActivity", theEmail)

                val emailIntent = Intent(Intent.ACTION_SEND)
                val theEmailAddress = arrayOf(theEmail)
                emailIntent.data = Uri.parse("mailto:")
                emailIntent.type= "plain/text"
                //προεπιλογή παραλήπτη και θέματος του email
                emailIntent.putExtra(Intent.EXTRA_EMAIL, theEmailAddress)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ενδιαφέρομαι για τον Παιδικό Σταθμό σας")
                try {
                    //επιλογή εφαρμογής που θα ανοίξει την αποστολή email
                    startActivity(Intent.createChooser(emailIntent, "Choose email client"))
                }
                catch(e:Exception){

                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    private fun makePhoneCall(){

        //δημιουργία αναφοράς του επιλεγμένου ΠΣ στη βάση
        val user = FirebaseDatabase.getInstance().reference.child("/Users").child(nUID)
        user.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val nursery =  p0.getValue<Nursery>(Nursery::class.java)
                //το τηλ του επιλεγμένου ΠΣ
                val thePhone: String = nursery?.phone.toString()
                Log.d("NurseryProfileActivity", thePhone)

                if (thePhone.trim().isNotEmpty()) {
                    //ζητάει πρόσβαση από τη συσκευή για να κάνει κλήση τηλεφωνικού αριθμού
                    if (ContextCompat.checkSelfPermission(
                            this@NurseryProfileActivity,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@NurseryProfileActivity,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            REQUEST_CALL
                        )
                    } else {
                        val dial = "tel:$thePhone"
                        startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }



    //η μέθοδος ενημερώνει το προφίλ του χρήστη με τα τρέχοντα στοιχεία της βάσης
    private fun nurseryInfo(){
        //δημιουργία αναφοράς της βάσης στον ΠΣ που επέλεξε ο χρήστης στο NurseryListActivity
        val nurseryRef = FirebaseDatabase.getInstance().reference.child("Users").child(nUID)

        nurseryRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val nursery = p0.getValue<Nursery>(Nursery::class.java)
                    Picasso.get().load(nursery!!.image).placeholder(R.drawable.ic_action_profile).into(profile_image_profile_nursery)
                    name_textview_profile_nursery?.text = nursery!!.name
                    nomos_spinner_profile_nursery?.text = nursery!!.prefecture
                    dimos_spinner_profile_nursery?.text = nursery!!.municipality
                    address_textview_profile_nursery?.text = nursery!!.address
                    email_textview_profile_nursery?.text = nursery!!.email
                    phone_textview_profile_nursery?.text = nursery!!.phone
                    activities_textview_profile_nursery?.text = nursery!!.activities
                    ages_spinner_profile_nursery?.text = nursery!!.ages
                    espa_spinner_profile_nursery?.text = nursery!!.espa
                    food_spinner_profile_nursery?.text = nursery!!.food
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}



