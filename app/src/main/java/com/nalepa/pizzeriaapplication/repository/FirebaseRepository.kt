package com.nalepa.pizzeriaapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.data.order.Order
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import java.sql.Date

class FirebaseRepository {

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun createNewUser(user: User) {
        cloud.collection("users")
            .document(user.uid)
            .set(user)

    }

    fun createOrder(order: Order) {
        val currentUser = auth.currentUser?.uid
        cloud.collection("users").document(currentUser!!).collection("cart")
            .get().addOnSuccessListener {
                cloud.collection("users").document(currentUser).collection("order")
                    .add(order)
                    .addOnSuccessListener {
                        it.update("id", it.id)
                    }
                    .addOnFailureListener {
                        Log.d("Repository",it.message.toString())
                    }
            }
    }

    fun getCurrentUserData() : LiveData<User> {
        val cloudResult = MutableLiveData<User>()
        val currentUser = auth.currentUser?.uid

        cloud.collection("users")
            .document(currentUser!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user!!)
            }
            .addOnFailureListener {
                Log.d("Repository",it.message.toString())
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
                Log.d("Repository", it.message.toString())
            }

        return cloudResult
    }

    fun getPizzaDetails(id: String): LiveData<Pizza> {

        val cloudResult = MutableLiveData<Pizza>()
        cloud.collection("menu_demo").document(id).get()
            .addOnSuccessListener {
                val pizzaDetails = it.toObject(Pizza::class.java)
                pizzaDetails?.id = it.id
                cloudResult.postValue(pizzaDetails!!)
            }
            .addOnFailureListener {
                Log.d("Repository", it.message.toString())
            }

        return cloudResult
    }

    fun getCurrentUserItems(): LiveData<List<Item>> {
        val cloudResult = MutableLiveData<List<Item>>()
        val currentUser = auth.currentUser?.uid
        cloud.collection("users").document(currentUser!!).collection("cart")
            .addSnapshotListener { snapshot, e ->
                if(e != null ){
                    Log.w("Repository","Listen failed due to ${e.message.toString()}", )
                }

                val orderItem = snapshot?.map {
                    val item = it.toObject(Item::class.java)
                    item.id = it.reference.id

                    item
                }
                cloudResult.postValue(orderItem!!)
            }
        return cloudResult
    }

    fun addItemToCart(item: Item) {
        val currentUserId = auth.currentUser?.uid
        cloud.collection("users").document(currentUserId!!).collection("cart")
            .add(item)
            .addOnSuccessListener {
                it.update("id", it.id)
            }
            .addOnFailureListener {
                Log.d("Repository", it.message.toString())
            }
    }


    fun updateItemDetails(item: Item) {
        val currentUser = auth.currentUser?.uid

        cloud.collection("users").document(currentUser!!).collection("cart")
            .document(item.id).update(
                "quantity", item.quantity,
                "totalPrice", item.totalPrice)
    }

    fun deleteItem(item: Item) {
        val currentUser = auth.currentUser?.uid

        cloud.collection("users").document(currentUser!!).collection("cart")
            .document(item.id).delete()
    }

    fun logout() {
        auth.signOut()
    }
}