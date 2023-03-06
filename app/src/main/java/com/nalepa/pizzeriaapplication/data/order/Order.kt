package com.nalepa.pizzeriaapplication.data.order

import java.sql.Date

data class Order(
    var id: String = "",
    var date: Date = Date(java.util.Date().time),
    var items: List<Item> = listOf()
)
