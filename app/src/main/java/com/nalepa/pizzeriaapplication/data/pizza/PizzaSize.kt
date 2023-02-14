package com.nalepa.pizzeriaapplication.data.pizza

data class PizzaSize(
    var name: String = "",
    var price: Double = 0.0,
    var diameter: Int = 0
)

data class Sizes(
    var small: PizzaSize = PizzaSize(),
    var medium: PizzaSize = PizzaSize(),
    var large: PizzaSize = PizzaSize()
)
