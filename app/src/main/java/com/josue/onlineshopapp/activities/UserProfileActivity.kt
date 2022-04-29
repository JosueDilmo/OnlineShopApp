package com.josue.onlineshopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.utils.Constants
import com.squareup.picasso.Picasso


class UserProfileActivity : BaseActivity(), View.OnClickListener {

   private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //picasso for the random pics
        val imageView = findViewById<ImageView>(R.id.iv_user_photo)
        Picasso.get()
            .load("https://thispersondoesnotexist.com/image")
            .into(imageView)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //user details from intent as ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        //field unable for changes
        //getting user details from User class
        findViewById<EditText>(R.id.et_first_name).isEnabled = false
        findViewById<EditText>(R.id.et_first_name).setText(mUserDetails.firstName)

        findViewById<EditText>(R.id.et_last_name).isEnabled = false
        findViewById<EditText>(R.id.et_last_name).setText(mUserDetails.lastName)

        findViewById<EditText>(R.id.et_email).isEnabled = false
        findViewById<EditText>(R.id.et_email).setText(mUserDetails.email)


        findViewById<Button>(R.id.btn_submit).setOnClickListener(this@UserProfileActivity)

    }

    //enable Views
    override fun onClick(v: View?) {
       if (v != null) {
           when (v.id) {

               R.id.btn_submit -> {

                   if(validateUserProfileDetails()) {

                       //Hashmap
                       val userHashMap = HashMap<String, Any>()

                       //format Mobile Number
                       val mobileNumber = findViewById<EditText>(R.id.et_mobile_number).text.toString().trim { it <= ' '}

                       //checking which Gender is selected
                       val gender = if (findViewById<RadioButton>(R.id.rb_male).isChecked) {
                           Constants.MALE
                       } else {
                           Constants.FEMALE
                       }

                       //checking if not empty
                       if (mobileNumber.isNotEmpty()) {
                           //key -- value
                           userHashMap[Constants.MOBILE] = mobileNumber.toLong()
                       }

                       //key -- value
                       userHashMap[Constants.GENDER] = gender

                       //progress dialog
                       showProgressDialog(resources.getString(R.string.please_wait))

                       FirestoreClass().updateUserProfileData(this@UserProfileActivity, userHashMap)

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

        // Redirect to the Main Screen
        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
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