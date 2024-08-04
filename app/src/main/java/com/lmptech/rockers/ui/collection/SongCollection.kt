package com.lmptech.rockers.ui.collection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lmptech.rockers.R
import com.lmptech.rockers.model.CollectionModel
import com.lmptech.rockers.ui.home.HorizontalSongCard
import com.lmptech.rockers.ui.navigation.NavDestination

object CollectionDestination : NavDestination {
    const val COLLECTION_ID = "collId"
    override val route: String
        get() = "collection_screen"
    val routeWithArg: String = "$route/{$COLLECTION_ID}"
}

@Composable
fun SongCollection(
    modifier: Modifier = Modifier,
    collectionViewModel: CollectionViewModel = hiltViewModel<CollectionViewModel>(),
    onBackTap: () -> Unit,
    onSongTap: (Int) -> Unit
) {

    val songCollection by collectionViewModel.songListUiState.collectAsState()

    Scaffold {
        if (songCollection.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                CircularProgressIndicator()
            }
        } else if (songCollection.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Text(text = songCollection.error!!)
            }
        } else if (songCollection.collectionModel != null) {
            CollectionBody(
                modifier = Modifier.padding(it),
                collectionModel = songCollection.collectionModel!!,
                onBackTap = onBackTap, onSongTap = onSongTap
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Text(text = "collection is not found")
            }
        }
    }
}

private const val s = "Back Button"

@Composable
fun CollectionBody(
    modifier: Modifier = Modifier,
    collectionModel: CollectionModel,
    onBackTap: () -> Unit,
    onSongTap: (Int) -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 4.dp)
            ) {
                AsyncImage(
                    model = collectionModel.thumbnail,
                    contentDescription = "${collectionModel.name} thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                )

                FilledTonalIconButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = onBackTap
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }

            Text(
                text = collectionModel.name,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 6.dp),
            )
            Text(
                text = collectionModel.description ?: "",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 6.dp),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        items(items = collectionModel.songs) {
            HorizontalSongCard(
                songModel = it,
                onSongTap = onSongTap
            )
        }
    }
}