package com.josue.onlineshopapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.utils.Constants
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class SettingsActivity : BaseActivity(), View.OnClickListener{

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //setting toolbar with back button
        setupActionBar()

        findViewById<TextView>(R.id.tv_edit).setOnClickListener(this)
        findViewById<Button>(R.id.btn_logout).setOnClickListener(this)

    }


    //get user details from Firestore
    private fun getUserDetails(){

        //progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        //user details
        FirestoreClass().getUserDetails(this)
    }

    //display user info
    fun userDetailsSuccess(user: User){

        mUserDetails = user

        hideProgressDialog()

        //picasso for the random pics
        userProfilePhoto()

        findViewById<TextView>(R.id.tv_name).text = "${user.firstName} ${user.lastName}"
        findViewById<TextView>(R.id.tv_gender).text = user.gender
        findViewById<TextView>(R.id.tv_email).text = user.email
        findViewById<TextView>(R.id.tv_mobile_number).text = "${user.mobile}"
    }

    //display on resume
    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    //toolbar
    private fun setupActionBar(){

        val toolbarSettingsAct = findViewById<Toolbar>(R.id.toolbar_settings_activity)

        setSupportActionBar(toolbarSettingsAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarSettingsAct.setNavigationOnClickListener { onBackPressed() }
    }

    //action for the views
    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id){

                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                    //todo check if is closing act
                    finish()
                }


                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


}