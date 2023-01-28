package com.nalepa.pizzeriaapplication.data.order

import com.nalepa.pizzeriaapplication.data.pizza.Pizza
import com.nalepa.pizzeriaapplication.data.pizza.PizzaSize

data class OrderItem(
    val pizza: Pizza,
    val size: PizzaSize,
    val quantity: Int
)