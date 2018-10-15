package me.gr.workmanager.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gr.workmanager.common.OUTPUT_PATH
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.io.File

class CleanupWorker(context: Context, params: WorkerParameters) : Worker(context, params), AnkoLogger {

    override fun doWork(): Result = try {
        File(applicationContext.filesDir, OUTPUT_PATH)
                .takeIf { it.exists() }
                ?.listFiles { _, name -> name.endsWith(".png") }
                ?.forEach { info("Deleted ${it.name} is ${it.delete()}") }
        Result.SUCCESS
    } catch (e: Exception) {
        error("Error cleaning up", e)
        Result.FAILURE
    }
}