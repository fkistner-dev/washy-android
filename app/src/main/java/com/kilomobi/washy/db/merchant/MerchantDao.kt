package com.kilomobi.washy.db.merchant

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MerchantDao {
    @Query("SELECT * FROM merchant")
    fun getAll(): List<Merchant>

    @Insert
    fun insertAll(vararg merchant: Merchant)

    @Delete
    fun delete(merchant: Merchant)
}