package com.example.mypdpgramm.models

import java.io.Serializable

class Message :Serializable{
    var id: String? = null
    var text: String? = null
    var time: String? = null
    var from: String? = null
    var to: String? = null



    constructor()
    constructor(id: String?, text: String?, time: String?, from: String?, to: String?) {
        this.id = id
        this.text = text
        this.time = time
        this.from = from
        this.to = to
    }

}