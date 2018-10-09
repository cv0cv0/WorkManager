package me.gr.workmanager.data

import android.net.Uri
import java.util.*

private val random = Random()
private val assetUris = arrayOf(
        Uri.parse("file:///android_asset/lit_pier.jpg"),
        Uri.parse("file:///android_asset/parting_ways.jpg"),
        Uri.parse("file:///android_asset/wrong_way.jpg")
)

fun randomImage() = assetUris[random.nextInt(assetUris.size)]