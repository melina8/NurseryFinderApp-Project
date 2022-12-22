package com.example.nurseryfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.nurseryfinder.Model.Nursery
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_filters.*
import java.io.Serializable


class FiltersActivity : AppCompatActivity() {

    private  var  filteredList: MutableList<Nursery>? = null
    private var users: MutableList<Nursery>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)

        //δημιουργία λίστας με όλους τους νομούς
        val greekPrefectures = arrayOf(
            "Επιλέξτε Νομό:",
            "Αθηνών",
            "Αιτωλοακαρνανίας",
            "Ανατολικής Αττικής",
            "Αργολίδας",
            "Αρκαδίας",
            "Άρτας",
            "Αττικής",
            "Αχαϊας",
            "Βοιωτίας",
            "Γρεβενών",
            "Δράμας",
            "Δυτικής Αττικής",
            "Δωδεκανήσου",
            "Έβρου",
            "Εύβοιας",
            "Ερυτανίας",
            "Ζακύνθου",
            "Ηλείας",
            "Ημαθείας",
            "Ηρακλείου",
            "Θεσπρωτίας",
            "Θεσσαλονίκης",
            "Ιωαννίνων",
            "Καβάλας",
            "Καρδίτσας",
            "Καστοριάς",
            "Κέρκυρας",
            "Κεφαλληνίας",
            "Κιλκίς",
            "Κοζάνης",
            "Κορινθίας",
            "Κυκλάδων",
            "Λακωνίας",
            "Λάρισας",
            "Λασιθίου",
            "Λέσβου",
            "Λευκάδας",
            "Λήμνου",
            "Μαγνησίας",
            "Μεσσηνίας",
            "Ξάνθης",
            "Πειραία",
            "Πέλλας",
            "Πιερίας",
            "Πρέβεζας",
            "Ρεθύμνης",
            "Ροδόπης",
            "Σάμου",
            "Σερρών",
            "Τρικάλων",
            "Φθιώτιδας",
            "Φλώρινας",
            "Φωκίδας",
            "Χαλκιδικής",
            "Χανίων",
            "Χίου"
        )

        //σύνδεση της λίστα με το spinner
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, greekPrefectures)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        nomos_spinner_filter.adapter = arrayAdapter


        //η επιλογή του χρήστη από το spinner
        nomos_spinner_filter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Νομό:")) {

                        //do nothing

                    } else if (parent?.getItemAtPosition(p2) == null) {

                        //do nothing
                    } else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        nomos_textview2_filter.setText(parent?.getItemAtPosition(p2).toString())
                        //η επιλογή του χρήστη εμφανίζεται σε μήνυμα
                       // Toast.makeText(this@FiltersActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ////////

        //δημιουργία λίστας με όλους τους δήμους
        val greekMunicipalities = arrayOf(
            "Επιλέξτε Δήμο:",
            "Αβδήρων",
            "Αγαθονησίου",
            "Αγιάς",
            "Αγίας Βαρβάρας",
            "Αγίας Παρασκευής",
            "Αγίου Βασιλείου",
            "Αγίου Δημητρίου",
            "Αγίου Ευστρατίου",
            "Αγίου Νικολάου",
            "Αγίων Αναργύρων - Καματερού",
            "Αγκιστρίου",
            "Αγράφων",
            "Αγρινίου",
            "Αθηναίων",
            "Αιγάλεω",
            "Αιγιαλείας",
            "Αίγινας",
            "Ακτίου-Βόνιτσας",
            "Αλεξάνδρειας",
            "Αλεξανδρούπολης",
            "Αλιάρτου",
            "Αλίμου",
            "Αλμυρού",
            "Αλμωπίας",
            "Αλοννήσου",
            "Αμαρίου",
            "Αμαρουσίου",
            "Αμοργού",
            "Αμπελοκήπων-Μενεμένης",
            "Αμυνταίου",
            "Αμφίκλειας-Ελάτειας",
            "Αμφιλοχίας",
            "Αμφίπολης",
            "Ανατολικής Μάνης",
            "Ανάφης",
            "Ανδραβίδας - Κυλλήνης",
            "Ανδρίτσαινας - Κρεστένων",
            "Άνδρου",
            "Αντιπάρου",
            "Ανωγείων",
            "Αποκορώνου",
            "Αργιθέας",
            "Αριστοτέλη",
            "Αρριανών",
            "Αρταίων",
            "Αρχαίας Ολυμπίας",
            "Αρχανών - Αστερουσίων",
            "Ασπροπύργου",
            "Αστυπάλαιας",
            "Αχαρνών",
            "Βάρης - Βούλας - Βουλιαγμένης",
            "Βέλου-Βόχας",
            "Βέροιας",
            "Βιάννου",
            "Βισαλτίας",
            "Βοΐου",
            "Βόλβης",
            "Βόλου",
            "Βόρειας Κυνουρίας",
            "Βορείων Τζουμέρκων",
            "Βριλησσίων",
            "Βύρωνος",
            "Γαλατσίου",
            "Γαύδου",
            "Γεωργίου Καραϊσκάκη",
            "Γλυφάδας",
            "Γόρτυνας",
            "Γορτυνίας",
            "Γρεβενών",
            "Δάφνης - Υμηττού",
            "Δέλτα",
            "Δελφών",
            "Δεσκάτης",
            "Δήμος Άργους - Μυκηνών",
            "Δήμος Μινώα Πεδιάδας",
            "Δήμος Οιχαλίας",
            "Διδυμοτείχου",
            "Διονύσου",
            "Δίου-Ολύμπου",
            "Διρφύων-Μεσσαπίων",
            "Διστόμου-Αράχοβας-Αντίκυρας",
            "Δομοκού",
            "Δοξάτου",
            "Δράμας",
            "Δυτικής Αχαΐας",
            "Δυτικής Μάνης",
            "Δωδώνης",
            "Δωρίδος",
            "Έδεσσας",
            "Ελασσόνας",
            "Ελαφονήσου",
            "Ελευσίνας",
            "Ελληνικού-Αργυρούπολης",
            "Εμμανουήλ Παππά",
            "Εορδαίας",
            "Επιδαύρου",
            "Ερέτριας",
            "Ερμιονίδας",
            "Ερυμάνθου",
            "Ευρώτα",
            "Ζαγοράς-Μουρεσίου",
            "Ζαγορίου",
            "Ζακύνθου",
            "Ζαχάρως",
            "Ζηρού",
            "Ζίτσας",
            "Ζωγράφου",
            "Ηγουμενίτσας",
            "Ήλιδας",
            "Ηλιούπολης",
            "Ηράκλειας",
            "Ηρακλείου",
            "Ηρακλείου",
            "Ηρωικής Νήσου Κάσου",
            "Ηρωικής Νήσου Ψαρών",
            "Ηρωικής Πόλεως Νάουσας",
            "Θάσου",
            "Θερμαϊκού",
            "Θέρμης",
            "Θέρμου",
            "Θεσσαλονίκης",
            "Θηβαίων",
            "Θήρας",
            "Ιάσμου",
            "Ιεράπετρας",
            "Ιεράς Πόλεως Μεσολογγίου",
            "Ιητών",
            "Ιθάκης",
            "Ικαρίας",
            "Ιλίου",
            "Ιστιαίας - Αιδηψού",
            "Ιωαννιτών",
            "Καβάλας",
            "Καισαριανής",
            "Καλαβρύτων",
            "Καλαμαριάς",
            "Καλαμάτας",
            "Καλλιθέας",
            "Καλυμνίων",
            "Καντάνου - Σελίνου",
            "Καρδίτσας",
            "Καρπάθου",
            "Καρπενησίου",
            "Καρύστου",
            "Κασσάνδρας",
            "Καστοριάς",
            "Κατερίνης",
            "Κάτω Νευροκοπίου",
            "Κέας",
            "Κεντρικών Τζουμέρκων",
            "Κερατσινίου - Δραπετσώνας",
            "Κέρκυρας",
            "Κεφαλλονιάς",
            "Κηφισιάς",
            "Κιλελέρ",
            "Κιλκίς",
            "Κιμώλου",
            "Κισσάμου",
            "Κοζάνης",
            "Κομοτηνής",
            "Κόνιτσας",
            "Κορδελιού-Ευόσμου",
            "Κορινθίων",
            "Κορυδαλλού",
            "Κρωπίας",
            "Κυθήρων",
            "Κύθνου",
            "Κύμης-Αλιβερίου",
            "Κω",
            "Λαγκαδά",
            "Λαμιέων",
            "Λάρισας",
            "Λαυρεωτικής",
            "Λεβαδέων",
            "Λειψών",
            "Λέρου",
            "Λέσβου",
            "Λευκάδας",
            "Λήμνου",
            "Λίμνης Πλαστήρα",
            "Λοκρών",
            "Λουτρακίου-Αγίων Θεοδώρων",
            "Λυκόβρυσης-Πεύκης",
            "Μαλεβιζίου",
            "Μάνδρας-Ειδυλλίας",
            "Μαντουδίου-Λίμνης-Αγίας Άννας",
            "Μαραθώνος",
            "Μαρκόπουλου Μεσογαίας",
            "Μαρωνείας-Σαπών",
            "Μεγαλόπολης",
            "Μεγανησίου",
            "Μεγαρέων",
            "Μεγίστης",
            "Μεσσήνης",
            "Μεταμορφώσεως",
            "Μετεώρων",
            "Μετσόβου",
            "Μήλου",
            "Μονεμβασιάς",
            "Μοσχάτου-Ταύρου",
            "Μουζακίου",
            "Μύκης",
            "Μυκόνου",
            "Μυλοποτάμου",
            "Μώλου-Αγίου Κωνσταντίνου",
            "Νάξου & Μικρών Κυκλάδων",
            "Ναυπακτίας",
            "Ναυπλιέων",
            "Νεάπολης-Συκεών",
            "Νέας Ζίχνης",
            "Νέας Ιωνίας",
            "Νέας Προποντίδας",
            "Νέας Σμύρνης",
            "Νεμέας",
            "Νεστόριου",
            "Νέστου",
            "Νίκαιας - Αγίου Ιωάννη Ρέντη",
            "Νικόλαου Σκουφά",
            "Νισύρου",
            "Νότιας Κυνουρίας",
            "Νοτίου Πηλίου",
            "Ξάνθης",
            "Ξηρομέρου",
            "Ξυλοκάστρου-Ευρωστίνης",
            "Οινουσσών",
            "Ορεστιάδας",
            "Ορεστίδος",
            "Οροπεδίου Λασιθίου",
            "Ορχομενού",
            "Παγγαίου",
            "Παιανίας",
            "Παιονίας",
            "Παλαιού Φαλήρου",
            "Παλαμά",
            "Παλλήνης",
            "Παξών",
            "Παπάγου-Χολαργού",
            "Παρανεστίου",
            "Πάργας",
            "Πάρου",
            "Πάτμου",
            "Πατρέων",
            "Παύλου Μελά",
            "Πειραιά",
            "Πέλλας",
            "Πεντέλης",
            "Περάματος",
            "Περιστερίου",
            "Πετρούπολης",
            "Πηνειού",
            "Πλατανιά",
            "Πολύγυρου",
            "Πόρου",
            "Πρέβεζας",
            "Πρεσπών",
            "Προσοτσάνης",
            "Πύδνας-Κολινδρού",
            "Πυλαίας-Χορτιάτη",
            "Πύλης",
            "Πύλου - Νέστορος",
            "Πύργου",
            "Πωγωνίου",
            "Ραφήνας-Πικερμίου",
            "Ρεθύμνης",
            "Ρήγα Φεραίου",
            "Ρόδου",
            "Σαλαμίνας",
            "Σαμοθράκης",
            "Σάμου",
            "Σαρωνικού",
            "Σερβίων-Βελβεντού",
            "Σερίφου",
            "Σερρών",
            "Σητείας",
            "Σιθωνίας",
            "Σικίνου",
            "Σικυωνίων",
            "Σιντικής",
            "Σίφνου",
            "Σκιάθου",
            "Σκοπέλου",
            "Σκύδρας",
            "Σκύρου",
            "Σουλίου",
            "Σουφλίου",
            "Σοφάδων",
            "Σπάρτης",
            "Σπάτων-Αρτέμιδος",
            "Σπετσών",
            "Στυλίδας",
            "Σύμης",
            "Σύρου - Ερμούπολης",
            "Σφακίων",
            "Τανάγρας",
            "Τεμπών",
            "Τήλου",
            "Τήνου",
            "Τοπείρου",
            "Τρικκαίων",
            "Τρίπολης",
            "Τριφυλίας",
            "Τροιζηνίας",
            "Τυρνάβου",
            "Ύδρας",
            "Φαιστού",
            "Φαρκαδόνος",
            "Φαρσάλων",
            "Φιλαδελφείας - Χαλκηδόνος",
            "Φιλιατών",
            "Φιλοθέης-Ψυχικού",
            "Φλώρινας",
            "Φολεγάνδρου",
            "Φούρνων Κορσεών",
            "Φυλής",
            "Χαϊδαρίου",
            "Χαλανδρίου",
            "Χαλκηδόνος",
            "Χάλκης",
            "Χαλκιδέων",
            "Χανίων",
            "Χερσονήσου",
            "Χίου",
            "Ωραιοκάστρου",
            "Ωρωπού"
        )

        //σύνδεση της λίστα με το spinner
        val arrayAdapter2 =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, greekMunicipalities)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dimos_spinner_filter.adapter = arrayAdapter2

        //η επιλογή του χρήστη από το spinner
        dimos_spinner_filter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Επιλέξτε Δήμο:")) {

                        //do nothing

                    } else if (parent?.getItemAtPosition(p2) == null) {
                        //do nothing
                    } else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        dimos_textview2_filter.setText(parent?.getItemAtPosition(p2).toString())
                        //Toast.makeText(this@FiltersActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////

        //δημιουργία λίστας με τις προτεινόμενες ηλικίες
        val greekAges = arrayOf(
            "Δεχόμαστε Ηλικίες από:",
            "0\nετών",
            "1\nέτους",
            "2\nετών",
            "3\nετών",
            "4\nετών"
        )
        //σύνδεση της λίστας με το spinner
        val arrayAdapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, greekAges)
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ages_spinner_filter.adapter = arrayAdapter3

        //η επιλογή του χρήστη από το spinner
        ages_spinner_filter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    //αν δεν επιλέξει κάτι ο χρήστης δεν αλλάζει τίποτα
                    if (parent?.getItemAtPosition(p2) == ("Δεχόμαστε Ηλικίες από:")) {
                        //do nothing
                    } else if (parent?.getItemAtPosition(p2) == null) {
                        //do nothing

                    } else {
                        //το textview που συνδέεται με το spinner συμπληρώνεται με την επιλογή του χρήστη
                        ages_textview2_filter.setText(parent?.getItemAtPosition(p2).toString())
                        //Toast.makeText(this@FiltersActivity, parent?.getItemAtPosition(p2).toString(), Toast.LENGTH_LONG).show()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            } ///////


        //το κουμπί είναι προεπιλεγμένο γκρι, αν το πατήσει ο χρήστης γίνεται φουξ κ ενεργοποιεί το φίλτρο "συμμετοχή στο ΕΣΠΑ"
        espa_button_filters.setOnClickListener(object : View.OnClickListener {
            private var isGrey = true
            override fun onClick(view: View?) {
                isGrey = !isGrey
                val resId: Int = if (isGrey) R.drawable.grey_button else R.drawable.magenda_button
                espa_button_filters.setBackgroundResource(resId)
                espa_button_filters.setText(if (isGrey) "" else "NΑΙ")
            }
        })

        //το κουμπί είναι προεπιλεγμένο γκρι, αν το πατήσει ο χρήστης γίνεται φουξ κ ενεργοποιεί το φίλτρο "σερβίρισμα φαγητού"
        food_button_filters.setOnClickListener(object : View.OnClickListener {
            private var isGrey = true
            override fun onClick(view: View?) {
                isGrey = !isGrey
                val resId: Int = if (isGrey) R.drawable.grey_button else R.drawable.magenda_button
                food_button_filters.setBackgroundResource(resId)
                food_button_filters.setText(if (isGrey) "" else "ΝΑΙ")
            }
        })

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //γίνεται εφαρμογή των φίλτρων που έπέλεξε ο χρήστης
        apply_filters_button.setOnClickListener {

            users = ArrayList()

            //οι επιλογές του χρήστη...
            val prefecture = nomos_textview2_filter?.text.toString()
            val municipality = dimos_textview2_filter?.text.toString()
            val ages = ages_textview2_filter?.text.toString()
            val espa =  espa_button_filters?.text.toString()
            val food = food_button_filters?.text.toString()
            Log.d("FiltersActivity", "intents are: $prefecture $municipality $ages $espa $food")

            //όλοι οι σταθμοί της βάσης δεδομένων αποθηκεύονται στην ArrayList "users"
            val ref = FirebaseDatabase.getInstance().getReference("/Users")
                .orderByChild("prefecture")

            ref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    users?.clear()
                    for (snapshot in dataSnapshot.children) {

                        Log.d("FiltersActivity", "snapshot: $snapshot")

                        val user = snapshot.getValue(Nursery::class.java)
                        if (user != null) {
                            users?.add(user)
                            Log.d("FiltersActivity", "users: ${users?.size}")
                            //γίνεται ανάθεση του περιεχομένου της λίστας users στη λίστα filteredList
                            filteredList = users
                        }
                    }  //for snapshot



                    /////////////////////////////////////////////
                    if (users != null) {
                        for (index: Int in users!!.indices) {
                            Log.d(
                                "FiltersActivity",
                                "users from firebase: ${users?.get(index)?.prefecture}"
                            )
                        }
                    }
                    /////////////////////////////////////////////


                    if (filteredList != null) {
                        for (index: Int in filteredList!!.indices) {
                            Log.d(
                                "FiltersActivity",
                                "users from filtered list: ${filteredList?.get(index)
                                    ?.prefecture}"
                            )


                        }
                    }

                         //φιλτράρεται η λίστα με βάση την επιλογή νομού
                         if (prefecture == null || prefecture == "") {
                             Log.d(
                                 "FiltersActivity",
                                 "filteredList1 didnt change: ${filteredList?.size}"
                             )
                         } else {
                             var query1 = filteredList?.filter { it.prefecture.equals(prefecture) }
                             if (query1 != null) {
                                 for (index: Int in query1.indices) {
                                     Log.d(
                                         "FiltersActivity",
                                         "query1: ${query1.get(index).prefecture}"
                                     )
                                 } //for
                                 filteredList = query1.toArrayList()
                                 Log.d("FiltersActivity", "filteredList1: ${filteredList?.size}")
                             }  //if query1

                         }  //////////

                        // φιλτράρεται η λίστα με βάση την επιλογή δήμου
                        if (municipality == null || municipality == "") {
                             Log.d(
                                 "FiltersActivity",
                                 "filteredList2 didnt change: ${filteredList?.size}"
                             )
                         } else {
                             var query2 =
                                 filteredList?.filter { it.municipality.equals(municipality) }
                             if (query2 != null) {
                                 for (index: Int in query2.indices) {
                                     Log.d(
                                         "FiltersActivity",
                                         "query2: ${query2.get(index).municipality}"
                                     )
                                 } //for
                                 filteredList = query2.toArrayList()
                                 Log.d("FiltersActivity", "filteredList2: ${filteredList?.size}")
                             }  //if query2

                         } ///////////

                        //φίλτράρεται η λίστα με βάση την επιλογή ηλικίας
                        if (ages == null || ages == "") {
                            Log.d(
                                "FiltersActivity",
                                "filteredList3 didnt change: ${filteredList?.size}"
                            )
                        } else {
                            var query3 = filteredList?.filter { it.ages[0].equals(ages[0]) }

                            if (query3 != null) {
                                for (index: Int in query3.indices) {
                                    Log.d("FiltersActivity", "query3: ${query3.get(index).ages}")
                               } //for
                                filteredList = query3.toArrayList()
                                Log.d("FiltersActivity", "filteredList3: ${filteredList?.size}")
                            }  //if query3
                        } ///////////

                        //φίλτράρεται η λίστα με βάση την επιλογή φαγητού
                        if (food == null || food == "") {
                            Log.d(
                                "FiltersActivity",
                                "filteredList5 didnt change: ${filteredList?.size}"
                            )
                        } else {
                            var query4 = filteredList?.filter { it.food.equals(food) }

                            if (query4 != null) {
                                for (index: Int in query4.indices) {
                                    Log.d("FiltersActivity", "query4: ${query4.get(index).food}")
                                } //for
                                filteredList = query4.toArrayList()
                                Log.d("FiltersActivity", "filteredList4: ${filteredList?.size}")
                            }  //if query4
                        } ///////////


                           //φίλτράρεται η λίστα με βάση την επιλογή ΕΣΠΑ
                           if (espa == null || espa == "") {
                                Log.d("FiltersActivity", "filteredList5 didnt change: ${filteredList?.size}")
                            } else{
                                var query5 = filteredList?.filter { it.espa.equals("ΝΑΙ") }

                                if (query5 != null) {
                                    for (index: Int in query5.indices) {
                                        Log.d("FiltersActivity", "query5: ${query5.get(index).espa}")
                                    } //for
                                    filteredList = query5.toArrayList()
                                    Log.d("FiltersActivity", "filteredList5: ${filteredList?.size}")
                                }  //if query5
                            } /////////


                   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                   if (filteredList != null) {
                       for (index: Int in filteredList!!.indices) {
                           Log.d("FiltersActivity", "Final filteredList: ${filteredList?.get(index)?.prefecture}")
                       }
                   }


                    //αν δεν επιλεγεί κάποιο φίλτρο ή δε βρεθεί αποτέλεσμα εμφανίζεται κατάλληλο μήνυμα, διαφορετικά μεταφέρεται η φιλτραρισμένη λίστα
                    // στην FilteredListActivity για να εμφανιστούν τα αποτελέσματα της εφαρμογής των επιλεγμένων φίλτρων

                    if (prefecture == "" && municipality =="" && ages == "" && food == "" && espa == ""){
                        Toast.makeText(this@FiltersActivity, "Δεν επιλέξατε κάποιο φίλτρο.\nΔοκιμάστε ξανά.", Toast.LENGTH_LONG).show()
                   }
                    else if (filteredList?.isEmpty()== true) {
                        Toast.makeText(this@FiltersActivity, "Δεν βρέθηκαν αποτελέσματα.\nΔοκιμάστε νέα αναζήτηση.", Toast.LENGTH_LONG).show()
                    } else {
                       val intent = Intent(this@FiltersActivity, FilteredListActivity::class.java)
                        intent.putParcelableArrayListExtra("FilteredList", filteredList as ArrayList<Nursery>)
                        startActivity(intent)

                    }
                }//onDataChanged



                override fun onCancelled(p0: DatabaseError) {
                }

            }) // end of listener


        } //apply filters listener
    } //on create


    fun <T> List<T>.toArrayList(): ArrayList<T>{
        return ArrayList(this)
    }

} //class











