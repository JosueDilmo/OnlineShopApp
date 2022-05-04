package com.josue.onlineshopapp.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.josue.onlineshopapp.R

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

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
        //function to send email to the user to reset password
        resetPasswordEmail()
    }
    //function to send email to the user to reset password
    private fun resetPasswordEmail(){
        //setting up the toolbar with back button
        val toolbarForgotAct = findViewById<Toolbar>(R.id.toolbar_forgot_password_activity)

        setSupportActionBar(toolbarForgotAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarForgotAct.setNavigationOnClickListener {onBackPressed()}

        //button submit which will send the email
        findViewById<Button>(R.id.btn_submit).setOnClickListener{
            val email = findViewById<EditText>(R.id.et_email_forgot_pw).text.toString().trim { it <= ' ' }
            //checking if the email is not empty
            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                //send password reset email with FirebaseAuth
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()
                        if (task.isSuccessful) {
                            //show toast message and finish the Forgot Password activity
                            Toast.makeText(this@ForgotPasswordActivity, resources.getString(R.string.email_sent_successful),
                            Toast.LENGTH_LONG).show()

                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }

        }
    }

}