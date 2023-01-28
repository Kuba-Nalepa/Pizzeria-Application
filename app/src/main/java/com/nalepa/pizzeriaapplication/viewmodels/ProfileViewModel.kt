package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class ProfileViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    val user = repository.getCurrentUserData()
}