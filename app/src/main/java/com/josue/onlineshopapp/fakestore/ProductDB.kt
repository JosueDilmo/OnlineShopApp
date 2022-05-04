package com.josue.onlineshopapp.fakestore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.josue.onlineshopapp.models.Products


@Database(
    entities = [Products::class],
    version = 2,
    exportSchema=false
)

abstract class ProductDB: RoomDatabase() {

    abstract fun getArticleDao(): ProductDAO

    companion object {
        @Volatile
        private var instance: ProductDB? = null
        private val LOCK = Any()

        val migration_1_2: Migration =object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE products ADD COLUMN amt TEXT DEFAULT '1'")
            }

        }

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDB::class.java,
                "product_db.db"
            )
                .addMigrations(migration_1_2)
                .build()
    }
}