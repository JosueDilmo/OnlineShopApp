package com.josue.onlineshopapp.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.CartItem
import com.josue.onlineshopapp.ui.activities.CartListActivity
import com.josue.onlineshopapp.ui.activities.SettingsActivity
import com.josue.onlineshopapp.ui.adapters.DashboardAdapter

class DashboardFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    fun deleteProduct(productID: Int){
        showAlertDialogToDeleteProduct(productID)
    }


    private fun showAlertDialogToDeleteProduct(productID: Int) {

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(resources.getString(R.string.delete_dialog_title))

        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteProduct(this, productID)

            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    fun productDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(requireActivity(),
        resources.getString(R.string.product_delete_success),
        Toast.LENGTH_SHORT
        ).show()

        getProductListFromFireStore()
    }


    fun successProductsListFromFireStore(productList: ArrayList<CartItem>){
        hideProgressDialog()
           val productsRv = view?.findViewById<RecyclerView>(R.id.rv_my_product_items)
           val noProducts = view?.findViewById<TextView>(R.id.tv_no_products_found)

       if (productList.size > 0) {
           productsRv?.visibility = View.VISIBLE

           noProducts?.visibility = View.GONE

           productsRv?.layoutManager = LinearLayoutManager(activity)
           productsRv?.setHasFixedSize(true)

           val adapterProducts = DashboardAdapter(requireActivity(), productList, this)
           productsRv?.adapter = adapterProducts
       } else {
           productsRv?.visibility = View.GONE
           noProducts?.visibility = View.VISIBLE
       }
    }


    private fun getProductListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }


    override fun onResume() {
        super.onResume()

        getProductListFromFireStore()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id){

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
            }

            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }



}