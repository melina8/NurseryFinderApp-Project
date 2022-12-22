 package com.example.nurseryfinder

 import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
 import android.util.Log
 import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //όταν ο χρήστης πατήσει το κουμπί "ΕΓΓΡΑΦΗ" καλείται η createAccount()
        register_button_register.setOnClickListener {

            createAccount()
        }

        //αν ο χρήστης έχει ήδη λογαριασμό επιλέγει να κάνει είσοδο
        alreadyhaveaccount_button_register.setOnClickListener {
            Log.d("RegisterActivity", "Show Login...")
            val intent = Intent( this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    //δημιουργεί έναν καινούργιο λογαριασμό χρήστη (ιδιοκτήτη ΠΣ)
    private fun createAccount(){
        val name = name_edittext_register.text.toString()
        val phone = phone_edittext_register.text.toString()
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        //αν κάποιο από τα παραπάνω πεδία δεν συμπληρωθεί από τον χρήστη εμφανίζεται κατάλληλο μήνυμα
        when{
            TextUtils.isEmpty(name) ->
                Toast.makeText(this, "Το όνομα του ΠΣ πρέπει να συμπληρωθεί", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(phone) ->
                Toast.makeText(this, "Το τηλέφωνο του ΠΣ πρέπει να συμπληρωθεί", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) ->
                Toast.makeText(this, "Η διεύθυνση email πρέπει να συμπληρωθεί", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) ->
                Toast.makeText(this, "Το password πρέπει να συμπληρωθεί", Toast.LENGTH_LONG).show()

            else ->{
                val progressDialog = ProgressDialog(this@RegisterActivity)
                progressDialog.setTitle("Δημιουργία Λογαριασμού")
                progressDialog.setMessage("παρακαλώ περιμένετε...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                //δημιουργία στιγμιοτύπου αυθεντικοποίησης
                val myAuth: FirebaseAuth = FirebaseAuth.getInstance()
                //δημιουργία λογαριασμού με email και password
                myAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            saveUserInfo(name, phone, email, password)
                        }

                        else  {
                            val message = it.exception!!.toString()
                            Toast.makeText(this@RegisterActivity, "Error: $message", Toast.LENGTH_LONG).show()
                            myAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }

    //αποθήκευση των δεδομένων του χρήστη
    private fun saveUserInfo(name:String, phone:String, email:String, password:String){

        //δημιουργία αναγνωριστικού του τρέχοντος χρήστη
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        //δημιουργία αναφοράς στη βάση για την αποθήκευση των δεδομένων όλων των χρηστών
        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        //αποθήκευση των δεδομένων του κάθε χρήστη στη βάση σε HashMap
        val userMap = HashMap<String, Any>()
        userMap["uid"] =  currentUserID
        userMap ["name"] =  name
        userMap ["phone"] =  phone
        userMap ["email"] =  email.toLowerCase()
        userMap ["activities"] = ""
        userMap ["prefecture"] = "Επιλέξτε Νομό"
        userMap ["municipality"] = "Επιλέξτε Δήμο"
        userMap ["address"] = ""
        userMap ["ages"] = "Επιλέξτε Ηλικία"
        userMap ["espa"] = "Επιλέξτε ΕΣΠΑ"
        userMap ["food"] = "Επιλέξτε Φαγητό"
        userMap ["image"] =  "https://firebasestorage.googleapis.com/v0/b/nursery-finder-e6e13.appspot.com/o/Default%20Images%2Fic_action_profile.png?alt=media&token=1b9b6e22-64fb-4d3e-9ee6-91142cec0de0"


        ref.child(currentUserID).setValue(userMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Ο λογαριασμός δημιουργήθηκε επιτυχώς", Toast.LENGTH_LONG).show()
                    //μεταφορά στην NavigationActivity μετά την επιτυχή δημιουργία λογαριασμού
                    val intent = Intent (this@RegisterActivity, NavigationActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                }
                else {
                    val message = it.exception!!.toString()
                    Toast.makeText(this, "error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()

                }
            }
    }
}
