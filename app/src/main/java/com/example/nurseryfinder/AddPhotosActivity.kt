package com.example.nurseryfinder

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_photos.*


class AddPhotosActivity : AppCompatActivity() {

    private var myUrl = ""
    //σύνδεσμος http για την φωτογραφία
    private var imageUri: Uri? = null
    private var storageUploadPicRef: StorageReference? = null
    private lateinit var   myProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photos)

        //δημιουργία αναφοράς στο αρχείο που θα αποθηκεύονται οι φωτογραφίες στο storage του firebase
        storageUploadPicRef = FirebaseStorage.getInstance().reference.child("Uploaded Pictures")

        //όταν ο χρήστης επιλέξει "Επιλέξτε Εικόνα" οδηγείται στο χώρο αποθήκευσης εικόνων της συσκευής του
        // και επιλέγει το τμήμα της εικόνας που επιθυμεί μέσω του "crop"
        choose_image_button_photos.setOnClickListener{
            CropImage.activity().setAspectRatio(1,1).start(this@AddPhotosActivity)

        }

        //όταν ο χρήστης επιλέξει "Ανέβασμα Αρχείου" καλείται η μέθοδος uploadPhoto()
        upload_button_photos.setOnClickListener {
            uploadPhoto()

        }

        all_uploads_button_photos.setOnClickListener{
            startActivity(Intent(this@AddPhotosActivity, PhotosGalleryActivity:: class.java))
        }
    }

    //καλείται αφού κόψει ο χρήστης την φωτογραφία για να της αποδοθεί uri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_view_photos.setImageURI(imageUri)
        }
    }



    //αποθηκεύει την επιλεγμένη από τον χρήστη φωτογραφία
    private fun uploadPhoto() {
        when{
            //αν πατήσει ο χρήστης το κουμπί "Ανέβασμα Αρχείου" χωρίς να έχει επιλέξει φωτο εμφανίζεται μήνυμα
            imageUri == null ->
                Toast.makeText(this, "Παρακαλώ επιλέξτε φωτογραφία", Toast.LENGTH_LONG).show()

            else -> {

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Ανάρτηση Φωτογραφίας")
                progressDialog.setMessage("παρακαλώ περιμένετε...")
                progressDialog.show()

                //δημιουργία αναφοράς του αρχείου της φωτογραφίας στο storage του firebase
                val fileRef = storageUploadPicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener (OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        //δημιουργία αναφοράς στη βάση δεδομένων
                        //όλες οι πληροφορίες σχετικά με την κάθε φωτογραφία αποθηκεύονται στον φάκελο Photos στον κόμβο του εκάστοτε user
                        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Photos")
                        // απόδοση μοναδικής ταυτότητας στην φωτογραφία
                        val photoId = ref.push().key

                        //αποθήκευση των πληροφοριών κάθε φωτογραφίας σε HashMap στη βάση
                        val photoMap = HashMap<String, Any>()
                        photoMap ["photoId"] =  photoId!!
                        photoMap["postImage"] = myUrl
                        photoMap ["publisher"] =  FirebaseAuth.getInstance().currentUser!!.uid
                        photoMap ["description"] = description_editText_photos.text.toString()


                        ref.child(photoId).updateChildren(photoMap)

                        Toast.makeText(this, "Η φωτογραφία αναρτήθηκε επιτυχώς", Toast.LENGTH_LONG).show()

                        progressDialog.dismiss()
                    }
                    else {
                        progressDialog.dismiss()
                    }
                } )

            }
        }
    }





}
