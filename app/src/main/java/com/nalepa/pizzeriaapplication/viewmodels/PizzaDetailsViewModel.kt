package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class PizzaDetailsViewModel : ViewModel(){
    private val repository = FirebaseRepository()

}