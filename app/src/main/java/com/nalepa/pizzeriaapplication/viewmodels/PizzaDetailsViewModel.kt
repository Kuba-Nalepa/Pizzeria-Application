package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class PizzaDetailsViewModel : ViewModel(){
    private val repository = FirebaseRepository()

    var pizzaDetails: LiveData<Pizza> = MutableLiveData()
    fun retrievePizzaDetails(id: String) {
        pizzaDetails = repository.getPizzaDetails(id)
    }
}