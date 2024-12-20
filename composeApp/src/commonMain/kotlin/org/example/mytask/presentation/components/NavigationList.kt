package org.example.mytask.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination

@Composable
fun NavigationList(
    currentRouteName: String?,
    onNavigationRouteNameChange: (String) -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        itemsIndexed(navigationItems) { _, pair ->
            val icon = pair.first.second
            val routeName = pair.second.second
            NavigationDrawerItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = null
                    )
                },
                label = { Text(routeName.title) },
                selected = routeName.route == currentRouteName,
                onClick = { onNavigationRouteNameChange(routeName.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

val navigationItems = listOf(
    Pair("icon" to Icons.Outlined.Home, "label" to RouteNames.Tasks),
    Pair("icon" to Icons.Outlined.CheckCircle, "label" to RouteNames.Checked),
    Pair("icon" to Icons.Outlined.FavoriteBorder, "label" to RouteNames.Favorites),
    Pair("icon" to Icons.Outlined.AccountCircle, "label" to RouteNames.Account),
)

sealed class RouteNames(
    val route: String,
    var title: String,
) {
    data object Splash : RouteNames(
        route = "SPLASH",
        title = "WELCOME !",
    )

    data object Tasks : RouteNames(
        route = "TASKS",
        title = "Tasks List",
    )

    data object TasksDetails : RouteNames(
        route = "TASK_DETAILS",
        title = "More infos",
    )

    data object Checked : RouteNames(
        route = "CHECKED",
        title = "Completed",
    )

    data object Favorites : RouteNames(
        route = "FAVORITES",
        title = "Favorites",
    )

    data object Account : RouteNames(
        route = "ACCOUNT",
        title = "Account",
    )
}

fun getTitle(currentDestination: NavDestination?): String {
    return when (currentDestination?.route) {
        RouteNames.Splash.route -> {
            RouteNames.Splash.title
        }

        RouteNames.Tasks.route -> {
            RouteNames.Tasks.title
        }

        RouteNames.TasksDetails.route -> {
            RouteNames.TasksDetails.title
        }

        RouteNames.Checked.route -> {
            RouteNames.Checked.title
        }

        RouteNames.Favorites.route -> {
            RouteNames.Favorites.title
        }

        RouteNames.Account.route -> {
            RouteNames.Account.title
        }

        else -> "More infos"
    }

}

