package com.nalepa.pizzeriaapplication.data

import com.nalepa.pizzeriaapplication.data.order.Cart

data class User(
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val orders: List<Cart> = listOf()
)
