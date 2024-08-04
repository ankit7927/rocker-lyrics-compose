package com.lmptech.rockers.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lmptech.rockers.R
import com.lmptech.rockers.model.ArtistModel
import com.lmptech.rockers.model.CollectionModel
import com.lmptech.rockers.model.SongModel

@Composable
fun VerticalSongCard(
    modifier: Modifier = Modifier,
    songModel: SongModel,
    onSongTap: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .width(320.dp)
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onSongTap.invoke(songModel.id) }
    ) {
        AsyncImage(
            model = songModel.thumbnail,
            contentDescription = "${songModel.name} thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(320.dp)
                .height(160.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Text(
            text = "${songModel.name} - ${songModel.album?.name}",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal),
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(visible = songModel.artist != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                songModel.artist!!.map {
                    Text(
                        text = it.name,
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }

        AnimatedVisibility(visible = songModel.created != null) {
            Text(
                text = songModel.created!!,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.ExtraLight),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }

    }
}


@Composable
fun HorizontalSongCard(
    modifier: Modifier = Modifier,
    songModel: SongModel,
    onSongTap: (Int) -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onSongTap.invoke(songModel.id) },
        leadingContent = {
            AsyncImage(
                model = songModel.thumbnail,
                contentDescription = "${songModel.name} thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        },
        headlineContent = { Text(text = songModel.name) },
        supportingContent = {
            songModel.album?.name?.let { Text(text = it) }
        })
}

@Composable
fun VerticalArtistCard(modifier: Modifier = Modifier, artistModel: ArtistModel) {
    Column(
        modifier = modifier.width(120.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.rounded_person_24),
            contentDescription = stringResource(R.string.artist_logo)
        )

        Text(
            text = artistModel.name,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun HorizontalArtistCard(modifier: Modifier = Modifier, artistModel: ArtistModel) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = artistModel.image,
                contentDescription = "${artistModel.name} image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
        },
        headlineContent = { Text(text = artistModel.name) },
        supportingContent = { Text(text = stringResource(R.string.get_more_songs)) })
}

@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    collectionModel: CollectionModel,
    onCollectionClick: (Int) -> Unit
) {
    Card(
        modifier = modifier.padding(4.dp),
        onClick = { onCollectionClick.invoke(collectionModel.id) }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = collectionModel.thumbnail,
                contentDescription = "${collectionModel.name} thumbnail",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = collectionModel.name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}