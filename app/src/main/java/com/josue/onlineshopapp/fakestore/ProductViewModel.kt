package com.josue.onlineshopapp.fakestore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josue.onlineshopapp.models.Products
import com.josue.onlineshopapp.utils.Resource
import kotlinx.coroutines.*
import retrofit2.Response
import java.math.BigDecimal

class ProductViewModel (val productsRepository: ProductRepo
): ViewModel() {

    val Getproducts: MutableLiveData<Resource<List<Products>>> = MutableLiveData()

    val price: MutableLiveData<BigDecimal> = MutableLiveData()

    val SearchProducts: MutableLiveData<Resource<List<Products>>> = MutableLiveData()

    init{
        viewModelScope.launch {
            val list=allproducts().await()
            if(list.isNotEmpty()){
                var ans:BigDecimal=0.toBigDecimal()
                for(i in list){
                    val a1=i.amt?.toInt()!!
                    val a2=i.price!!
                    val adder=(a1*a2).toBigDecimal()
                    ans=ans.plus(adder)
                }
                price.value=ans
            }
            else{
                price.value= BigDecimal.valueOf(0.0)
            }
        }
        getProducts()
    }

    fun increase(Newprice:BigDecimal){
        price.value = price.value?.plus(Newprice)
    }

    fun decrease(Newprice:BigDecimal){
        price.value=price.value?.minus(Newprice)
    }

    fun deletePrice(Amount:Int,Newprice:Double){
        val DeletePrice:BigDecimal=(Newprice*Amount).toBigDecimal()
        price.value=price.value?.minus(DeletePrice)
    }

    fun getProducts()=viewModelScope.launch{
        Getproducts.postValue(Resource.Loading())
        val response= productsRepository.getproducts()
        Getproducts.postValue(handleGetProducts(response))
    }

    fun searchProducts(name:String)=viewModelScope.launch{
        SearchProducts.postValue(Resource.Loading())
        val response= productsRepository.searchproducts(name)
        SearchProducts.postValue(handleSearchProducts(response))

    }

    private fun handleGetProducts(response: Response<List<Products>>):Resource<List<Products>>{
        if(response.isSuccessful){
            response.body()?.let{
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchProducts(response: Response<List<Products>>):Resource<List<Products>>{
        if(response.isSuccessful){
            response.body()?.let{
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun upsert(products: Products)=viewModelScope.launch {
        productsRepository.upsert(products)
    }

    fun delete(products: Products)=viewModelScope.launch {
        productsRepository.delete(products)
    }

    fun getAllProducts()= productsRepository.getAllProducts()


    fun checkid(Id:Int)=viewModelScope.async {
        productsRepository.checkid(Id)
    }


    fun allproducts()=viewModelScope.async{
        productsRepository.allproducts()
    }
}
