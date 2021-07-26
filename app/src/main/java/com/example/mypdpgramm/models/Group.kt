package com.example.mypdpgramm.models

import java.io.Serializable

class Group  : Serializable {
    var id: String? = null
    var name: String? = null
    var usersCount: Int? = null


    constructor()
    constructor(id: String?, name: String?, usersCount: Int?) {
        this.id = id
        this.name = name
        this.usersCount = usersCount
    }
}