package com.nalepa.pizzeriaapplication.data.order

import java.sql.Date

data class Order(
    var id: String = "",
    var date: Date = Date(java.util.Date().time),
    var address: String = "",
    var postalCode: String = "",
    var city: String = "",
    var optionalInputs: OptionalInputs = OptionalInputs(),
    var items: List<Item> = listOf()
)

data class OptionalInputs(
    var apartmentNumber: Int? = null,
    var floor: Int? = null,
    var msg: String? = null
)
