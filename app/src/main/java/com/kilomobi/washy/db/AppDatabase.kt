package com.kilomobi.washy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kilomobi.washy.db.dealer.Dealer
import com.kilomobi.washy.db.dealer.ListDealerDao

@Database(entities = [(Dealer::class)], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun listDealerDao(): ListDealerDao
}