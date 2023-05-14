package net.nikonorov.expandabletext

import androidx.navigation.*
import net.nikonorov.expandabletext.exposition.ExpositionType

sealed interface Route {
    val path: String

    object MainMenuRoute : Route {
        override val path: String = "main_menu"
    }

    object ExpositionRoute : Route {
        val arguments: List<NamedNavArgument> = listOf(
            navArgument("type") {
                type = NavType.StringType
                nullable = false
            }
        )
        override val path: String = "exposition?type={type}"

        fun getExpositionType(backStackEntry: NavBackStackEntry): ExpositionType {
            return when (val type = backStackEntry.arguments?.getString("type")) {
                "Collapse" -> ExpositionType.Collapse
                "CollapseExpand" -> ExpositionType.CollapseExpand
                else -> throw IllegalArgumentException("Unsupported type: $type")
            }
        }

        fun navigate(navController: NavController, type: ExpositionType) {
            navController.navigate("exposition?type=${type.name}")
        }
    }
}
