package com.paradoxo.threadscompose.ui.navigation

import com.paradoxo.threadscompose.R


sealed class Destinations(val route: String, val resourceId: Pair<Int, Int>? = null) {
    object Login : Destinations("login")
    object Feed : Destinations("feed", Pair(R.drawable.ic_home, R.drawable.ic_home_outlined))
    object Search : Destinations("search", Pair(R.drawable.ic_search, R.drawable.ic_search))
    object Post : Destinations("post", Pair(R.drawable.ic_post, R.drawable.ic_post))
    object Notifications :
        Destinations("notifications", Pair(R.drawable.ic_heart, R.drawable.ic_heart_outlined))

    object Profile :
        Destinations("profile", Pair(R.drawable.ic_profile, R.drawable.ic_profile_outlined))

    object ProfileEdit : Destinations("profileEdit")

    object LoginNavigation : Destinations("loginNavigation")
    object HomeNavigation : Destinations("homeNavigation")
}

val screenItems = listOf(
    Destinations.Feed,
    Destinations.Search,
    Destinations.Post,
    Destinations.Notifications,
    Destinations.Profile
)
