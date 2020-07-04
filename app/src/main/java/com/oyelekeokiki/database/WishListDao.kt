package com.oyelekeokiki.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oyelekeokiki.model.Product
import kotlinx.coroutines.flow.Flow


@Dao
interface WishListDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addToWishList(it: Product)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWishListProducts(it: List<Product>)

  @Query("DELETE FROM Product WHERE id = :productId")
  suspend fun removeFromWishList(productId: Int)

  @Query("UPDATE Product SET stock = stock + :count WHERE ID = :productId")
  suspend fun updateProductStockCount(productId: Int, count: Int)

  @Query("SELECT * FROM Product")
  fun getWishList(): LiveData<List<Product>>

  @Query("SELECT id FROM Product")
  fun getWishListIds(): LiveData<List<Int>>

  @Query("SELECT * FROM Product WHERE id= :productId")
  suspend fun getWishListItemWith(productId: Int): Product

  @Query("DELETE FROM Product")
  suspend fun deleteAll()

  @Query("SELECT count(*) FROM Product")
  fun getWishListCount(): LiveData<Int>

}
