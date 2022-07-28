package com.dashmed.employee.networking

import com.squareup.moshi.Json

data class OrderItem (
    @Json(name = "uid") var uid: String,
    @Json(name = "name") var name: String,
    @Json(name = "quantity") var quantity: Int
)

data class Order (
    @Json(name = "name") var name: String,
    @Json(name = "description") var description: String,
    @Json(name = "composition") var composition: List<String>,
    @Json(name = "expiration") var expiration: Int,
    @Json(name = "cost") var cost: Float,
    @Json(name = "quantity") var quantity: Int
)