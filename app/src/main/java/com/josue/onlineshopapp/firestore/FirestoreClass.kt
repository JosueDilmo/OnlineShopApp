package com.josue.onlineshopapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.josue.onlineshopapp.models.Address
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.models.Order
import com.josue.onlineshopapp.models.User
import com.josue.onlineshopapp.ui.activities.*
import com.josue.onlineshopapp.ui.fragments.DashboardFragment
import com.josue.onlineshopapp.ui.fragments.OrdersFragment
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

                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity -> {
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

    //function to upload products to Firebase
    fun uploadProductDetails (activity: ProductDetailsActivity, addToCart: CartItem  ){
        val productID = addToCart.product_id
        mFireStore.collection(Constants.CART_ITEMS)
            .document(productID.toString())
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error uploading product to cart",
                    e)
            }
    }


    fun getProductsList (fragment: Fragment){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USERS_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val cartList: ArrayList<CartItem> = ArrayList()
                for (i in document.documents) {

                    val productList = i.toObject(CartItem::class.java)!!

                    productList.product_id = i.id.toInt()
                        cartList.add(productList)

                }
                when (fragment){
                    is DashboardFragment ->{
                        fragment.successProductsListFromFireStore(cartList)
                    }

                }
            }
    }

    fun getCartList(activity: Activity){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USERS_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val cartList: ArrayList<CartItem> = ArrayList()
                for (i in document.documents) {

                    val cartItem = i.toObject(CartItem::class.java)

                    cartItem!!.product_id = i.id.toInt()
                    cartList.add(cartItem)
                }

                when (activity) {
                    is CartListActivity ->{
                        activity.successCartItemList(cartList)
                    }
                }

                when(activity){
                    is CheckoutActivity ->{
                        activity.successCartListFromFirestore(cartList)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error while getting cart list",
                    e
                )

                when (activity) {
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName, "Error while getting cart list",
                    e
                )
            }
    }

    fun updateMyCart (context: Context, cart_id: Int, itemHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id.toString())
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context) {
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {  e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getOrdersList (fragment: OrdersFragment){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USERS_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val orderList: ArrayList<Order> = ArrayList()
                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!

                    orderItem.id = i.id
                    orderList.add(orderItem)
                }
                fragment.populateOrdersList(orderList)

            }
            .addOnFailureListener { e->
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting orders list.",
                    e
                )
            }
    }

    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address){
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                "Error while adding the address.",
                    e
                )
            }
    }

    fun getAddressList (activity: AddressListActivity){
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USERS_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val addressList: ArrayList<Address> = ArrayList()
                for (i in document.documents) {
                    val addresses = i.toObject(Address::class.java)!!
                    addresses.id = i.id
                    addressList.add(addresses)
                }
                activity.successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while getting the address list.",
                    e
                )
            }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId: String) {

        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the Address.",
                    e
                )
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: Int){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USERS_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener{ document ->
                //Log.e("add on success listener", "add on success listener")
                if (document.documents.size == 0){
                    //Log.e("if state", "if state")
                    activity.productNotInCart()
                } else {
                    //Log.e("else state", "else state")
                    for (i in document.documents){
                        //Log.e("for loop", "for loop")
                        val cartList = i.toObject(CartItem::class.java)
                            if (cartList!!.product_id == i.id.toInt()){
                                //Log.e(" 2 if state", "2 if state")
                                activity.productExistsInCart()
                            } else {
                                //Log.e("2 else state", "2 else state")
                                activity.productNotInCart()
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while checking cart list",
                    e
                )
            }

    }

    fun placeOrder (activity: CheckoutActivity, order: Order) {
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order.",
                    e
                )
            }
    }

    fun updateAll(activity: CheckoutActivity, cartList: ArrayList<CartItem>) {
        val writeBatch = mFireStore.batch()

        for (cart in cartList) {
            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cart.product_id.toString())
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.updateAllSuccess()

        }.addOnFailureListener { e ->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName,
                "Error while updating all the details after order placed.",
                e
            )
        }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while checking cart list",
                    e
                )

            }
    }

    fun deleteProductFromCart(context: Context, productId: Int){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(productId.toString())
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity ->{
                        context.itemRemovedSuccess()
                    }
                }


            }
            .addOnFailureListener { e ->
                when (context) {
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(
                    context.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }



    fun deleteProduct(fragment: DashboardFragment, productId: Int) {

        mFireStore.collection(Constants.CART_ITEMS)
            .document(productId.toString())
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e ->

                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

}


