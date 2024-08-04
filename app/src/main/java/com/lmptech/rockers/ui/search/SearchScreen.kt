package com.lmptech.rockers.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lmptech.rockers.ui.home.HorizontalSongCard
import com.lmptech.rockers.ui.navigation.NavDestination

object SearchDestination : NavDestination {
    override val route: String
        get() = "search_screen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
    onBackTap: () -> Unit,
    onSongTap: (Int) -> Unit
) {

    val searchUiState by searchViewModel.searchUiState.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = searchUiState.query,
                onQueryChange = { searchViewModel.onQueryChange(it) },
                onSearch = { },
                active = false,
                onActiveChange = {},
                placeholder = { Text(text = "Search song")},
                leadingIcon = {
                    IconButton(onClick = onBackTap) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            ) {}
        }
    ) { paddingValues ->
        LazyColumn(modifier = modifier.padding(paddingValues)) {
            if (searchUiState.loading) {
                item {
                    Box (modifier=Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (searchUiState.songs.isEmpty() && searchUiState.query.isEmpty()) {
                item {
                    Text(
                        text = "Search for songs",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.LightGray
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            } else if (searchUiState.songs.isEmpty()) {
                item {
                    Text(
                        text = "No songs found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.LightGray
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(items = searchUiState.songs) {
                    HorizontalSongCard(songModel = it, onSongTap = onSongTap)
                }
            }
        }
    }
}