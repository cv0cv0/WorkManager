package me.gr.workmanager.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.KEY_IMAGE_URI
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.text.DateFormat
import java.util.*

class SaveImageWorker(context: Context, params: WorkerParameters) : Worker(context, params), AnkoLogger {
    companion object {
        private const val TITLE = "Filtered Image"
        private val dateFormat = DateFormat.getDateTimeInstance()
    }

    override fun doWork(): Result {
        val resolver = applicationContext.contentResolver
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        try {
            val bitmap = resolver.openInputStream(Uri.parse(resourceUri)).use { BitmapFactory.decodeStream(it) }
            val imageUrl = MediaStore.Images.Media.insertImage(resolver, bitmap, TITLE, dateFormat.format(Date()))
            if (imageUrl.isNullOrEmpty()) {
                error("Writing to MediaStore failed")
                return Result.FAILURE
            }
            outputData = Data.Builder().putString(KEY_IMAGE_URI, imageUrl).build()
            return Result.SUCCESS
        } catch (e: Exception) {
            error("Unable to save image to Gallery", e)
            return Result.FAILURE
        }
    }
}