package com.example.gallery.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Video(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var filePath: String = "",
    var mediaId: Long = 0,
    var bucketId: String = "",
    var folderName: String = "",
    var fileUri: String = "",
    var folderPath: String = "",
    var duration: Long = 0,
    var resolution: String = "",
    var dateModified: String = "",
    var videosCount: Int = 0,
    var title: String = "",
    var size: Long = 0
) : Parcelable
