package com.lmptech.rockers.data.remote.repository

import com.lmptech.rockers.data.remote.model.HomeFeed
import com.lmptech.rockers.data.remote.service.SongApiService
import com.lmptech.rockers.model.CollectionModel
import com.lmptech.rockers.model.SongModel
import retrofit2.Response
import javax.inject.Inject

class SongRepository @Inject constructor(private val songApiService: SongApiService) {
    suspend fun getFeedSong(): Response<HomeFeed> = songApiService.getHomeFeed()

    suspend fun getSongById(id: Int): Response<SongModel> = songApiService.getSongById(id)

    suspend fun getCollectionById(id: Int): Response<CollectionModel> =
        songApiService.getCollectionById(id)

    suspend fun searchSong(query: String): Response<List<SongModel>> =
        songApiService.searchSong(query)
}