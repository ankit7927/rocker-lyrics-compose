package com.lmptech.rockers.model

import androidx.room.Entity

data class ArtistModel(
    val id:Int,
    val name:String,
    val description:String?,
    val image:String,
)
