package me.gr.workmanager.api

import me.gr.workmanager.data.Imgur
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("image")
    fun uploadImage(@Part image: MultipartBody.Part): Call<Imgur>
}