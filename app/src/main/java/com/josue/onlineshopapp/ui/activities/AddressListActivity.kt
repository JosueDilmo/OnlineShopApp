package com.josue.onlineshopapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.Address
import com.josue.onlineshopapp.ui.adapters.AddressListAdapter
import com.josue.onlineshopapp.utils.Constants
import com.josue.onlineshopapp.utils.SwipeToDeleteCallback
import com.josue.onlineshopapp.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setupActionBar()

        findViewById<TextView>(R.id.tv_add_address).setOnClickListener{
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        getAddressList()

        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if (mSelectAddress){
            findViewById<TextView>(R.id.tv_title).text = resources.getString(R.string.title_select_address)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            getAddressList()
        }
    }

    override fun onResume() {
        super.onResume()
        //getAddressList()
    }

    private fun getAddressList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressList(this)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        hideProgressDialog()
        if (addressList.size > 0){
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.GONE
            val recycleView = findViewById<RecyclerView>(R.id.rv_address_list)
            recycleView.visibility = View.VISIBLE
            recycleView.layoutManager = LinearLayoutManager(this)
            recycleView.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList, mSelectAddress)
            recycleView.adapter = addressAdapter

            if (!mSelectAddress){
            val editSwipeHandler = object: SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = recycleView.adapter as AddressListAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition)
                }
            }


            val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    showProgressDialog(resources.getString(R.string.please_wait))

                    FirestoreClass().deleteAddress(this@AddressListActivity,
                        addressList[viewHolder.adapterPosition].id)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(recycleView)

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(recycleView)
            }



        } else {
            findViewById<RecyclerView>(R.id.rv_address_list).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

    private fun setupActionBar(){

        val toolbarAddressAct = findViewById<Toolbar>(R.id.toolbar_address_list_activity)

        setSupportActionBar(toolbarAddressAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_arrow_back_24)
        }

        toolbarAddressAct.setNavigationOnClickListener { onBackPressed() }
    }
}