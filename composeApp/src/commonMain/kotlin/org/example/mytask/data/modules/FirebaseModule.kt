package org.example.mytask.data.modules

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

object FirebaseModule {
    fun auth(): FirebaseAuth = Firebase.auth

    fun firestore(): FirebaseFirestore = Firebase.firestore
}