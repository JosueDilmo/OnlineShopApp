package com.josue.onlineshopapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.josue.onlineshopapp.R
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

    //clickable components in login screen are login button, forgot password, register text and don't have an account?
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.forgot_password -> {
                    //todo - forgot password act
                }

                R.id.btn_login -> {
                    loginRegisteredUser()

                }

                R.id.register_now -> {
                    //launch register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }
        }

    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_email).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }

    }

    private fun loginRegisteredUser(){

        if(validateLoginDetails()) {

            //progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //get the info from editText and trim the space
            val email = findViewById<EditText>(R.id.et_email).text.toString().trim { it <= ' ' }
            val password = findViewById<EditText>(R.id.et_password).text.toString().trim{ it <= ' ' }

            //log in using firebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->

                    //progress dialog
                    hideProgressDialog()

                    if(task.isSuccessful) {
                        //todo - send user to main act
                        showErrorSnackBar("You are logged in successfully", false)
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }


}


