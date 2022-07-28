package com.dashmed.employee.networking

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://dashmed-av.herokuapp.com/employee/api/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        MoshiConverterFactory.create(
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    ))
    .baseUrl(BASE_URL)
    .build()


interface APIService {

    @POST("login/")
    suspend fun login(@Body data: LoginReq): LoginRes

    @GET("get-pending/")
    suspend fun getPending(@Query("uid") uid: String): GetPendingRes

    @GET("get-order/")
    suspend fun getOrder(@Query("order_id") order_id: String): GetOrderRes

    @PUT("deliver-order/")
    suspend fun deliverOrder(@Body data: DeliverOrderReq): EmptyRes

    @GET("check-password/")
    suspend fun checkPassword(@Query("uid") uid: String, @Query("password") password: String): EmptyRes

    @PUT("change-password/")
    suspend fun changePassword(@Body data: ChangePasswordReq): EmptyRes

}


object API {
    val service: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}