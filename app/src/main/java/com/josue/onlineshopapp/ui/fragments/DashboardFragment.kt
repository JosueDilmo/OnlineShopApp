package com.josue.onlineshopapp.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.firestore.FirestoreClass
import com.josue.onlineshopapp.models.ProductsFirestore
import com.josue.onlineshopapp.ui.activities.SettingsActivity
import com.josue.onlineshopapp.ui.adapters.DashboardAdapter

class DashboardFragment : BaseFragment() {

    //fun to inflate the option menu in fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //option menu in fragment
        setHasOptionsMenu(true)

    }

    fun deleteProduct(productID: String){
        showAlertDialogToDeleteProduct(productID)
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

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


    fun successProductsListFromFireStore(productList: ArrayList<ProductsFirestore>){
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


    //fun for the views
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    //fun to inflate the Dashboard menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //fun to handle the action items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id){

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }



}