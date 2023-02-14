package com.nalepa.pizzeriaapplication.data.order

import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize

data class OrderItem(
    var id: String = "",
    var name: String = "",
    var size: PizzaSize = PizzaSize(),
    var quantity: Int = 0,
    var price: Double = 0.0
)