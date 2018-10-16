package me.gr.workmanager.ext

@Suppress("UNREACHABLE_CODE")
fun String?.throwExceptionIfNullOrEmpty() {
    if (isNullOrEmpty()) {
        error("Invalid input uri")
        throw IllegalArgumentException("Invalid input uri")
    }
}