package com.lmptech.rockers.data.remote.service

import com.lmptech.rockers.data.remote.model.HomeFeed
import com.lmptech.rockers.model.CollectionModel
import com.lmptech.rockers.model.SongModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface SongApiService {
    @GET("home-feed")
    suspend fun getHomeFeed(): Response<HomeFeed>

    @GET("song/{id}/get")
    suspend fun getSongById(
        @Path("id") id: Int
    ): Response<SongModel>

    @GET("coll/{id}")
    suspend fun getCollectionById(
        @Path("id") id: Int
    ): Response<CollectionModel>

    @GET("search")
    suspend fun searchSong(
        @Query("query") query: String
    ): Response<List<SongModel>>

}