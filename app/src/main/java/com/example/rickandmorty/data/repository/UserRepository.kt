package com.example.rickandmorty.data.repository


import com.example.rickandmorty.common.Constants.COLLECTION_PATH
import com.example.rickandmorty.common.Constants.E_MAIL
import com.example.rickandmorty.common.Constants.ID
import com.example.rickandmorty.common.Constants.NICKNAME
import com.example.rickandmorty.common.Constants.PHONE_NUMBER
import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore
) {

    suspend fun signIn(email: String, password: String): Resource<Unit> {
        return try {
            val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password).await()


            if (signInTask.user != null) {
                Resource.Success(Unit)
            } else {

                Resource.Fail("Sign in failed")
            }

        } catch (e: FirebaseAuthInvalidCredentialsException) {

            Resource.Fail("Incorrect email or password")
        } catch (e: Exception) {

            Resource.Error(e)
        }
    }


    suspend fun signUp(
        email: String, password: String, nickname: String, phoneNumber: String
    ): Resource<Unit> {
        return try {
            val signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return if (signUpTask.user != null) {
                val currentUser = signUpTask.user
                val user = hashMapOf(
                    ID to currentUser!!.uid,
                    E_MAIL to email,
                    NICKNAME to nickname,
                    PHONE_NUMBER to phoneNumber
                )
                firestore.collection(COLLECTION_PATH).document(currentUser.uid).set(user).await()
                Resource.Success(Unit)

            } else {
                Resource.Fail("Sign up failed")
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getUserInfo(): Resource<UserModel> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val documentSnapshot =
                    firestore.collection(COLLECTION_PATH).document(currentUser.uid).get().await()

                val user = UserModel(
                    email = documentSnapshot.getString(E_MAIL),
                    nickname = documentSnapshot.getString(NICKNAME),
                    phoneNumber = documentSnapshot.getString(PHONE_NUMBER)
                )
                Resource.Success(user)

            } else {
                Resource.Fail("User not found")
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}





