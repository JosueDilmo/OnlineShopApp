package com.josue.onlineshopapp.fakestore

import com.josue.onlineshopapp.models.Products

class ProductRepo(
   private val db:ProductDB
) {
    suspend fun getproducts() = Retrofit.api.getProducts()

    suspend fun searchproducts(name: String) = Retrofit.api.searchProducts(name)

    suspend fun upsert (products: Products) = db.getArticleDao().upsert(products)

    suspend fun delete (products: Products) = db.getArticleDao().delete(products)

    fun getAllProducts() = db.getArticleDao().getallProducts()

    suspend fun checkid(id: Int) = db.getArticleDao().checkid(id)

    suspend fun allproducts() = db.getArticleDao().allProducts()
}