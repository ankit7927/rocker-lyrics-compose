package com.lmptech.rockers.data.remote.model

import com.lmptech.rockers.model.CollectionModel
import com.lmptech.rockers.model.SongModel

data class HomeFeed(
    val popular: List<SongModel>,
    val latest: List<SongModel>,
    val collections: List<CollectionModel>,
)