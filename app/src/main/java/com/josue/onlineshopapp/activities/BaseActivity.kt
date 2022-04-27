package com.josue.onlineshopapp.activities

import android.app.Dialog
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.josue.onlineshopapp.R

open class BaseActivity : AppCompatActivity() {

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


    //function used to show the progress dialog message to user.
    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.progress_text).text = text

        //user can't cancel.
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    //This function is used to dismiss the progress dialog if it is visible to user.
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}