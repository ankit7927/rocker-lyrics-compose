package com.lmptech.rockers.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lmptech.rockers.model.SongModel

interface FavoriteSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songModel: SongModel)

}