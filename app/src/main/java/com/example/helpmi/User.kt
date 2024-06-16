package com.example.helpmi


data class User(val id: Int, val username: String, val email : String? = null) {
    fun getID(): Int {
        return id
    }

}
