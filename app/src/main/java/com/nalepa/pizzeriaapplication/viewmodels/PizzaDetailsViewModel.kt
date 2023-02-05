package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class PizzaDetailsViewModel : ViewModel(){
    private val repository = FirebaseRepository()

    var pizza: LiveData<Pizza> = MutableLiveData()
    fun retrievePizza(id: String) {
        pizza = repository.getPizzaDetails(id)
    }

    private val _pizzaSize = MutableLiveData(PizzaSize())
    val pizzaSize: LiveData<PizzaSize> = _pizzaSize

    private val _quantity = MutableLiveData(0)
    val quantity: LiveData<Int> = _quantity
    fun setPizzaSize(pizzaSize: PizzaSize) {
        _pizzaSize.value = pizzaSize
    }
    fun increaseQuantity() {
        _quantity.value = _quantity.value?.plus(1)
    }

    fun decreaseQuantity() {
        if (_quantity.value!! > 0 ) _quantity.value = _quantity.value?.minus(1)
    }
}