package com.nalepa.pizzeriaapplication.data.pizza

data class Pizza(
    var id: String? = "",
    var name: String = "",
    var ingredients: List<String> = listOf(),
    var sizes: Sizes = Sizes(),
    var image: String = ""
)
