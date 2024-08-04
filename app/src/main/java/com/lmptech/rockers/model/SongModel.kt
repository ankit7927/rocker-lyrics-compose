package com.lmptech.rockers.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val music: String?,
    val thumbnail: String,
    val lyrics: String?,
    val ytvid: String?,
    val slug: String,
    val created: String?,
    val album: AlbumModel?,
    val artist: List<ArtistModel>?,
)
