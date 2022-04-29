package com.josue.onlineshopapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.utils.Constants
import org.w3c.dom.Text

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //full screen
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //event assigned to forgot password text
        findViewById<TextView>(R.id.forgot_password).setOnClickListener(this)
        //event assigned to login button
        findViewById<Button>(R.id.btn_login).setOnClickListener(this)
        //event assigned to register text
        findViewById<TextView>(R.id.register_now).setOnClickListener(this)

    }

    fun userLoggedInSuccess(user: User) {

        //progress dialog
        hideProgressDialog()

        //print user details
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        //checking if profile is incomplete if true send to UserProfileActivity
        if (user.profileComplete == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            //if completed redirect user to Main Screen
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()

    }

    //clickable components in login screen are Login button, Forgot Password and Register text
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.forgot_password -> {
                    //launch forgot password act when the user clicks on the text
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    loginRegisteredUser()

                }

                R.id.register_now -> {
                    //launch register act when the user clicks on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

    private fun loginRegisteredUser(){

        if(validateLoginDetails()) {

            //progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //get the info from editText and trim the space
            val email = findViewById<EditText>(R.id.et_login_email).text.toString().trim { it <= ' ' }
            val password = findViewById<EditText>(R.id.et_login_password).text.toString().trim{ it <= ' ' }

            //log in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->

                    if(task.isSuccessful) {

                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        //progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    //function to validate email and password
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_login_email).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_login_password).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }

    }

}


