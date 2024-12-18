package com.example.rickandmorty.data.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.rickandmorty.common.Constants.COLLECTION_PATH
import com.example.rickandmorty.common.Constants.E_MAIL
import com.example.rickandmorty.common.Constants.ID
import com.example.rickandmorty.common.Constants.NICKNAME
import com.example.rickandmorty.common.Constants.PHONE_NUMBER
import com.example.rickandmorty.common.Resource
import com.example.rickandmorty.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth, private val firestore: FirebaseFirestore
) {

    suspend fun signIn(email: String, password: String): Resource<Unit> {
        return try {
            val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            // Eğer user null değilse, başarılı bir şekilde giriş yapılmış demektir.
            if (signInTask.user != null) {
                Resource.Success(Unit) // Giriş başarılı
            } else {
                // Herhangi bir kullanıcı bilgisi eksik ya da geçersiz
                Resource.Fail("Sign in failed")
            }

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Şifre veya email hatalı ise bu hata tetiklenir
            Resource.Fail("Incorrect email or password") // Burada hata mesajı veririz

        } catch (e: Exception) {
            // Beklenmedik diğer hatalar
            Resource.Error(e) // Diğer genel hatalar
        }
    }


    suspend fun signUp(
        email: String, password: String, nickname: String, phoneNumber: String
    ): Resource<Unit> {
        return try {
            val signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, password)
                .await() // Email ve password ile kullanıcı kaydını oluşturur.
            return if (signUpTask.user != null) {// Kullanıcı başarılı bir şekilde oluşturulmuşsa burası çalışır.
                val currentUser = signUpTask.user
                val user = hashMapOf(
                    ID to currentUser!!.uid,
                    E_MAIL to email,
                    NICKNAME to nickname,
                    PHONE_NUMBER to phoneNumber
                ) // Kullanıcı bilgileri oluşturulur.
                firestore.collection(COLLECTION_PATH).document(currentUser.uid).set(user)
                    .await() // Kullanıcı bilgileri veritabanına kaydedilir.
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
            val currentUser = firebaseAuth.currentUser // Firebaseye giriş yapmış kullanıcı alınır.
            if (currentUser != null) { // Eğer giriş yapılmıs bir kullanıcı varsa veriler alınır.
                val documentSnapshot =
                    firestore.collection(COLLECTION_PATH).document(currentUser.uid).get().await()
                // Kullanıcı bilgisini ilgili koleksiyondaki kullanıcı belgesini almak için get metodu kullanır.Asenkron olarak yapılır ve await() sayesinde işlem tamamlanana kadar beklenir.
                val user = UserModel(
                    email = documentSnapshot.getString(E_MAIL),
                    nickname = documentSnapshot.getString(NICKNAME),
                    phoneNumber = documentSnapshot.getString(PHONE_NUMBER)
                ) // DocumentSnapshot üzerinden alınan verileri kullanarak bir usermodel olusturulur.
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





