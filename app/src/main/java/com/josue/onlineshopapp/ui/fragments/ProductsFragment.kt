package com.josue.onlineshopapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.ui.adapters.ProductAdapter
import com.josue.onlineshopapp.fakestore.ProductViewModel
import com.josue.onlineshopapp.ui.activities.DashboardActivity
import com.josue.onlineshopapp.utils.Resource

class ProductsFragment : Fragment(R.layout.fragment_products){

    lateinit var mViewModel: ProductViewModel
    lateinit var mProductadapter: ProductAdapter

    val TAG="ProductFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = (activity as DashboardActivity).mViewModel

        setRecyclerview()

        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        etSearch.addTextChangedListener { editable->
            val category=editable.toString()
            if(category=="electronics" || category=="jewelery" || category=="men's clothing" || category=="women's clothing"){
                mViewModel.searchProducts(category)
            }

        }

        mViewModel.SearchProducts.observe(viewLifecycleOwner, Observer{

            val paginationProgressBar = view.findViewById<ProgressBar>(R.id.progressBar)

            when(it){
                is Resource.Success->{
                    paginationProgressBar.visibility= View.INVISIBLE
                    it.data?.let{NewProducts->
                        mProductadapter.differ.submitList(NewProducts)
                    }
                }

                is Resource.Error->{
                    paginationProgressBar.visibility= View.INVISIBLE
                    val message=it.message
                    Log.e(TAG,"an error occurred $message")
                }

                is Resource.Loading->{
                    paginationProgressBar.visibility= View.VISIBLE
                }
            }
        })

        mViewModel.Getproducts.observe(viewLifecycleOwner, Observer{

            val paginationProgressBar = view.findViewById<ProgressBar>(R.id.progressBar)

            when(it){
                is Resource.Success->{
                    paginationProgressBar.visibility= View.INVISIBLE
                    it.data?.let{NewProducts->
                        mProductadapter.differ.submitList(NewProducts)
                    }
                }

                is Resource.Error->{
                    paginationProgressBar.visibility= View.INVISIBLE
                    val message=it.message
                    Log.e(TAG,"an error occured $message")
                }

                is Resource.Loading->{
                    paginationProgressBar.visibility= View.VISIBLE
                }
            }
        })

       // val fabCart = view.findViewById<FloatingActionButton>(R.id.fabCart)
        //fabCart.setOnClickListener {
        //    findNavController(view).navigate(R.id.action_productFragment_to_cartFragment)
        //}
    }

    private fun setRecyclerview(){
        mProductadapter= ProductAdapter(mViewModel)

        view?.findViewById<RecyclerView>(R.id.rvProduct)?.apply{
            adapter= mProductadapter
            layoutManager= LinearLayoutManager(context)
        }
    }

}
