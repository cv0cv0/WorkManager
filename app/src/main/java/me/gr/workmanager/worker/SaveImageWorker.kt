package me.gr.workmanager.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.util.inputStreamFor
import me.gr.workmanager.ext.throwExceptionIfNullOrEmpty
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
        val resourceUri = inputData.getString(KEY_IMAGE_URI).apply { throwExceptionIfNullOrEmpty() }
        try {
            val bitmap = inputStreamFor(applicationContext, resourceUri!!).use { BitmapFactory.decodeStream(it) }
            val imageUrl = MediaStore.Images.Media.insertImage(applicationContext.contentResolver, bitmap, TITLE, dateFormat.format(Date()))
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