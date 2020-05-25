package com.kilomobi.washy.db.dealer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListDealerDao {
    @Query("SELECT * FROM dealer")
    fun getAll(): List<Dealer>

    @Insert
    fun insertAll(vararg dealer: Dealer)

    @Delete
    fun delete(dealer: Dealer)
}