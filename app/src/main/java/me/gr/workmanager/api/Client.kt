package me.gr.workmanager.api

import me.gr.workmanager.common.BASE_URL
import me.gr.workmanager.common.IMGUR_CLIENT_ID
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()!!
val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()!!
val api = retrofit.create(Api::class.java)!!

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers().newBuilder()
                .add("Authorization", "Client-ID$IMGUR_CLIENT_ID")
                .build()
        val authenticatedRequest = request.newBuilder().headers(headers).build()
        return chain.proceed(authenticatedRequest)
    }
}