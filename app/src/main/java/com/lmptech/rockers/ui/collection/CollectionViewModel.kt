package com.lmptech.rockers.ui.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmptech.rockers.data.remote.repository.SongRepository
import com.lmptech.rockers.model.CollectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SongListUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val collectionModel: CollectionModel? = null
)

@HiltViewModel
class CollectionViewModel @Inject constructor(songRepository: SongRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val _songListUiState = MutableStateFlow(SongListUiState())
    val songListUiState: StateFlow<SongListUiState> = _songListUiState

    private val id: Int? = savedStateHandle.get<Int>("collId")

    init {
        viewModelScope.launch(Dispatchers.IO) {

            if (id == null) {
                _songListUiState.emit(_songListUiState.value.copy(error = "id is null"))
                return@launch
            }

            _songListUiState.emit(_songListUiState.value.copy(loading = true))

            try {
                val response = songRepository.getCollectionById(id)
                if (response.isSuccessful && response.body() != null) {
                    _songListUiState.emit(
                        _songListUiState.value.copy(
                            loading = false,
                            collectionModel = response.body()
                        )
                    )
                } else {
                    _songListUiState.emit(
                        _songListUiState.value.copy(
                            loading = false,
                            error = "failed to get collection"
                        )
                    )
                }
            } catch (e: Exception) {
                _songListUiState.emit(
                    _songListUiState.value.copy(
                        loading = false,
                        error = "exception to get collection"
                    )
                )
            }

        }
    }
}