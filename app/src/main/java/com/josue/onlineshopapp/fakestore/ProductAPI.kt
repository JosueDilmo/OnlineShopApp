package com.josue.onlineshopapp.fakestore

import com.josue.onlineshopapp.models.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {

    @GET("/products")
    suspend fun getProducts(): Response<List<Products>>

    @GET("/products/category/{name}")
    suspend fun searchProducts(
        @Path("name") name:String
    ): Response<List<Products>>
}