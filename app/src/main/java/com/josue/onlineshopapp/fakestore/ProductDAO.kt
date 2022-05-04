package com.josue.onlineshopapp.fakestore

import androidx.lifecycle.LiveData
import androidx.room.*
import com.josue.onlineshopapp.models.Products


@Dao
interface ProductDAO {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun upsert(products: Products)

    @Delete
    suspend fun delete(products: Products)

    @Query("Select * from products")
    fun getallProducts(): LiveData<List<Products>>

    @Query("Select exists(Select * from products where id=:Id)")
    suspend fun checkid(Id: Int):Int

    @Query("Select * from products")
    suspend fun allProducts():List<Products>
}