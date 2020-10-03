package com.kilomobi.washy

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class __Class : RealmObject() {

    @PrimaryKey
    var name: String = ""
    var permissions: RealmList<__Permission> = RealmList()

}
