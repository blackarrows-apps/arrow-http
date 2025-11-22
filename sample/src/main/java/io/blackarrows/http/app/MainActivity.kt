package io.blackarrows.http.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.blackarrows.http.app.navigation.Screen
import io.blackarrows.http.app.ui.*
import io.blackarrows.http.app.ui.theme.ArrowhttpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArrowhttpTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Menu.route
                ) {
                    composable(Screen.Menu.route) {
                        MenuScreen(
                            onNavigate = { route -> navController.navigate(route) }
                        )
                    }

                    composable(Screen.GetJson.route) {
                        ImageGalleryScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.PostJson.route) {
                        PostJsonScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.GetRaw.route) {
                        GetRawScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.PutJson.route) {
                        PutJsonScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.DeleteJson.route) {
                        DeleteJsonScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.PostForm.route) {
                        PostFormScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}