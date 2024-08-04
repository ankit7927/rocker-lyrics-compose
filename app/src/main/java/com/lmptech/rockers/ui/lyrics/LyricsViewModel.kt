package com.lmptech.rockers.ui.lyrics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmptech.rockers.data.remote.repository.SongRepository
import com.lmptech.rockers.model.SongModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LyricsUiState{
    val loading:Boolean
    val error:String

    data class WithoutLyrics (
        override val loading: Boolean,
        override val error: String
    ) : LyricsUiState

    data class WithLyrics (
        override val loading: Boolean,
        override val error: String,
        val songModel: SongModel
    ): LyricsUiState
}


private data class LyricsViewModelState (
    val loading: Boolean = false,
    val error: String? = null,
    val songModel: SongModel? = null
) {
    fun toLyricsUiState(): LyricsUiState = if (songModel == null) {
        LyricsUiState.WithoutLyrics(loading, error ?: "")
    } else {
        LyricsUiState.WithLyrics(loading, error ?: "", songModel)
    }
}

@HiltViewModel
class LyricsViewModel @Inject constructor(
    private val songRepository: SongRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mutableLyricsViewModelState: MutableStateFlow<LyricsViewModelState> =
        MutableStateFlow(LyricsViewModelState())

    val lyricsUiState: StateFlow<LyricsUiState> = mutableLyricsViewModelState.map {
        it.toLyricsUiState()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500),
        mutableLyricsViewModelState.value.toLyricsUiState()
    )

    private val songId: Int = checkNotNull(savedStateHandle.get<Int>(LyricsDestination.SONG_ID))

    init {
        viewModelScope.launch (Dispatchers.IO) {
            mutableLyricsViewModelState.emit(mutableLyricsViewModelState.value.copy(loading = true))
            val response = songRepository.getSongById(id = songId)

            try {
                if (response.isSuccessful && response.body()!= null) {
                    mutableLyricsViewModelState.emit(
                        mutableLyricsViewModelState.value.copy(
                            loading = false,
                            songModel = response.body()
                        )
                    )
                } else {
                    mutableLyricsViewModelState.emit(
                        mutableLyricsViewModelState.value.copy(
                            loading = false,
                            error = "failed to get lyrics"
                        )
                    )
                }
            } catch (e: Exception) {
                println(e)
                mutableLyricsViewModelState.emit(
                    mutableLyricsViewModelState.value.copy(
                        loading = false,
                        error = "exception to get lyrics"
                    )
                )
            }
        }
    }


}