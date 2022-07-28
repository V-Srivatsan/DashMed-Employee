package com.dashmed.employee.networking

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class EmptyRes (
    @Json(name = "valid") var valid: Boolean,
    @Json(name = "message") var message: String?
)


@Keep
@JsonClass(generateAdapter = true)
data class LoginReq (
    @Json(name = "username") var username: String,
    @Json(name = "password") var password: String
)

data class LoginRes (
    @Json(name = "valid") var valid: Boolean,
    @Json(name = "message") var message: String?,
    @Json(name = "UID") var uid: String?
)


data class GetPendingRes (
    @Json(name = "valid") var valid: Boolean,
    @Json(name = "message") var message: String?,
    @Json(name = "orders") var orders: List<OrderItem>?
)

data class GetOrderRes (
    @Json(name = "valid") var valid: Boolean,
    @Json(name = "message") var message: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "contact") var contact: String?,
    @Json(name = "address") var address: String?,
    @Json(name = "lat") var lat: Double?,
    @Json(name = "long") var long: Double?,
    @Json(name = "items") var items: List<Order>?
)

@Keep
@JsonClass(generateAdapter = true)
data class DeliverOrderReq (
    @Json(name = "uid") var uid: String,
    @Json(name = "order_id") var order_id: String,
)

@Keep
@JsonClass(generateAdapter = true)
data class ChangePasswordReq (
    @Json(name = "uid") var uid: String,
    @Json(name = "password") var password: String
)