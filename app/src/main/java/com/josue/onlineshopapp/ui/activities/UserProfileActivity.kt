package com.josue.onlineshopapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.utils.Constants


class UserProfileActivity : BaseActivity(), View.OnClickListener {

   private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //getting extra details of the user
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //user details from intent as ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        //picasso for the random pics
        userProfilePhoto()

        //setting text views
        findViewById<EditText>(R.id.et_first_name).setText(mUserDetails.firstName)
        findViewById<EditText>(R.id.et_last_name).setText(mUserDetails.lastName)
        findViewById<EditText>(R.id.et_email).isEnabled = false
        findViewById<EditText>(R.id.et_email).setText(mUserDetails.email)

        //checking if profile complete
        if (mUserDetails.profileComplete == 0) {

            //setting profile title
            findViewById<TextView>(R.id.tv_title).text = resources.getString(R.string.title_complete_profile)

            //setting profile
            findViewById<EditText>(R.id.et_first_name).isEnabled = false
            findViewById<EditText>(R.id.et_last_name).isEnabled = false


        } else {
            //set back button in toolbar
            setupActionBar()

            findViewById<TextView>(R.id.tv_title).text = resources.getString(R.string.title_edit_profile)

            if (mUserDetails.mobile != 0L) {
                findViewById<EditText>(R.id.et_mobile_number).setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                findViewById<RadioButton>(R.id.rb_male).isChecked = true
            } else {
                findViewById<RadioButton>(R.id.rb_female).isChecked = true

            }

        }

        findViewById<Button>(R.id.btn_submit).setOnClickListener(this@UserProfileActivity)

    }

    //toolbar
    private fun setupActionBar() {
        val toolbarProfileAct = findViewById<Toolbar>(R.id.toolbar_user_profile_activity)

        setSupportActionBar(toolbarProfileAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarProfileAct.setNavigationOnClickListener { onBackPressed() }
    }

    //enable Views and save
    override fun onClick(v: View?) {
       if (v != null) {
           when (v.id) {

               R.id.btn_submit -> {

                   if(validateUserProfileDetails()) {

                       //progress dialog
                       showProgressDialog(resources.getString(R.string.please_wait))

                       //update profile fun
                       updateUserProfileDetails()

                   }
               }
           }
       }
    }

    //function for the result if success and proceeding after updating the user details
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        // Redirect to the Dashboard Screen
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    //function to update user profile details to the FireStore
    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        //checking and change names if needed
        val firstName = findViewById<TextView>(R.id.et_first_name).text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = findViewById<TextView>(R.id.et_last_name).text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        //checking and changing gender if needed
        val gender = if (findViewById<RadioButton>(R.id.rb_male).isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        //checking and changing mobile number if needed
        val mobileNumber = findViewById<TextView>(R.id.et_mobile_number).text.toString().trim { it <= ' '}
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        //profile complete
        userHashMap[Constants.COMPLETE_PROFILE] = 1

        //updating in FireStore
        FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    //validate User Details in this case Mobile Number
    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_mobile_number).text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

}