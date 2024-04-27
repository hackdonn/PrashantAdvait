package com.app.prashantadvait.image

import android.graphics.Bitmap
import android.util.LruCache
import java.io.File
import java.io.FileOutputStream

class ImageCache(private val cacheDir: File) {

    // Memory cache using LRU algorithm
    private val memoryCache: LruCache<String, Bitmap> = LruCache(50)

    fun getFromMemoryCache(key: String): Bitmap? {
        return memoryCache[key]
    }

    fun putInMemoryCache(key: String, bitmap: Bitmap) {
        memoryCache.put(key, bitmap)
    }

    fun getFromDiskCache(key: String): Bitmap? {
        val file = File(cacheDir, key)
        if (file.exists()) {
            return android.graphics.BitmapFactory.decodeFile(file.path)
        }
        return null
    }

    fun putInDiskCache(key: String, bitmap: Bitmap) {
        val file = File(cacheDir, key)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}