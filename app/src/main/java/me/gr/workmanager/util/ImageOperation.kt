package me.gr.workmanager.util

import android.annotation.SuppressLint
import android.net.Uri
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import me.gr.workmanager.common.IMAGE_MANIPULATION_WORK_NAME
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.common.TAG_OUTPUT
import me.gr.workmanager.worker.*

@SuppressLint("EnqueueWork")
class ImageOperation(
        imageUri: Uri?,
        applyWaterColor: Boolean,
        applyGrayScale: Boolean,
        applyBlur: Boolean,
        applySave: Boolean,
        applyUpload: Boolean
) {
    var continuation = WorkManager.getInstance().beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java))
        private set

    init {
        var hasInputData = false

        if (applyWaterColor) {
            continuation = continuation.then(
                    OneTimeWorkRequest.Builder(WaterColorFilterWorker::class.java)
                            .setInputData(createInputData(imageUri))
                            .build())
            hasInputData = true
        }

        if (applyGrayScale) {
            val grayScaleBuilder = OneTimeWorkRequest.Builder(GrayScaleFilterWorker::class.java)
            if (!hasInputData) {
                grayScaleBuilder.setInputData(createInputData(imageUri))
                hasInputData = true
            }
            continuation = continuation.then(grayScaleBuilder.build())
        }

        if (applyBlur) {
            val blurBuilder = OneTimeWorkRequest.Builder(BlurEffectFilterWorker::class.java)
            if (!hasInputData) {
                blurBuilder.setInputData(createInputData(imageUri))
                hasInputData = true
            }
            continuation = continuation.then(blurBuilder.build())
        }

        if (applySave) {
            continuation = continuation.then(
                    OneTimeWorkRequest.Builder(SaveImageWorker::class.java)
                            .setInputData(createInputData(imageUri))
                            .addTag(TAG_OUTPUT)
                            .build())
        }

        if (applyUpload) {
            continuation = continuation.then(
                    OneTimeWorkRequest.Builder(UploadWorker::class.java)
                            .setInputData(createInputData(imageUri))
                            .addTag(TAG_OUTPUT)
                            .build())
        }
    }

    private fun createInputData(imageUri: Uri?) = Data.Builder().putString(KEY_IMAGE_URI, imageUri?.toString()).build()
}