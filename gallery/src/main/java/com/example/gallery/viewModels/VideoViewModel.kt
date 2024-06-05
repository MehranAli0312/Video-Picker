package com.example.gallery.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gallery.data.local.mediastore.VideoRepo
import com.example.gallery.data.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepo: VideoRepo
) : ViewModel() {

    fun getAllVideosFromStorageAndInsertDb() {
        viewModelScope.launch {
            try {
                videoRepo.getAllVideosFromStorageAndInsertDb()
            } catch (e: Exception) {
                Log.e("VideoViewModel", "Error fetching and inserting videos", e)
            }
        }
    }

    val getVideosFromDb: Flow<PagingData<Video>> =
        videoRepo.getVideosFromDb().cachedIn(viewModelScope)

}