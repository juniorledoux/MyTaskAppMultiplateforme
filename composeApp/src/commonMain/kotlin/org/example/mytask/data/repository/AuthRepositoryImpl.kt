package org.example.mytask.data.repository

import dev.gitlive.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.mytask.core.launchWithAwait
import org.example.mytask.data.modules.FirebaseModule
import org.example.mytask.domain.models.User
import org.example.mytask.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val firebaseModule: FirebaseModule,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

) : AuthRepository {
    override val currentUserId: String = firebaseModule.auth().currentUser?.uid.toString()

    override
    val isAuthenticated: Boolean =
        firebaseModule.auth().currentUser != null && firebaseModule.auth().currentUser?.isAnonymous == false

    override val hasUser: Boolean
        get() = firebaseModule.auth().currentUser != null

    override
    val currentUser: Flow<User> = firebaseModule.auth().authStateChanged.map {
        it?.let { User(it.uid, it.isAnonymous, it.email.toString()) } ?: User()
    }

    override suspend fun loginAsAnonymous() {
        launchWithAwait(scope) {
            firebaseModule.auth().signInAnonymously()
        }
    }

    override suspend fun login(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty())
            try {
                launchWithAwait(scope) {
                    firebaseModule.auth().signInWithEmailAndPassword(email, password)
                }
            } catch (_: Exception) {
            }
    }

    override suspend fun register(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            try {
                launchWithAwait(scope) {
                    // Check if user data already exists
                    val userExists = firebaseModule.firestore().collection("users")
                        .where { "email".equalTo(email) }
                        .get()
                        .documents.isNotEmpty()
                    if (!userExists) {
                        var newUserCredential = EmailAuthProvider.credential(email, password)
                        newUserCredential = firebaseModule.auth()
                            .createUserWithEmailAndPassword(email, password).credential
                            ?: newUserCredential
                        firebaseModule.auth().currentUser?.linkWithCredential(newUserCredential)
                    } else {
                        // User data exists, link to anonymous account
                        val credential = EmailAuthProvider.credential(email, password)
                        firebaseModule.auth().currentUser?.linkWithCredential(credential)
                    }
                }
                // Handle exceptions
            } catch (_: Exception) {
            }
        }
    }

    override suspend fun logout() {
        launchWithAwait(scope) {
            if (firebaseModule.auth().currentUser?.isAnonymous == true) firebaseModule.auth().currentUser?.delete()
            firebaseModule.auth().signOut()
        }
    }
}