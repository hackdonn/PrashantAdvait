package com.app.prashantadvait.api

import com.app.prashantadvait.BuildConfig
import com.app.prashantadvait.model.Image
import com.app.prashantadvait.model.Thumbnail
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class APIService {

    class ApiException(message: String) : Exception(message)
    class NetworkException(message: String) : Exception(message)

    // Fetching image from API
    suspend fun fetchThumbnails(): List<Thumbnail> {
        return withContext(Dispatchers.IO) {

            // Create a request for the thumbnails from the API
            // Base URL is kept in build.property file for security purposes.
            // To access it please change the file structure to 'Project'
            val request = Request.Builder()
                .url(BuildConfig.BASE_URL)
                .build()

            try {
                val response: Response = OkHttpClient().newCall(request).execute()

                if (response.isSuccessful) {
                    val jsonResponse = response.body.string()

                    if (jsonResponse.isBlank()) {
                        throw ApiException("Empty response body")
                    }

                    val thumbnailList: Array<Image> =
                        Gson().fromJson(jsonResponse, Array<Image>::class.java)
                    thumbnailList.map { it.thumbnail }.toList()
                } else {
                    throw ApiException("API error: ${response.code} - ${response.message}")
                }
            } catch (e: IOException) {
                throw NetworkException("Network error: ${e.message}")
            }
        }
    }
}