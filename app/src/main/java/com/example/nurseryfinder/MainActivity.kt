package com.example.nurseryfinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eimaigonios_button_main.setOnClickListener {
            Log.d("MainActivity", "Show Presentation Options")
            val intent = Intent (this, SelectPresentation::class.java)
            startActivity(intent)
        }

        eimaiidioktitis_button_main.setOnClickListener{
            Log.d("MainActivity", "Go to Register screen")
            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
