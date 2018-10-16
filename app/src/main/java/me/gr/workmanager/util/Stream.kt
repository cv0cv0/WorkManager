package me.gr.workmanager.util

import android.content.Context
import android.net.Uri
import me.gr.workmanager.common.ASSET_PREFIX
import java.io.InputStream

fun inputStreamFor(context: Context, resourceUri: String): InputStream? {
    return if (resourceUri.startsWith(ASSET_PREFIX)) {
        val assetManager = context.resources.assets
        assetManager.open(resourceUri.substring(ASSET_PREFIX.length))
    } else {
        val resolver = context.contentResolver
        resolver.openInputStream(Uri.parse(resourceUri))
    }
}