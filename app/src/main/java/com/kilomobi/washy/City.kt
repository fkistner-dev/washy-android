package com.kilomobi.washy

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class City : RealmObject() {

    @PrimaryKey
    var id: Long = 0
    var countryCode: String = ""
    var zipCode: String = ""
    var name: String = ""
    var slug: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var accuracy: Long = 0

}
