package com.example.gallery.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.gallery.data.model.Video

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<Video>)

    @Transaction
    suspend fun insertAllVideosInTransaction(videos: List<Video>) {
        videos.chunked(2000).forEach { chunk ->
            insertVideos(chunk)
        }
    }

    @Query("SELECT * FROM Video ORDER BY id ASC")
    fun getAllVideos(): PagingSource<Int, Video>
}