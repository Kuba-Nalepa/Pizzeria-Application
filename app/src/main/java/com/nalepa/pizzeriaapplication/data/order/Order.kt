package com.nalepa.pizzeriaapplication.data.order

import java.text.SimpleDateFormat

data class Order(
    val id: String,
    val items: List<OrderItem>,
    val date: SimpleDateFormat
)
