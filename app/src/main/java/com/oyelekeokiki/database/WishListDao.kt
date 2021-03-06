package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oyelekeokiki.model.Product


@Dao
interface WishListDao {
  /** Coroutine functions **/
  @Query("DELETE FROM Product")
  suspend fun deleteAll()

  @Query("SELECT id FROM Product")
  suspend fun getWishListIds(): List<Int>

  @Query("SELECT * FROM Product WHERE id= :productId")
  suspend fun getWishListItemWith(productId: Int): Product

  @Query("SELECT stock FROM Product WHERE ID = :productId")
  suspend fun getProductStockCount(productId: Int): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToWishList(it: Product)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWishListProducts(it: List<Product>)

  @Query("DELETE FROM Product WHERE id = :productId")
  suspend fun removeFromWishList(productId: Int)

  @Query("UPDATE Product SET stock = stock - 1 WHERE ID = :productId")
  suspend fun decrementProductStockCount(productId: Int)

  /** LiveData functions **/
  @Query("SELECT id FROM Product")
  fun getLiveWishListIds(): LiveData<List<Int>>

  @Query("SELECT * FROM Product")
  fun getWishList(): LiveData<List<Product>>

  @Query("SELECT count(*) FROM Product")
  fun getWishListCount(): LiveData<Int>
}
