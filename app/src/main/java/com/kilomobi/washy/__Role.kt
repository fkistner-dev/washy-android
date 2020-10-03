package com.kilomobi.washy

import io.realm.RealmObject
import io.realm.RealmList
import io.realm.annotations.PrimaryKey

open class __Role : RealmObject() {

    @PrimaryKey
    var name: String = ""
    var members: RealmList<__User> = RealmList()

}
