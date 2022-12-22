package com.example.nurseryfinder

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //αν ο χρήστης δεν διαθέτει λογαριασμό επιστρέφει στην RegisterActivity
        noaccount_button_login.setOnClickListener {
            Log.d("LoginActivity", "Show Register")
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }
        //αν ο χρήστης επιλέξει "ΕΙΣΟΔΟΣ" καλείται η userLogin()
        login_button_login.setOnClickListener {
            Log.d("LoginActivity", "Show Home page")
            userLogin()

        }
    }

    //πραγματοποιείται είσοδος του χρήστη στο λογαριασμό του
    private fun userLogin(){
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        //αν δεν συμπληρωθούν τα απαραίτητα πεδία εμφανίζεται κατάλληλο μήνυμα
        when {
            TextUtils.isEmpty(email) ->
                Toast.makeText(this, "Παρακαλώ συμπληρώστε το email", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) ->
                Toast.makeText(this, "Παρακαλώ συμπληρώστε το password", Toast.LENGTH_LONG).show()

            //διαφορετικά, πραγματοποιείται είσοδος με email και password
            else -> {

                val progressDialog = ProgressDialog(this@LoginActivity)
                progressDialog.setTitle("Είσοδος")
                progressDialog.setMessage("παρακαλώ περιμένετε...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val myAuth: FirebaseAuth = FirebaseAuth.getInstance()
                myAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        //αν είναι επιτυχής ο χρήστης οδηγείται στην NavigationActivity
                        if (it.isSuccessful) {
                            val intent = Intent (this@LoginActivity, NavigationActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }

                        else {
                            val message = it.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                            FirebaseAuth.getInstance().signOut()
                            progressDialog.dismiss()
                        }
                    }
            }
        }

    }


    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent (this@LoginActivity, NavigationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }
}
