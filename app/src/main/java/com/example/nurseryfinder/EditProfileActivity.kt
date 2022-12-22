package com.example.nurseryfinder

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nurseryfinder.Model.Nursery
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private var imageChanged: Boolean = false
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        //ο συνδεδεμένος χρήστης
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        //ορισμός του φακέλου "Profile Pictures" στο Storage του Firebase όπου θα αποθηκεύονται οι φωτο προφίλ των χρηστών
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Profile Pictures")


        //δημιουργία λίστας με όλους τους νομούς
        val greekPrefectures = arrayOf("Επιλέξτε Νομό:", "Αθηνών", "Αιτωλοακαρνανίας", "Ανατολικής Αττικής", "Αργολίδας", "Αρκαδίας", "Άρτας",
            "Αττικής", "Αχαϊας", "Βοιωτίας", "Γρεβενών", "Δράμας", "Δυτικής Αττικής", "Δωδεκανήσου", "Έβρου", "Εύβοιας", "Ερυτανίας", "Ζακύνθου",
            "Ηλείας", "Ημαθείας", "Ηρακλείου", "Θεσπρωτίας", "Θεσσαλονίκης", "Ιωαννίνων", "Καβάλας", "Καρδίτσας", "Καστορίας", "Κέρκυρας",
            "Κεφαλληνίας", "Κιλκίς", "Κοζάνης", "Κορινθίας", "Κυκλάδων", "Λακωνίας", "Λάρισας", "Λασιθίου", "Λέσβου", "Λευκάδας",
            "Λήμνου", "Μαγνησίας", "Μεσσηνίας", "Ξάνθης", "Πειραία", "Πέλλας", "Πιερίας", "Πρέβεζας", "Ρεθύμνης", "Ροδόπης", "Σάμου", "Σερρών",
            "Τρικάλων", "Φθιώτιδας", "Φλώρινας", "Φωκίδας", "Χαλκιδικής", "Χανίων", "Χίου")

        //σύνδεση της λίστα με το spinner
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekPrefectures)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nomos_spinner_edit_profile.adapter = arrayAdapter


        //η επιλογή του χρήστη από το spinner
        nomos_spinner_edit_profile.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Νομό")) {

                        //do nothing

                    }else if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Νομό:")) {

                        //do nothing
                    }else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        nomos_textview_edit_profile.setText(parent?.getItemAtPosition(p2).toString())
                        //η επιλογή του χρήστη εμφανίζεται σε μήνυμα
                        Toast.makeText(this@EditProfileActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ////////

        //δημιουργία λίστας με όλους τους δήμους
        val greekMunicipalities = arrayOf("Επιλέξτε Δήμο:", "Αβδήρων", "Αγαθονησίου", "Αγιάς", "Αγίας Βαρβάρας", "Αγίας Παρασκευής",
            "Αγίου Βασιλείου", "Αγίου Δημητρίου", "Αγίου Ευστρατίου", "Αγίου Νικολάου", "Αγίων Αναργύρων - Καματερού",
            "Αγκιστρίου", "Αγράφων", "Αγρινίου", "Αθηναίων", "Αιγάλεω", "Αιγιαλείας", "Αίγινας", "Ακτίου-Βόνιτσας", "Αλεξάνδρειας",
            "Αλεξανδρούπολης", "Αλιάρτου", "Αλίμου", "Αλμυρού", "Αλμωπίας", "Αλοννήσου", "Αμαρίου", "Αμαρουσίου", "Αμοργού",
            "Αμπελοκήπων - Μενεμένης", "Αμυνταίου", "Αμφίκλειας - Ελάτειας", "Αμφιλοχίας", "Αμφίπολης", "Ανατολικής Μάνης", "Ανάφης",
            "Ανδραβίδας - Κυλλήνης", "Ανδρίτσαινας - Κρεστένων", "Άνδρου", "Αντιπάρου", "Ανωγείων", "Αποκορώνου", "Αργιθέας",
            "Αριστοτέλη", "Αρριανών", "Αρταίων", "Αρχαίας Ολυμπίας", "Αρχανών - Αστερουσίων", "Ασπροπύργου", "Αστυπάλαιας", "Αχαρνών",
            "Βάρης - Βούλας - Βουλιαγμένης", "Βέλου - Βόχας", "Βέροιας", "Βιάννου", "Βισαλτίας", "Βοΐου", "Βόλβης", "Βόλου",
            "Βόρειας Κυνουρίας", "Βορείων Τζουμέρκων", "Βριλησσίων", "Βύρωνος", "Γαλατσίου", "Γαύδου", "Γεωργίου Καραϊσκάκη",
            "Γλυφάδας", "Γόρτυνας", "Γορτυνίας", "Γρεβενών", "Δάφνης - Υμηττού", "Δέλτα", "Δελφών", "Δεσκάτης", "Δήμος Άργους - Μυκηνών",
            "Δήμος Μινώα Πεδιάδας", "Δήμος Οιχαλίας", "Διδυμοτείχου", "Διονύσου", "Δίου - Ολύμπου", "Διρφύων - Μεσσαπίων", "Διστόμου- Αράχοβας- Αντίκυρας",
            "Δομοκού", "Δοξάτου", "Δράμας", "Δυτικής Αχαΐας", "Δυτικής Μάνης", "Δωδώνης", "Δωρίδος", "Έδεσσας", "Ελασσόνας", "Ελαφονήσου",
            "Ελευσίνας", "Ελληνικού-Αργυρούπολης", "Εμμανουήλ Παππά", "Εορδαίας", "Επιδαύρου", "Ερέτριας", "Ερμιονίδας", "Ερυμάνθου", "Ευρώτα",
            "Ζαγοράς-Μουρεσίου", "Ζαγορίου", "Ζακύνθου", "Ζαχάρως", "Ζηρού", "Ζίτσας", "Ζωγράφου", "Ηγουμενίτσας", "Ήλιδας", "Ηλιούπολης",
            "Ηράκλειας", "Ηρακλείου", "Ηρακλείου", "Ηρωικής Νήσου Κάσου", "Ηρωικής Νήσου Ψαρών", "Ηρωικής Πόλεως Νάουσας", "Θάσου", "Θερμαϊκού",
            "Θέρμης", "Θέρμου", "Θεσσαλονίκης", "Θηβαίων", "Θήρας", "Ιάσμου", "Ιεράπετρας", "Ιεράς Πόλεως Μεσολογγίου", "Ιητών", "Ιθάκης",
            "Ικαρίας", "Ιλίου", "Ιστιαίας - Αιδηψού", "Ιωαννιτών", "Καβάλας", "Καισαριανής", "Καλαβρύτων", "Καλαμαριάς", "Καλαμάτας",
            "Καλλιθέας", "Καλυμνίων", "Καντάνου - Σελίνου", "Καρδίτσας", "Καρπάθου", "Καρπενησίου", "Καρύστου", "Κασσάνδρας", "Καστοριάς",
            "Κατερίνης", "Κάτω Νευροκοπίου", "Κέας", "Κεντρικών Τζουμέρκων", "Κερατσινίου - Δραπετσώνας", "Κέρκυρας", "Κεφαλλονιάς",
            "Κηφισιάς", "Κιλελέρ", "Κιλκίς", "Κιμώλου", "Κισσάμου", "Κοζάνης", "Κομοτηνής", "Κόνιτσας", "Κορδελιού-Ευόσμου", "Κορινθίων",
            "Κορυδαλλού", "Κρωπίας", "Κυθήρων", "Κύθνου", "Κύμης-Αλιβερίου", "Κω", "Λαγκαδά", "Λαμιέων", "Λάρισας", "Λαυρεωτικής", "Λεβαδέων",
            "Λειψών", "Λέρου", "Λέσβου", "Λευκάδας", "Λήμνου", "Λίμνης Πλαστήρα", "Λοκρών", "Λουτρακίου-Αγίων Θεοδώρων", "Λυκόβρυσης-Πεύκης",
            "Μαλεβιζίου", "Μάνδρας - Ειδυλλίας", "Μαντουδίου- Λίμνης- Αγίας Άννας", "Μαραθώνος", "Μαρκόπουλου Μεσογαίας", "Μαρωνείας - Σαπών",
            "Μεγαλόπολης", "Μεγανησίου", "Μεγαρέων", "Μεγίστης", "Μεσσήνης", "Μεταμορφώσεως", "Μετεώρων", "Μετσόβου", "Μήλου", "Μονεμβασιάς",
            "Μοσχάτου-Ταύρου", "Μουζακίου", "Μύκης", "Μυκόνου", "Μυλοποτάμου", "Μώλου - Αγίου Κωνσταντίνου", "Νάξου & Μικρών Κυκλάδων", "Ναυπακτίας",
            "Ναυπλιέων", "Νεάπολης-Συκεών", "Νέας Ζίχνης", "Νέας Ιωνίας", "Νέας Προποντίδας", "Νέας Σμύρνης", "Νεμέας", "Νεστόριου", "Νέστου",
            "Νίκαιας - Αγίου Ιωάννη Ρέντη", "Νικόλαου Σκουφά", "Νισύρου", "Νότιας Κυνουρίας", "Νοτίου Πηλίου", "Ξάνθης", "Ξηρομέρου",
            "Ξυλοκάστρου - Ευρωστίνης", "Οινουσσών", "Ορεστιάδας", "Ορεστίδος", "Οροπεδίου Λασιθίου", "Ορχομενού", "Παγγαίου", "Παιανίας",
            "Παιονίας", "Παλαιού Φαλήρου", "Παλαμά", "Παλλήνης", "Παξών", "Παπάγου-Χολαργού", "Παρανεστίου", "Πάργας", "Πάρου", "Πάτμου",
            "Πατρέων", "Παύλου Μελά", "Πειραιά", "Πέλλας", "Πεντέλης", "Περάματος", "Περιστερίου", "Πετρούπολης", "Πηνειού", "Πλατανιά",
            "Πολύγυρου", "Πόρου", "Πρέβεζας", "Πρεσπών", "Προσοτσάνης", "Πύδνας-Κολινδρού", "Πυλαίας-Χορτιάτη", "Πύλης", "Πύλου - Νέστορος",
            "Πύργου", "Πωγωνίου", "Ραφήνας-Πικερμίου", "Ρεθύμνης", "Ρήγα Φεραίου", "Ρόδου", "Σαλαμίνας", "Σαμοθράκης", "Σάμου",
            "Σαρωνικού", "Σερβίων - Βελβεντού", "Σερίφου", "Σερρών", "Σητείας", "Σιθωνίας", "Σικίνου", "Σικυωνίων", "Σιντικής", "Σίφνου",
            "Σκιάθου", "Σκοπέλου", "Σκύδρας", "Σκύρου", "Σουλίου", "Σουφλίου", "Σοφάδων", "Σπάρτης", "Σπάτων-Αρτέμιδος", "Σπετσών",
            "Στυλίδας", "Σύμης", "Σύρου - Ερμούπολης", "Σφακίων", "Τανάγρας", "Τεμπών", "Τήλου", "Τήνου", "Τοπείρου", "Τρικκαίων",
            "Τρίπολης", "Τριφυλίας", "Τροιζηνίας", "Τυρνάβου", "Ύδρας", "Φαιστού", "Φαρκαδόνος", "Φαρσάλων", "Φιλαδελφείας - Χαλκηδόνος",
            "Φιλιατών", "Φιλοθέης-Ψυχικού", "Φλώρινας", "Φολεγάνδρου", "Φούρνων Κορσεών","Φυλής", "Χαϊδαρίου", "Χαλανδρίου", "Χαλκηδόνος",
            "Χάλκης", "Χαλκιδέων", "Χανίων", "Χερσονήσου", "Χίου", "Ωραιοκάστρου", "Ωρωπού")

        //σύνδεση της λίστα με το spinner
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekMunicipalities)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dimos_spinner_edit_profile.adapter = arrayAdapter2

        //η επιλογή του χρήστη από το spinner
        dimos_spinner_edit_profile.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Δήμο")) {

                        //do nothing

                    }else if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Δήμο:")) {
                        //do nothing
                    }else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        dimos_textview_edit_profile.setText(parent?.getItemAtPosition(p2).toString())
                        Toast.makeText(this@EditProfileActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////

        //δημιουργία λίστας με τις προτεινόμενες ηλικίες
        val greekAges = arrayOf("Δεχόμαστε Ηλικίες από:", "0 ετών", "1 έτους", "2 ετών", "3 ετών", "4 ετών")
        //σύνδεση της λίστας με το spinner
        val arrayAdapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekAges)
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ages_spinner_edit_profile.adapter = arrayAdapter3

        //η επιλογή του χρήστη από το spinner
        ages_spinner_edit_profile.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Δεχόμαστε Ηλικίες από:")) {
                        //do nothing
                    }
                    else if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Ηλικία")){
                        //do nothing

                    } else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        ages_textview_edit_profile.setText(parent?.getItemAtPosition(p2).toString())
                        Toast.makeText(this@EditProfileActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////

        //δημιουργία λίστας ναι/ όχι
        val greekESPA = arrayOf("Συμμετέχουμε στο ΕΣΠΑ:","ΝΑΙ","ΟΧΙ")
        //σύνδεση της λίστας με το spinner
        val arrayAdapter4 = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekESPA)
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        espa_spinner_edit_profile.adapter = arrayAdapter4

        //η επιλογή του χρήστη από το spinner
        espa_spinner_edit_profile.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Συμμετέχουμε στο ΕΣΠΑ:")) {
                        //do nothing

                    }else if (parent?.getItemAtPosition(p2) == ("Επιλέξτε ΕΣΠΑ")) {
                        //do nothing

                    } else {

                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        espa_textview_edit_profile.setText(parent?.getItemAtPosition(p2).toString())
                        Toast.makeText(this@EditProfileActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////

        //δημιουργία λίστας ναι/ όχι
        val greekFood = arrayOf("Σερβίρουμε Φαγητό:","ΝΑΙ","ΟΧΙ")
        val arrayAdapter5 = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekFood)
        //σύνδεση της λίστας με το spinner
        arrayAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        food_spinner_edit_profile.adapter = arrayAdapter5

        //η επιλογή του χρήστη
        food_spinner_edit_profile.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Σερβίρουμε Φαγητό:")) {
                        //do nothing

                    }else if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Φαγητό")) {
                        //do nothing

                    }else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        food_textview_edit_profile.setText(parent?.getItemAtPosition(p2).toString())
                        Toast.makeText(this@EditProfileActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////

        //ο χρήστης επιλέγει "αλλαγή εικόνας"
        change_image_button_edit_profile.setOnClickListener {

            imageChanged = true

            //κόβει την εικόνα κατά προτίμηση, σε μέγεθος 1x1 (τετράγωνο)
            CropImage.activity().setAspectRatio(1,1).start(this@EditProfileActivity)
        }


        // ο χρήστης επιλέγει το κουμπί "αποθήκευση"
        savedata_button_edit_profile.setOnClickListener {
            //αν έχει αλλάξει η εικόνα προφίλ καλείται η upload PhotoAndUpdateInfo()
            if (imageChanged){

                uploadPhotoAndUpdateInfo()

            }
            //αν δεν αλλάξει η εικόνα καλείται η updateNurseryInfo()
            else{
                updateNurseryInfo()
            }

        }



        nurseryInfo()
    }

    //το αρχείο της κομμένης εικόνας αποθηκεύεται ως uri και το αποτέλεσμα αποθηκεύεται σαν φωτό προφίλ
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == Activity.RESULT_OK  &&  data != null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_edit_profile.setImageURI(imageUri)
        }
    }

    //γίνεται ενημέρωση των πληροφοριών προφίλ του παιδικού σταθμού ως αποτέλεσμα της επεξεργασίας προφίλ
    private fun updateNurseryInfo(){

        //αν ο χρήστης παραλείψει να συμπληρώσει οτιδήποτε από τα παρακάτω εμφανίζεται ανάλογο μήνυμα στην οθόνη
        when{
            TextUtils.isEmpty(name_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε το Όνομα Παιδικού Σταθμού", Toast.LENGTH_LONG).show()

            nomos_textview_edit_profile.text.toString() == "Επιλέξτε Νομό" ->
                Toast.makeText(this, "Επιλέξτε Νομό", Toast.LENGTH_LONG).show()

            dimos_textview_edit_profile.text.toString() =="Επιλέξτε Δήμο" ->
                Toast.makeText(this, "Επιλέξτε Δήμο", Toast.LENGTH_LONG).show()

            TextUtils.isEmpty(address_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε τη διεύθυνση του Παιδικού Σταθμού", Toast.LENGTH_LONG).show()

            ages_textview_edit_profile.text.toString() =="Επιλέξτε Ηλικία" ->
                Toast.makeText(this, "Επιλέξτε από ποια ηλικία δέχεστε", Toast.LENGTH_LONG).show()

            espa_textview_edit_profile.text.toString() == "Επιλέξτε ΕΣΠΑ" ->
                Toast.makeText(this, "Επιλέξτε αν συμμετέχετε στο ΕΣΠΑ", Toast.LENGTH_LONG).show()

            food_textview_edit_profile.text.toString() == "Επιλέξτε Φαγητό" ->
                Toast.makeText(this, "Επιλέξτε αν σερβίρετε φαγητό", Toast.LENGTH_LONG).show()

            TextUtils.isEmpty(activities_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε τις δραστηριότητες που προσφέρει ο Παιδικός Σταθμός", Toast.LENGTH_LONG).show()



            else -> {
                //οι πληροφορίες που αφορούν τον ΠΣ αποθηκεύονται σε HashMap στην τοποθεσία "Users" της βάσης δεδομένων
                val nurseryRef = FirebaseDatabase.getInstance().reference.child("Users")

                val userMap = HashMap<String, Any>()
                userMap ["name"] =  name_editText_edit_profile.text.toString()
                userMap ["phone"] =  phone_editText_edit_profile.text.toString()
                userMap ["email"] =  email_editText_edit_profile.text.toString()
                userMap ["prefecture"] = nomos_textview_edit_profile.text.toString()
                userMap ["address"] = address_editText_edit_profile.text.toString()
                userMap ["activities"] =  activities_editText_edit_profile.text.toString()
                userMap ["municipality"] = dimos_textview_edit_profile.text.toString()
                userMap ["ages"] = ages_textview_edit_profile.text.toString()
                userMap ["espa"] = espa_textview_edit_profile.text.toString()
                userMap ["food"] = food_textview_edit_profile.text.toString()


                nurseryRef.child(firebaseUser.uid).updateChildren(userMap)

                Toast.makeText(this, "Τα στοιχεία σας ενημερώθηκαν", Toast.LENGTH_LONG).show()

                //εφόσον ο χρήστης επιλέξει αποθήκευση δεδομένων η οθόνη οδηγείται στο μενού πλοήγησης μέσω Intent
                val intent = Intent (this@EditProfileActivity, NavigationActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    //τα views  του EditProfileActivity ενημερώνονται από την βάση δεδομένων του firebase και εμφανίζονται συμπληρωμένα
    private fun nurseryInfo(){

        val nurseryRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser.uid)

        nurseryRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()){
                    val nursery = p0.getValue<Nursery>(Nursery::class.java)
                    Picasso.get().load(nursery!!.image).placeholder(R.drawable.ic_action_profile).into(profile_image_edit_profile)
                    name_editText_edit_profile.setText(nursery!!.name)
                    email_editText_edit_profile.setText(nursery!!.email)
                    phone_editText_edit_profile.setText(nursery!!.phone)
                    address_editText_edit_profile.setText(nursery!!.address)
                    activities_editText_edit_profile.setText(nursery!!.activities)
                    nomos_textview_edit_profile.text = nursery!!.prefecture
                    dimos_textview_edit_profile.text = nursery!!.municipality
                    ages_textview_edit_profile.text = nursery!!.ages
                    espa_textview_edit_profile.text = nursery!!.espa
                    food_textview_edit_profile.text = nursery!!.food

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    //παρόμοια διαδικασία με updateNurseryInfo() με επιπλέον λειτουργία αποθήκευσης και ενημέρωσης της εικόνας προφίλ
    private fun uploadPhotoAndUpdateInfo()
    {
        when
        {
            //imageUri == null ->
            //Toast.makeText(this, "Επιλέξτε φωτογραφία προφίλ", Toast.LENGTH_LONG).show()

            TextUtils.isEmpty(name_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε το Όνομα Παιδικού Σταθμού", Toast.LENGTH_LONG).show()

            nomos_textview_edit_profile.text.toString() == "Επιλέξτε Νομό" ->
                Toast.makeText(this, "Επιλέξτε Νομό", Toast.LENGTH_LONG).show()

            dimos_textview_edit_profile.text.toString() =="Επιλέξτε Δήμο" ->
                Toast.makeText(this, "Επιλέξτε Δήμο", Toast.LENGTH_LONG).show()

            TextUtils.isEmpty(address_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε τη διεύθυνση του Παιδικού Σταθμού", Toast.LENGTH_LONG).show()

            ages_textview_edit_profile.text.toString() =="Επιλέξτε Ηλικία" ->
                Toast.makeText(this, "Επιλέξτε από ποια ηλικία δέχεστε", Toast.LENGTH_LONG).show()

            espa_textview_edit_profile.text.toString() == "Επιλέξτε ΕΣΠΑ" ->
                Toast.makeText(this, "Επιλέξτε αν συμμετέχετε στο ΕΣΠΑ", Toast.LENGTH_LONG).show()

            food_textview_edit_profile.text.toString() == "Επιλέξτε Φαγητό" ->
                Toast.makeText(this, "Επιλέξτε αν σερβίρετε φαγητό", Toast.LENGTH_LONG).show()

            TextUtils.isEmpty(activities_editText_edit_profile.text.toString()) ->
                Toast.makeText(this, "Συμπληρώστε τις δραστηριότητες που προσφέρει ο Παιδικός Σταθμός", Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Ενημέρωση Στοιχείων")
                progressDialog.setMessage("παρακαλώ περιμένετε...")
                progressDialog.show()

                //η εικόνα αποθηκεύεται στο storage του firebase και συνδέεται με το user id του χρήστη που την ανεβάζει
                val fileRef = storageProfilePicRef!!.child(firebaseUser!!.uid + ".jpg")

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

                        val ref = FirebaseDatabase.getInstance().reference.child("Users")

                        val userMap = HashMap<String, Any>()
                        userMap ["name"] =  name_editText_edit_profile.text.toString()
                        userMap ["phone"] =  phone_editText_edit_profile.text.toString()
                        userMap ["email"] =  email_editText_edit_profile.text.toString()
                        userMap ["prefecture"] = nomos_textview_edit_profile.text.toString()
                        userMap ["address"] = address_editText_edit_profile.text.toString()
                        userMap ["activities"] =  activities_editText_edit_profile.text.toString()
                        userMap ["municipality"] = dimos_textview_edit_profile.text.toString()
                        userMap ["ages"] = ages_textview_edit_profile.text.toString()
                        userMap ["espa"] = espa_textview_edit_profile.text.toString()
                        userMap ["food"] = food_textview_edit_profile.text.toString()
                        userMap["image"] = myUrl

                        ref.child(firebaseUser.uid).updateChildren(userMap)

                        Toast.makeText(this, "Η ενημέρωση των στοιχείων σας πραγματοποιήθηκε επιτυχώς", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@EditProfileActivity, NavigationActivity::class.java)
                        startActivity(intent)
                        finish()
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
