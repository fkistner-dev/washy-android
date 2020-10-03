package com.kilomobi.washy

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class __User : RealmObject() {

    @PrimaryKey
    var id: String = ""
    var role: __Role? = null

}
