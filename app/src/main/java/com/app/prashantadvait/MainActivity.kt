package com.app.prashantadvait

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.app.prashantadvait.adapter.ImageAdapter
import com.app.prashantadvait.api.APIService
import com.app.prashantadvait.databinding.ActivityMainBinding
import com.app.prashantadvait.image.ImageCache
import com.app.prashantadvait.image.ImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageLoader: ImageLoader
    private lateinit var imageCache: ImageCache
    private lateinit var imageAPI: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialiseLateInIt()
        apiCall()
        setUpView()
    }

    private fun initialiseLateInIt() {
        imageCache = ImageCache(cacheDir)
        imageLoader = ImageLoader(imageCache)
        imageAPI = APIService()
    }

    private fun apiCall() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val thumbnails = imageAPI.fetchThumbnails()
                imageAdapter = ImageAdapter(thumbnails, imageLoader)
                binding.recyclerView.adapter = imageAdapter
            } catch (e: APIService.ApiException) {

                Toast.makeText(this@MainActivity, "API error: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            } catch (e: APIService.NetworkException) {
                Toast.makeText(this@MainActivity, "Network error: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setUpView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
    }
}