package me.gr.workmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import androidx.work.WorkStatus
import me.gr.workmanager.common.IMAGE_MANIPULATION_WORK_NAME
import me.gr.workmanager.common.TAG_OUTPUT
import me.gr.workmanager.util.ImageOperation

class FilterViewModel : ViewModel() {
    private val workManager = WorkManager.getInstance()

    val outputStatus: LiveData<List<WorkStatus>>
        get() = workManager.getStatusesByTag(TAG_OUTPUT)

    fun apply(operation: ImageOperation) {
        operation.continuation.enqueue()
    }

    fun cancel() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }
}