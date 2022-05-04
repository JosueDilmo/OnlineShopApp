package com.josue.onlineshopapp.ui.activities

import android.app.Dialog
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.josue.onlineshopapp.R
import android.os.Handler
import android.widget.ImageView
import com.squareup.picasso.Picasso

open class BaseActivity : AppCompatActivity() {

    //double back pressed to exit
    private var doubleBackToExitPressedOnce = false

    private lateinit var mUserProfileImage: ImageView


    //global instance for progress dialog
    private lateinit var mProgressDialog: Dialog

    //function to show the success and error messages in snack bar component
    fun showErrorSnackBar(message: String, errorMessage: Boolean){

        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError))
        }else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess))
        }
        snackBar.show()
    }

    //picasso to get random user pics
    fun userProfilePhoto(): ImageView? {
        mUserProfileImage = findViewById(R.id.iv_user_photo)
        Picasso.get()
            .load("https://thispersondoesnotexist.com/image")
            .fit()
            .centerInside()
            .into(mUserProfileImage)
        return mUserProfileImage
    }

    //function used to show the progress dialog message to user
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        //Set the screen content from a layout resource
        //The resource will be inflated, adding all top-level views to the screen
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.progress_text).text = text

        //user can't cancel
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen
        mProgressDialog.show()
    }

    //function is used to dismiss the progress dialog if it is visible to user
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    //fun to for back pressed to close
    fun doubleBackToExit(){

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show()

        //if pressed back twice in 2s app will be closed
        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }
}