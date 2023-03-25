package com.nalepa.pizzeriaapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.order.Item
import com.nalepa.pizzeriaapplication.data.order.Order
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class SharedViewModel: ViewModel() {
    private val repository = FirebaseRepository()


    var user = repository.getCurrentUserData()
    val menu = repository.getMenuList()
    val favourites = repository.getFavouritePizzasList()
    val userItems = repository.getCurrentUserItems()
    var pizza: LiveData<Pizza> = MutableLiveData()
    var status: LiveData<Boolean> = MutableLiveData()



    private val _pizzaSize = MutableLiveData(PizzaSize())
    val pizzaSize: LiveData<PizzaSize> = _pizzaSize

    private val _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    private val _price = MutableLiveData(0.0)
    val price: LiveData<Double> = _price


    fun createNewUser(user: User) {
        repository.createNewUser(user)
    }

    fun retrievePizza(id: String) {
        pizza = repository.getPizzaDetails(id)
    }

    fun setPizzaSize(pizzaSize: PizzaSize) {
        _pizzaSize.value = pizzaSize
        setPrice()
    }

    fun increaseQuantity() {
        _quantity.value = _quantity.value?.plus(1)
        setPrice()
    }

    fun decreaseQuantity() {
        if (_quantity.value!! > 0 ) _quantity.value = _quantity.value?.minus(1)
        setPrice()
    }

    fun addItemToCart(item: Item) {
        repository.addItemToCart(item)
    }

    fun createOrder(order: Order) {
        repository.createOrder(order)
    }

    private fun setPrice() {
        _price.value = _pizzaSize.value?.price?.times(_quantity.value!!)
    }

    fun deleteOrder(orderItem: Item) {
        repository.deleteItem(orderItem)
    }

    fun updateOrderDetails(orderItem: Item) {
        repository.updateItemDetails(orderItem)
    }

    fun logout() {
        repository.logout()
    }

    fun uploadUserImage(uri: Uri) {
        repository.uploadUserImage(uri)
    }

    fun updateUserName(name: String, surname: String) {
        repository.updateUserName(name, surname)
    }

    fun addPizzaToFavourites(pizza: Pizza) {
        repository.addPizzaToFavourites(pizza)
    }

    fun retrieveFavouriteStatus(id: String){
        status = repository.retrieveFavouriteStatus(id)
    }

    fun deleteFavouritePizza(id: String) {
        repository.deleteFavouritePizza(id)
    }
}