package com.nalepa.pizzeriaapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.nalepa.pizzeriaapplication.data.User
import com.nalepa.pizzeriaapplication.repository.FirebaseRepository

class RegistrationViewModel: ViewModel() {
    private val repository = FirebaseRepository()

    fun createNewUser(user: User) {
        repository.createNewUser(user)
    }
}