package com.kilomobi.washy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kilomobi.washy.db.merchant.Merchant
import com.kilomobi.washy.db.merchant.MerchantDao

@Database(entities = [(Merchant::class)], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun listMerchantDao(): MerchantDao
}