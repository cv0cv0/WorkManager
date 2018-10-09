package me.gr.workmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.OUTPUT_PATH
import java.io.File

class CleanupWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        private const val TAG = "CleanupWorker"
    }

    override fun doWork(): Result = try {
        File(applicationContext.filesDir, OUTPUT_PATH)
                .takeIf { it.exists() }
                ?.listFiles { _, name -> name.endsWith(".png") }
                ?.forEach {
                    val deleted = it.delete()
                    Log.i(TAG, String.format("Deleted %s - %s", it.name, deleted))
                }
        Result.SUCCESS
    } catch (e: Exception) {
        Log.e(TAG, "Error cleaning up", e)
        Result.FAILURE
    }
}