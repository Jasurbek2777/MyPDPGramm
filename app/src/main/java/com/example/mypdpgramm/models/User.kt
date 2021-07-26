package com.example.mypdpgramm.models

import java.io.Serializable

class User : Serializable {
    var name: String? = null
    var photoUrl: String? = null
    var gmail: String? = null
    var uid: String? = null
    var lastSeen: String? = ""
    var online: Boolean? = false
    var signed: Boolean? = null
    var lastMessage: Message? =Message()



    constructor()
    constructor(
        name: String?,
        photoUrl: String?,
        gmail: String?,
        uid: String?,
        lastSeen: String?,
        online: Boolean?,
        signed: Boolean?,
        lastMessage: Message?
    ) {
        this.name = name
        this.photoUrl = photoUrl
        this.gmail = gmail
        this.uid = uid
        this.lastSeen = lastSeen
        this.online = online
        this.signed = signed
        this.lastMessage = lastMessage
    }

    constructor(
        name: String?,
        photoUrl: String?,
        gmail: String?,
        uid: String?,
        lastSeen: String?,
        online: Boolean?,
        signed: Boolean?
    ) {
        this.name = name
        this.photoUrl = photoUrl
        this.gmail = gmail
        this.uid = uid
        this.lastSeen = lastSeen
        this.online = online
        this.signed = signed
    }


}