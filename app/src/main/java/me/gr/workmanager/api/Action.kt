package me.gr.workmanager.api

import android.net.Uri
import me.gr.workmanager.data.Imgur
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

fun Uri.uploadImage(): Call<Imgur> {
    val imageFile = File(this.path)
    val requestBody = RequestBody.create(MediaType.parse("image/png"), imageFile)
    val body = MultipartBody.Part.createFormData("image", "image.png", requestBody)
    return api.uploadImage(body)
}