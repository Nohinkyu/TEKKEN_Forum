package com.nik.tkforum.data.source.remote.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseData {

    var user: FirebaseUser? = null
    var token: String? = null

    fun setUserInfo() {
        user = FirebaseAuth.getInstance().currentUser
    }

    fun getUserToken() {
        user?.let {
            it.getIdToken(true)
                .addOnCompleteListener { task ->
                    val idToken = task.result.token
                    token = idToken
                }
        }
        Log.d("getToken", token.toString())
    }
}