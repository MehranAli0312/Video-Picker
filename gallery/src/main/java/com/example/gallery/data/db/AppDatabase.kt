package com.example.gallery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gallery.data.model.Video

@Database(
    entities = [Video::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // record dao
    abstract fun videoDao(): VideoDao
}