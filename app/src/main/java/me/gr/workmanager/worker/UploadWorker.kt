package me.gr.workmanager.worker

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.api.uploadImage
import me.gr.workmanager.common.KEY_IMAGE_URI
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.toast

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params), AnkoLogger {

    override fun doWork(): Result {
        val imageUri = inputData.getString(KEY_IMAGE_URI)
        return try {
            val response = Uri.parse(imageUri).uploadImage().execute()
            if (response.isSuccessful) {
                outputData = Data.Builder().putString(KEY_IMAGE_URI, response.body()?.data?.link).build()
                Result.SUCCESS
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: "Request failed"
                applicationContext.toast(errorMessage)
                error(errorMessage)
                Result.FAILURE
            }
        } catch (e: Exception) {
            val errorMessage = "Failed to upload image with URI: $imageUri"
            applicationContext.toast(errorMessage)
            error(errorMessage)
            Result.FAILURE
        }
    }
}