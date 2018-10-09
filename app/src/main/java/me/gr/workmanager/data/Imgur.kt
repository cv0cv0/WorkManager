package me.gr.workmanager.data

data class Imgur(
        val success: Boolean,
        val status: Int,
        val data: Data
) {

    data class Data(
            val id: String,
            val link: String
    )
}