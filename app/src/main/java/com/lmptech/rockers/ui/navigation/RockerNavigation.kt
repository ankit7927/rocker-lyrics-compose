package com.lmptech.rockers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lmptech.rockers.ui.collection.CollectionDestination
import com.lmptech.rockers.ui.collection.SongCollection
import com.lmptech.rockers.ui.home.HomeDestination
import com.lmptech.rockers.ui.home.HomeScreen
import com.lmptech.rockers.ui.lyrics.LyricsDestination
import com.lmptech.rockers.ui.lyrics.LyricsScreen
import com.lmptech.rockers.ui.search.SearchDestination
import com.lmptech.rockers.ui.search.SearchScreen

@Composable
fun RockerNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = HomeDestination.route
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onSongTap = { navController.navigate("${LyricsDestination.route}/${it}") },
                onCollectionTap = { navController.navigate("${CollectionDestination.route}/${it}") },
                onSearchIconTap = { navController.navigate(SearchDestination.route) }
            )
        }

        composable(
            route = LyricsDestination.routeWithArg,
            arguments = listOf(
                navArgument(name = LyricsDestination.SONG_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            LyricsScreen(
                onBackTap = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = CollectionDestination.routeWithArg,
            arguments = listOf(
                navArgument(name = CollectionDestination.COLLECTION_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            SongCollection(
                onBackTap = { navController.popBackStack() },
                onSongTap = { navController.navigate("${LyricsDestination.route}/${it}") }
            )
        }

        composable(route = SearchDestination.route) {
            SearchScreen(onBackTap = { navController.popBackStack() },
                onSongTap = { navController.navigate("${LyricsDestination.route}/${it}") })
        }

    }
}