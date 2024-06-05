package com.example.gallery.data.local.mediastore

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.gallery.data.db.VideoDao
import com.example.gallery.data.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class VideoRepo @Inject constructor(private val context: Context, private val videoDao: VideoDao) {


    suspend fun getAllVideosFromStorageAndInsertDb() = withContext(Dispatchers.IO) {
        try {
            val allVideos = getAllVideosFromStorage()
            videoDao.insertAllVideosInTransaction(allVideos)
        } catch (e: Exception) {
            Log.e("VideoRepo", "Error inserting videos into DB", e)
        }
    }

    private suspend fun getAllVideosFromStorage(): List<Video> = withContext(Dispatchers.IO) {
        val allVideoList: MutableList<Video> = mutableListOf()
        val allVideosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.DURATION,
        )
        val sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " ASC"

        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(allVideosUri, projection, null, null, sortOrder)
            cursor?.use {
                while (it.moveToNext()) {
                    val video = Video(
                        mediaId = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)),
                        title = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                            ?: "No Title",
                        dateModified = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)),
                        folderName = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                            ?: "Unknown",
                        bucketId = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID))
                            ?: "0",
                        size = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)),
                        filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)),
                        resolution = it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
                            ?: "",
                        duration = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)),
                        fileUri = Uri.withAppendedPath(
                            allVideosUri,
                            it.getString(it.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                        ).toString()
                    )
                    allVideoList.add(video)
                }
            }
        } catch (e: Exception) {
            Log.e("VideoRepo", "Error fetching videos from storage", e)
        } finally {
            cursor?.close()
        }
        return@withContext allVideoList
    }


    fun getVideosFromDb(): Flow<PagingData<Video>> {
        return Pager(
            PagingConfig(
                pageSize = 10, maxSize = 100, enablePlaceholders = true
            )
        ) {
            videoDao.getAllVideos()
        }.flow
    }

}