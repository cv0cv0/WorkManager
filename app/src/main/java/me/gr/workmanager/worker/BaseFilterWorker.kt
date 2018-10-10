package me.gr.workmanager.worker

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.common.OUTPUT_PATH
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

abstract class BaseFilterWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        private const val TAG = "BaseFilterWorker"
        private const val ASSET_PREFIX = "file:///android_asset/"
    }

    @WorkerThread
    abstract fun applyFilter(input: Bitmap): Bitmap

    override fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        if (resourceUri.isNullOrEmpty()) {
            Log.e(TAG, "Invalid input uri")
            throw IllegalArgumentException("Invalid input uri")
        }
        return try {
            Result.SUCCESS
        } catch (fileNotFoundException: FileNotFoundException) {
            Log.e(TAG, "Failed to decode input stream", fileNotFoundException)
            throw RuntimeException("Failed to decode input stream", fileNotFoundException)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying filter", throwable)
            Result.FAILURE
        }
    }

    private fun inputStreamFor(context: Context, resourceUri: String): InputStream? {
        return if (resourceUri.startsWith(ASSET_PREFIX)) {
            val assetManager = context.resources.assets
            assetManager.open(resourceUri.substring(ASSET_PREFIX.length))
        } else {
            val resolver = context.contentResolver
            resolver.openInputStream(Uri.parse(resourceUri))
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