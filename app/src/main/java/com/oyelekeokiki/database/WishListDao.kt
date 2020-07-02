package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oyelekeokiki.model.Product


@Dao
interface WishListDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addToWishList(it: Product)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertWishListProducts(it: List<Product>)

  @Query("DELETE FROM Product WHERE id = :productId")
  fun removeFromWishList(productId: Int)

  @Query("SELECT * FROM Product")
  fun getWishList(): LiveData<List<Product>>

  @Query("SELECT * FROM Product WHERE id= :productId")
  fun getWishListItemWith(productId: Int): LiveData<Product>

  @Query("DELETE FROM Product")
  fun deleteAll()

  @Query("SELECT count(*) FROM Product")
  fun getProductsCount(): Int

}
