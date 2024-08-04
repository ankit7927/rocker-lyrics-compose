package com.lmptech.rockers.data.remote

import com.lmptech.rockers.data.remote.service.SongApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfig {

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            //.baseUrl("https://rockerlyrics.pythonanywhere.com/api/")
            .baseUrl("http://192.168.247.24:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun getSongApiService(retrofit: Retrofit): SongApiService {
        return retrofit.create(SongApiService::class.java)
    }

}