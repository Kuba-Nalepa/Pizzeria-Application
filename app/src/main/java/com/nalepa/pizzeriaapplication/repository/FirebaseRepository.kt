package com.nalepa.pizzeriaapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.order.Order
import com.nalepa.pizzeriaapplication.data.order.OrderItem
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
                pizzaDetails?.id = it.reference.id
                cloudResult.postValue(pizzaDetails!!)
            }
            .addOnFailureListener {
                Log.d("Repository", it.message.toString())
            }

        return cloudResult
    }
    fun addItemToUsersOrder(orderItem: OrderItem) {
        val currentUserId = auth.currentUser?.uid
        cloud.collection("users").document(currentUserId!!).collection("order")
            .add(hashMapOf(
                "name" to orderItem.name,
                "size" to orderItem.size,
                "quantity" to orderItem.quantity,
                "price" to orderItem.price
            ))
            .addOnFailureListener {
                Log.d("Repository", it.message.toString())
            }
    }

    fun getCurrentUserOrder(): LiveData<List<OrderItem>> {
            val cloudResult = MutableLiveData<List<OrderItem>>()
            val currentUser = auth.currentUser?.uid
        cloud.collection("users").document(currentUser!!).collection("order")
            .addSnapshotListener { snapshot, e ->
                if(e != null ){
                    Log.w("Repository","Listen failed due to ${e.message.toString()}", )
                }

                val orderItem = snapshot?.map {
                    val item = it.toObject(OrderItem::class.java)
                    item.id = it.reference.id

                    item
                }
                cloudResult.postValue(orderItem!!)
            }
        return cloudResult
    }

    fun updateOrderDetails(orderItem: OrderItem) {
        val currentUser = auth.currentUser?.uid

        cloud.collection("users").document(currentUser!!).collection("order")
            .document(orderItem.id).update("quantity", orderItem.quantity, "price", orderItem.price)
    }

    fun deleteOrderItem(orderItem: OrderItem) {
        val currentUser = auth.currentUser?.uid

        cloud.collection("users").document(currentUser!!).collection("order")
            .document(orderItem.id).delete()
    }
}