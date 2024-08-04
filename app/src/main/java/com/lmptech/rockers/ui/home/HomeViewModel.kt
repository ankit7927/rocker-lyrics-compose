package com.lmptech.rockers.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmptech.rockers.data.remote.model.HomeFeed
import com.lmptech.rockers.data.remote.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val loading: Boolean = false,
    val error: String? = null,
    val homeFeed: HomeFeed? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    private val mutableHomeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = mutableHomeState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mutableHomeState.emit(mutableHomeState.value.copy(loading = true))

            try {
                val feedSong = songRepository.getFeedSong()

                println(feedSong.body())

                if (feedSong.isSuccessful) {
                    mutableHomeState.emit(
                        mutableHomeState.value.copy(
                            loading = false,
                            homeFeed = feedSong.body()
                        )
                    )
                } else {
                    mutableHomeState.emit(
                        mutableHomeState.value.copy(
                            loading = false,
                            error = "failed to get home fed"
                        )
                    )
                }
            } catch (e: Exception) {
                println(e)
                mutableHomeState.emit(
                    mutableHomeState.value.copy(
                        loading = false,
                        error = "exception to get home fed"
                    )
                )
            }
        }
    }


}