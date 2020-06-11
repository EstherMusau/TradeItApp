package com.example.tradeitapp

class UserClass {
    var profileimageurl: Int? = null
    var username: String = ""

    constructor(profileimageurl: Int, username: String) {
        this.profileimageurl = profileimageurl
        this.username = username
    }

    constructor() {}
}