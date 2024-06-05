package com.example.gallery.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.gallery.adapters.VideoPagingAdapter
import com.example.gallery.databinding.ActivityVideoBinding
import com.example.gallery.viewModels.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_CODE = 100

        // Define video permissions based on Android version
        val videoPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private val videoViewModel: VideoViewModel by viewModels()

    private val binding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }

    private var videoPagingAdapter: VideoPagingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.setUpRecyclerView()

        // Check and request permissions
        if (checkForPermission(videoPermission[0])) {
            handleVideoData()
        } else {
            askPermission(videoPermission)
        }

        // Observe data from ViewModel
        observeData()
    }

    // Set up RecyclerView and Adapter
    private fun ActivityVideoBinding.setUpRecyclerView() {
        videoPagingAdapter = VideoPagingAdapter()
        videoRecyclerView.apply {
            setHasFixedSize(true)
            adapter = videoPagingAdapter
        }
    }

    // Fetch and insert videos from storage
    private fun handleVideoData() {
        videoViewModel.getAllVideosFromStorageAndInsertDb()
    }

    // Observe data from ViewModel
    private fun observeData() {
        lifecycleScope.launch {
            videoViewModel.getVideosFromDb.collectLatest { pagingData ->
                videoPagingAdapter?.submitData(pagingData)
            }
        }
    }


    // Check for permission
    private fun checkForPermission(permission: String): Boolean {
        ActivityCompat.checkSelfPermission(this, permission)
        return ContextCompat.checkSelfPermission(
            applicationContext,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request permissions
    private fun askPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            val bool = grantResults[0] == PackageManager.PERMISSION_GRANTED
            onPermissionResult(bool)
        }
    }

    // Handle permission result
    private fun onPermissionResult(granted: Boolean) {
        if (granted) {
            handleVideoData()
        } else {
            askPermission(videoPermission)
        }
    }

}