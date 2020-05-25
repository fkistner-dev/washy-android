package com.kilomobi.washy.db.dealer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kilomobi.washy.adapter.AdapterClick
import com.kilomobi.washy.recycler.RecyclerItem

@Entity(tableName = Dealer.TABLE_NAME)
data class Dealer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UID)
    val uid: Long = 0,
    @ColumnInfo(name = ID)
    override val id: String? = "0",
    @ColumnInfo(name = NAME)
    var name: String,
    @ColumnInfo(name = PRESENTATION)
    var presentation: String? = null,
    @ColumnInfo(name = LATITUDE)
    var latitude: Float,
    @ColumnInfo(name = LONGITUDE)
    var longitude: Float,
    @ColumnInfo(name = PRICE_RANGE)
    var priceRange: String,
    @ColumnInfo(name = RATING)
    var rating: Float,
    @ColumnInfo(name = SERVICES)
    var isSelected: Boolean = false
) : RecyclerItem, AdapterClick {
    companion object {
        const val TABLE_NAME = "dealer"
        const val UID = "uid"
        const val ID = "id"
        const val NAME = "name"
        const val PRESENTATION = "presentation"
        const val SERVICES = "services"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val PRICE_RANGE = "price_range"
        const val RATING = "rating"
    }
}