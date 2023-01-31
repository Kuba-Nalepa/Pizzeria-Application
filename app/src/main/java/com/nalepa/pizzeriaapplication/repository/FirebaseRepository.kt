package com.nalepa.pizzeriaapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.pizza.Pizza

class FirebaseRepository {

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun createNewUser(user: User) {
        cloud.collection("users")
            .document(user.uid)
            .set(user)

    }

    fun getCurrentUserData() : LiveData<User> {
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid

        cloud.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d("repository",it.message.toString())
            }

        return cloudResult
    }

    fun getMenuList() : LiveData<List<Pizza>> {
        val cloudResult = MutableLiveData<List<Pizza>>()

        cloud.collection("menu_demo").get()
            .addOnSuccessListener {
                val menu = it.documents.map { doc ->
                    val pizza = doc.toObject(Pizza::class.java)!!
                    pizza.id = doc.reference.id

                    pizza
                }
                cloudResult.postValue(menu)
            }
            .addOnFailureListener {
                Log.d("repository", it.message.toString())
            }

        return cloudResult
    }

    fun getPizzaDetails(id: String): LiveData<Pizza> {

        val cloudResult = MutableLiveData<Pizza>()
        cloud.collection("menu_demo").document(id).get()
            .addOnSuccessListener {
                val pizzaDetails = it.toObject(Pizza::class.java)
                pizzaDetails?.id = it.reference.id
                cloudResult.postValue(pizzaDetails!!)
            }
            .addOnFailureListener {
                Log.d("GetPizzaDetails exec", it.message.toString())
            }

        return cloudResult
    }
}