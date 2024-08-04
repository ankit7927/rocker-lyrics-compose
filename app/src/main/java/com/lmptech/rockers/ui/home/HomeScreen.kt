package com.lmptech.rockers.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lmptech.rockers.data.remote.model.HomeFeed
import com.lmptech.rockers.ui.navigation.NavDestination

object HomeDestination : NavDestination {
    override val route: String
        get() = "home_screen"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    onSongTap: (Int) -> Unit,
    onCollectionTap: (Int) -> Unit,
    onSearchIconTap: () -> Unit
) {
    val homeState by homeViewModel.homeState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Rockers") }, navigationIcon = {
                    IconButton(onClick = onSearchIconTap) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "")
                    }
                }, actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                    }
                })
        }) {

        if (homeState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (homeState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                Text(text = homeState.error!!)
            }
        } else if (homeState.homeFeed == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it), contentAlignment = Alignment.Center
            ) {
                Text(text = "home feed is not found")
            }
        } else {
            HomeBody(
                modifier = Modifier.padding(it),
                homeFeed = homeState.homeFeed!!,
                onSongTap = onSongTap,
                onCollectionTap = onCollectionTap
            )
        }
    }
}

@Composable
fun HomeBody(modifier: Modifier = Modifier, homeFeed: HomeFeed, onSongTap: (Int) -> Unit, onCollectionTap: (Int) -> Unit) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(
                text = "Top lyrics for you",
                style = TextStyle(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
            LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(items = homeFeed.popular) {
                    VerticalSongCard(songModel = it, onSongTap = onSongTap)
                }
            }
        }

        if (homeFeed.collections.isNotEmpty()) {
            item {
                Text(
                    text = "Best Collections",
                    style = TextStyle(fontWeight = FontWeight.Medium),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .height(270.dp),
                    userScrollEnabled = false,
                    columns = GridCells.Fixed(2)
                ) {
                    items(items = homeFeed.collections) {
                        CollectionCard(collectionModel = it, onCollectionClick = onCollectionTap)
                    }
                }
            }
        }

        item {
            Text(
                text = "Trending lyrics",
                style = TextStyle(fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
            )
        }
        items(items = homeFeed.latest) {
            HorizontalSongCard(songModel = it, onSongTap = onSongTap)
        }
    }
}
