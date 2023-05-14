package net.nikonorov.expandabletext

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.nikonorov.expandabletext.exposition.ExpositionScreen
import net.nikonorov.expandabletext.mainmenu.MainMenuScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Route.MainMenuRoute.path) {
        composable(Route.MainMenuRoute.path) {
            MainMenuScreen(
                navigateToExpositionScreen = { type -> Route.ExpositionRoute.navigate(navController, type) },
                modifier = modifier,
            )
        }
        composable(route = Route.ExpositionRoute.path,
            arguments = Route.ExpositionRoute.arguments) { navBackStackEntry ->
            ExpositionScreen(modifier = modifier,
                type = Route.ExpositionRoute.getExpositionType(navBackStackEntry))
        }
    }
}
