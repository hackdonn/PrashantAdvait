package com.app.prashantadvait.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ImageLoader(private val imageCache: ImageCache) {

    // Load image using Coroutine
    suspend fun loadImage(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val cachedImage = imageCache.getFromMemoryCache(url)
            if (cachedImage != null) {
                return@withContext cachedImage
            }

            val diskImage = imageCache.getFromDiskCache(url)
            if (diskImage != null) {
                imageCache.putInMemoryCache(url, diskImage)
                return@withContext diskImage
            }

            val request = Request.Builder().url(url).build()
            val response = OkHttpClient().newCall(request).execute()

            if (response.isSuccessful) {
                val bitmap = BitmapFactory.decodeStream(response.body.byteStream())
                bitmap?.let {
                    imageCache.putInMemoryCache(url, it)
                    imageCache.putInDiskCache(url, it)
                }
                bitmap
            } else {
                null
            }
        }
    }
}