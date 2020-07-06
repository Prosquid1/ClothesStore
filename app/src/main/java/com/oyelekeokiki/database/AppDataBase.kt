package com.oyelekeokiki.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oyelekeokiki.model.Product

@Database(
    entities = [
      Product::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDataBase : RoomDatabase() {
  abstract fun wishListDao(): WishListDao
}
