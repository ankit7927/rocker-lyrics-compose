package com.lmptech.rockers.ui.lyrics

import android.content.res.ColorStateList
import android.widget.TextView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lmptech.rockers.model.SongModel
import com.lmptech.rockers.ui.navigation.NavDestination

object LyricsDestination : NavDestination {
    const val SONG_ID = "id"
    override val route: String
        get() = "lyrics_screen"
    val routeWithArg: String = "$route/{$SONG_ID}"
}

@Composable
fun LyricsScreen(
    modifier: Modifier = Modifier,
    lyricsViewModel: LyricsViewModel = hiltViewModel<LyricsViewModel>(),
    onBackTap: () -> Unit
) {

    val lyricsState by lyricsViewModel.lyricsUiState.collectAsState()
    val toast = Toast.makeText(LocalContext.current, "feature coming soon", Toast.LENGTH_SHORT)

    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { toast.show() }) {
                        Icon(imageVector = Icons.Rounded.Share, contentDescription = "")
                    }
                    IconButton(onClick = { toast.show() }) {
                        Icon(imageVector = Icons.Rounded.Settings, contentDescription = "")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { toast.show() }) {
                        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "")
                    }
                })
        }
    ) {

        if (lyricsState.loading) {
            Box (contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                CircularProgressIndicator()
            }
        } else if (lyricsState.error!="") {
            Box (contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                Text(text = lyricsState.error)
            }
        } else if (lyricsState is LyricsUiState.WithLyrics) {
            val withLyrics = lyricsState as LyricsUiState.WithLyrics
            LyricsBody(
                modifier = Modifier.padding(it),
                songModel = withLyrics.songModel,
                onBackTap = onBackTap
            )
        }
    }
}


@Composable
fun LyricsBody(modifier: Modifier = Modifier, songModel: SongModel, onBackTap: () -> Unit) {
    Column (modifier= modifier.verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        Box (modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 4.dp)) {
            AsyncImage(
                model = songModel.thumbnail,
                contentDescription = "",
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
                    contentDescription = ""
                )
            }
        }


        Text(
            textAlign = TextAlign.Center,
            text = "${songModel.name} - ${songModel.album?.name}",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 6.dp),
        )

        AnimatedVisibility(
            visible = songModel.artist != null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        ) {
            LazyRow {
                items(items = songModel.artist!!) {
                    Text(text = it.name, modifier=Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        LyricsText(lyricsHtml = songModel.lyrics!!)
    }
}



@Composable
fun LyricsText(modifier: Modifier = Modifier, lyricsHtml: String = "") {

    val textView = TextView(LocalContext.current)
    textView.textSize = 18f
    textView.setTextColor(
        ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_enabled)),
            intArrayOf(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f).toArgb())
        )
    )
    textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
            .padding(16.dp),
        factory = {
            textView
        },
        update = {
            it.text =
                HtmlCompat.fromHtml(lyricsHtml, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV)
        })
}