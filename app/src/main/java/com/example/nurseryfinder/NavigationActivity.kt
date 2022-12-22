package com.example.nurseryfinder

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.nurseryfinder.fragments.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var homeFragment: HomeFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var editFragment: EditFragment
    lateinit var photosFragment: PhotosFragment
    lateinit var searchFragment: SearchFragment
    lateinit var deleteAccountFragment: DeleteAccountFragment
    lateinit var logoutFragment: LogoutFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setSupportActionBar(toolbar_appbar_nav)
        val actionBar = supportActionBar
        actionBar?.title = "Μενού Πλοήγησης"


        val drawerToggle: ActionBarDrawerToggle = object: ActionBarDrawerToggle(
            this, drawer_layout,toolbar_appbar_nav, (R.string.open), (R.string.close) ){
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        //ανοίγει το fragment, προεπιλέγεται το profileFragment
        profileFragment = ProfileFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, profileFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()


        //verifyUserIsLoggedIn()
    }

    // private fun verifyUserIsLoggedIn(){
    // val uid = FirebaseAuth.getInstance().uid
    //if (uid==null){
    // val intent = Intent(this, MainActivity::class.java)
    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    //startActivity(intent)
    // }
    //}

    //μενού επιλογών του χρήστη
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.Home -> {
                homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.profile -> {
                profileFragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, profileFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.edit_profile -> {
                startActivity(Intent(this@NavigationActivity, EditProfileActivity:: class.java))
            }
            R.id.add_photos -> {
                startActivity(Intent(this@NavigationActivity, AddPhotosActivity:: class.java))
                //return@OnNavigationItemSelectedListener true

                //photosFragment = PhotosFragment()
                //supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, photosFragment)
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.search -> {
                searchFragment = SearchFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, searchFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.logout -> {
                logoutFragment = LogoutFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, logoutFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.delete_account -> {
                deleteAccountFragment = DeleteAccountFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frame_appbar_nav, deleteAccountFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }

    }
}
