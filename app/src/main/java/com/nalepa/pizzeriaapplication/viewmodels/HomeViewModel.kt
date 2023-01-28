package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class HomeViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val menu = repository.getMenuList()
}