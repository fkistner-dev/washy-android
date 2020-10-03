package com.kilomobi.washy

import io.realm.RealmObject
import io.realm.RealmList
import io.realm.annotations.PrimaryKey

open class __Realm : RealmObject() {

    @PrimaryKey
    var id: Long = 0
    var permissions: RealmList<__Permission> = RealmList()

}
