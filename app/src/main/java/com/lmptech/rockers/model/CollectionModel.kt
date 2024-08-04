package com.lmptech.rockers.model

data class CollectionModel(
    val id: Int,
    val name: String,
    val description: String?,
    val thumbnail: String,
    val songs: List<SongModel>
)
