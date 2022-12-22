package com.example.nurseryfinder


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_presentation.*

class SelectPresentation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_presentation)


        //το κουμπί "Εμφάνιση όλων (κατά Νομό)" οδηγεί στην λίστα Παιδικών Σταθμών μέσω Intent
        list_button_selectpresentation.setOnClickListener {
            val intent = Intent(this, NurseryListActivity::class.java)
            startActivity(intent)
        }

        //το κουμπί "Αναζήτηση με φίλτρα" οδηγεί στην Activity αναζήτησης με φίλτρα μέσω Intent
        filter_button_selectpresentation.setOnClickListener {
            val intent = Intent(this, FiltersActivity::class.java)
            startActivity(intent)

        }
    }
}