package com.example.nurseryfinder.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nurseryfinder.FiltersActivity
import com.example.nurseryfinder.NurseryListActivity
import com.example.nurseryfinder.R
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        //το κουμπί "Εμφάνιση όλων" οδηγείται στην NurseryListActivity (λίστα εγγραφών)
        view.list_button_search_frag.setOnClickListener {
            val intent = Intent(context, NurseryListActivity::class.java)
            startActivity(intent)
        }
        //το κουμπί "Αναζήτηση με Φίλτρα" οδηγεί στην FiltersActivity
         view.filter_button_search_frag.setOnClickListener {
             val intent = Intent(context, FiltersActivity::class.java)
             startActivity(intent)
         }

        return view

    }   /////



}