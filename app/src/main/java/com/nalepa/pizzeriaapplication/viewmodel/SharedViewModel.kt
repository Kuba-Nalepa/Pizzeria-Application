package com.nalepa.pizzeriaapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.data.order.OrderItem
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class SharedViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    fun createNewUser(user: User) {
        repository.createNewUser(user)
    }

    val user = repository.getCurrentUserData()
    val menu = repository.getMenuList()
    val userOrder = repository.getCurrentUserOrder()
    var pizza: LiveData<Pizza> = MutableLiveData()

    fun retrievePizza(id: String) {
        pizza = repository.getPizzaDetails(id)
    }

    private val _pizzaSize = MutableLiveData(PizzaSize())
    val pizzaSize: LiveData<PizzaSize> = _pizzaSize

    private val _quantity = MutableLiveData(1)
    val quantity: LiveData<Int> = _quantity

    private val _price = MutableLiveData(0.0)
    val price: LiveData<Double> = _price

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

    fun addItemToOrderItems(orderItem: OrderItem) {
        repository.addItemToUsersOrder(orderItem)
    }

    private fun setPrice() {
        _price.value = _pizzaSize.value?.price?.times(_quantity.value!!)
    }

    fun deleteOrder(orderItem: OrderItem) {
        repository.deleteOrderItem(orderItem)
    }
    fun updateOrderDetails(orderItem: OrderItem) {
        repository.updateOrderDetails(orderItem)
    }
}