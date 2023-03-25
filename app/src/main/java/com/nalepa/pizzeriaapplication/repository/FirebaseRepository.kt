package com.nalepa.pizzeriaapplication.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.data.order.Order
import com.nalepa.pizzeriaapplication.data.pizza.Pizza

class FirebaseRepository {

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()
    private val currentUser = auth.currentUser?.uid
    private val menu = "menu"
    private val users = "users"
    private val cart = "cart"
    private val order = "order"
    private val favourites = "favourites"

    fun createNewUser(user: User) {
        cloud.collection(users)
            .document(user.uid)
            .set(user)

    }

    fun createOrder(order: Order) {
        cloud.collection(users).document(currentUser!!).collection(cart)
            .get().addOnSuccessListener {
                cloud.collection("users").document(currentUser).collection(this.order)
                    .add(order)
                    .addOnSuccessListener {
                        it.update("id", it.id)
                    }
                    .addOnFailureListener {
                        Log.d("Repository",it.message.toString())
                    }
            }
    }

    fun uploadUserImage(uri: Uri) {
        storage.getReference(users)
            .child("${currentUser}.jpg")
            .putFile(uri)
            .addOnSuccessListener {
                getUserImage(it.storage)
            }
    }

    private fun getUserImage(storage: StorageReference) {
        storage.downloadUrl
            .addOnSuccessListener {
                updateUserImage(it)
            }
    }

    private fun updateUserImage(uri: Uri) {
        cloud.collection(users)
            .document(currentUser!!)
            .update("image", uri.toString())
    }

    fun updateUserName(name: String, surname: String) {
        cloud.collection(users)
            .document(currentUser!!)
            .update(
                "name", name,
                "surname", surname)
    }

    fun getCurrentUserData() : LiveData<User> {
        val cloudResult = MutableLiveData<User>()

        cloud.collection(users)
            .document(currentUser!!)
            .addSnapshotListener { value, error ->
                val user = value?.toObject(User::class.java)
                cloudResult.postValue(user!!)
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

    fun getFavouritePizzasList() : LiveData<List<Pizza>> {
        val cloudResult = MutableLiveData<List<Pizza>>()

        cloud.collection(users).document(currentUser!!).collection(favourites).addSnapshotListener { value, _ ->
            val favourites = value?.documents?.map { doc ->
                val pizza = doc.toObject(Pizza::class.java)!!

                pizza
            }
            cloudResult.postValue(favourites!!)
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
        cloud.collection(users).document(currentUser!!).collection(cart)
            .addSnapshotListener { snapshot, _ ->

                val orderItem = snapshot?.map {
                    val item = it.toObject(Item::class.java)
                    item.id = it.reference.id

                    item
                }
                cloudResult.postValue(orderItem!!)
            }
        return cloudResult
    }

    fun retrieveFavouriteStatus(id: String): LiveData<Boolean> {
        val result = MutableLiveData(false)
        cloud.collection(users).document(currentUser!!).collection(favourites)
             .addSnapshotListener { value, _ ->

                 result.postValue(value?.documents?.any {
                     it.id == id

                 })
             }


        return result
    }


    fun addItemToCart(item: Item) {
        val currentUserId = auth.currentUser?.uid
        cloud.collection(users).document(currentUserId!!).collection(cart)
            .add(item)
            .addOnSuccessListener {
                it.update("id", it.id)
            }
            .addOnFailureListener {
                Log.d("Repository", it.message.toString())
            }
    }

    fun addPizzaToFavourites(pizza: Pizza) {
        cloud.collection(users).document(currentUser!!).collection(favourites)
            .add(pizza)
            .addOnSuccessListener { originalDocument ->

                cloud.collection(users).document(currentUser).collection(favourites)
                    .document(originalDocument.id).get().addOnSuccessListener {
                        val data = it.data

                        cloud.collection(users).document(currentUser).collection(favourites)
                            .document(pizza.id!!).set(data!!).addOnSuccessListener {

                                cloud.collection(users).document(currentUser).collection(favourites)
                                    .document(originalDocument.id).delete()
                            }
                    }

            }
    }

    fun updateItemDetails(item: Item) {

        cloud.collection(users).document(currentUser!!).collection(cart)
            .document(item.id).update(
                "quantity", item.quantity,
                "totalPrice", item.totalPrice)
    }

    fun deleteItem(item: Item) {

        cloud.collection(users).document(currentUser!!).collection(cart)
            .document(item.id).delete()
    }

    fun deleteFavouritePizza(id: String) {
        cloud.collection(users).document(currentUser!!).collection(favourites)
            .document(id).delete()
    }

    fun logout() {
        auth.signOut()
    }
}