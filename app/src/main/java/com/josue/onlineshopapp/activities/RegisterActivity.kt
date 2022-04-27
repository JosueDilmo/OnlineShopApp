package com.josue.onlineshopapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.josue.onlineshopapp.R

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        setupActionBar()

        findViewById<TextView>(R.id.to_login).setOnClickListener{
            onBackPressed()
        }

        findViewById<Button>(R.id.btn_register).setOnClickListener{
            registerUser()
        }

    }

    private fun setupActionBar(){
        //setting up the toolbar with back button
        val toolbarRegisterAct = findViewById<Toolbar>(R.id.toolbar_register_activity)

        setSupportActionBar(toolbarRegisterAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarRegisterAct.setNavigationOnClickListener {onBackPressed()}
    }


     //function to validate the entries of a new user.
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_first_name).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_last_name).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_email).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_confirm_password).text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' ' } != findViewById<TextView>(R.id.et_confirm_password).text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !findViewById<CheckBox>(R.id.check_terms_and_conditions).isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                true
            }
        }
    }

    //function to register the user with email and password using FirebaseAuth.
    private fun registerUser() {

        //check with validateRegisterDetails if the entries are valid or not.
        if (validateRegisterDetails()) {

            //show progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = findViewById<TextView>(R.id.et_email).text.toString().trim { it <= ' ' }
            val password: String = findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' ' }

            //create an instance and register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        //hide progress dialog
                        hideProgressDialog()

                        //if the registration is successfully done
                        if (task.isSuccessful) {
                            //Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            //sending the user back to login act
                            FirebaseAuth.getInstance().signOut()
                            finish()

                        } else {
                            //if the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

}