package me.gr.workmanager.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.common.OUTPUT_PATH
import me.gr.workmanager.util.inputStreamFor
import me.gr.workmanager.ext.throwExceptionIfNullOrEmpty
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

abstract class BaseFilterWorker(context: Context, params: WorkerParameters) : Worker(context, params), AnkoLogger {

    @WorkerThread
    abstract fun applyFilter(input: Bitmap): Bitmap

    override fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URI).apply { throwExceptionIfNullOrEmpty() }
        return try {
            val inputBitmap = inputStreamFor(applicationContext, resourceUri!!).use { BitmapFactory.decodeStream(it) }
            val outputBitmap = applyFilter(inputBitmap)
            val outputUri = writeBitmapToFile(applicationContext, outputBitmap)
            outputData = Data.Builder().putString(KEY_IMAGE_URI, outputUri.toString()).build()
            Result.SUCCESS
        } catch (fileNotFoundException: FileNotFoundException) {
            error("Failed to decode input stream", fileNotFoundException)
            throw RuntimeException("Failed to decode input stream", fileNotFoundException)
        } catch (throwable: Throwable) {
            error("Error applying filter", throwable)
            Result.FAILURE
        }
    }

    private fun writeBitmapToFile(context: Context, bitmap: Bitmap): Uri {
        val name = String.format("filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(context.filesDir, OUTPUT_PATH).apply { if (!exists()) mkdirs() }
        val outputFile = File(outputDir, name)
        FileOutputStream(outputFile).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
        }
        return Uri.fromFile(outputFile)
    }
}