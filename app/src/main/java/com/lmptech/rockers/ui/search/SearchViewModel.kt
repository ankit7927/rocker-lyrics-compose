package com.lmptech.rockers.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmptech.rockers.data.remote.repository.SongRepository
import com.lmptech.rockers.model.SongModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val loading: Boolean = false,
    val error: String = "",
    val query: String = "",
    val songs: List<SongModel> = emptyList(),
    val searchActive: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val songRepository: SongRepository) :
    ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            _searchUiState.emit(_searchUiState.value.copy(query = query, loading = true))

            if (_searchUiState.value.query.isEmpty()) {
                _searchUiState.emit(_searchUiState.value.copy(loading = false, songs = emptyList()))
                return@launch
            }

            val response = songRepository.searchSong(query)

            try {
                if (response.isSuccessful && response.body() != null) {
                    _searchUiState.emit(
                        _searchUiState.value.copy(
                            loading = false,
                            songs = response.body()!!
                        )
                    )
                } else {
                    _searchUiState.emit(
                        _searchUiState.value.copy(
                            loading = false,
                            error = "failed to search"
                        )
                    )
                }
            } catch (e: Exception) {
                _searchUiState.emit(
                    _searchUiState.value.copy(
                        loading = false,
                        error = "exception to search"
                    )
                )
            }
        }
    }
}