package com.example.mypdpgramm.models

import java.io.Serializable

class GroupMessage  : Serializable {
    var text: String? = null
    var time: String? = null
    var from: String? = null
    var groupId: String? = null




    constructor()
    constructor(text: String?, time: String?, from: String?, groupId: String?) {
        this.text = text
        this.time = time
        this.from = from
        this.groupId = groupId
    }
}