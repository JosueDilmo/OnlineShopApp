package com.josue.onlineshopapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.josue.onlineshopapp.ui.activities.LoginActivity
import com.josue.onlineshopapp.ui.activities.RegisterActivity
import com.josue.onlineshopapp.ui.activities.UserProfileActivity
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.utils.Constants

class FirestoreClass {

    //access Cloud Firestore instance
    private val mFireStore = FirebaseFirestore.getInstance()

    //function to make an entry of the registered user in the FireStore database
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        //"users" is collection name.
        //if the collection is already created then it will not create the same one again
        mFireStore.collection(Constants.USERS)
            //document ID for users fields
            //the document is the User ID
            .document(userInfo.id)
            //userInfo are Field and the SetOption is set to merge instead of replacing the fields
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //call function of base activity for transferring the result to it
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    //function to get the user ID from FirebaseAuth
    fun getCurrentUserID(): String {

        //currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //variable to assign the currentUserID if it isn't null or else it will be blank
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        //returning the userID from the FirebaseAuth
        return currentUserID
    }

    //function to get user details from activity
    fun getUserDetails(activity: Activity){

        //passing the collection which has the data
        mFireStore.collection(Constants.USERS)
            //document ID to get the Fields of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                //document received and converted into User object
                val user = document.toObject(User::class.java)!!

                //store preferences
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.ONSHOPPES_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key:value = logged_in_username: firstName lastName
                editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                //call function and transfer result to it
                when (activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e( activity.javaClass.simpleName,"Error while getting user details.", e )
            }




    }

    //function to update the user profile data into the FireStore database
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error while updating user details", e)
            }

    }


}


