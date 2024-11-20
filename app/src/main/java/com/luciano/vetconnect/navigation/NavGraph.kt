package com.luciano.vetconnect.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.luciano.vetconnect.R
import com.luciano.vetconnect.features.auth.login.LoginScreen
import com.luciano.vetconnect.features.client.settings.ChangePasswordScreen
import com.luciano.vetconnect.features.auth.password.ForgotPasswordScreen
import com.luciano.vetconnect.features.auth.register.RegisterScreen
import com.luciano.vetconnect.features.auth.splash.SplashScreen
import com.luciano.vetconnect.features.client.home.HomeScreen
import com.luciano.vetconnect.features.veterinary.home.HomeVetScreen
import com.luciano.vetconnect.features.client.notifications.NotificationsScreen
import com.luciano.vetconnect.features.client.profile.EditProfileScreen
import com.luciano.vetconnect.features.client.profile.ProfileScreen
import com.luciano.vetconnect.features.client.search.SearchResults
import com.luciano.vetconnect.features.client.search.SearchScreen
import com.luciano.vetconnect.features.client.settings.SettingsScreen
import com.luciano.vetconnect.features.client.vet_information.VetDetailScreen
import com.luciano.vetconnect.features.veterinary.notifications.VetNotificationsScreen
import com.luciano.vetconnect.features.veterinary.profile.VetEditProfileScreen
import com.luciano.vetconnect.features.veterinary.profile.VetProfileScreen
import com.luciano.vetconnect.features.veterinary.reviews.VetReviewDetailScreen
import com.luciano.vetconnect.features.veterinary.reviews.VetReviewsScreen
import com.luciano.vetconnect.features.client.saved.SavedVetScreen
import com.luciano.vetconnect.features.veterinary.services.VetServicesScreen
import com.luciano.vetconnect.features.veterinary.settings.VetChangePasswordScreen
import com.luciano.vetconnect.features.veterinary.settings.VetNotificationSettingsScreen
import com.luciano.vetconnect.features.veterinary.settings.VetSettingsScreen
import com.luciano.vetconnect.features.veterinary.statistics.VetStatisticsScreen
import com.luciano.vetconnect.shared.ui.components.client.MenuOverlay
import com.luciano.vetconnect.shared.ui.components.veterinary.MenuOverlayVet
import com.luciano.vetconnect.features.client.settings.NotificationSettingsScreen
import com.luciano.vetconnect.shared.data.repository.VeterinaryRepository
import com.luciano.vetconnect.shared.ui.theme.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object ChangePassword : Screen("change_password")

    // Vista del Cliente
    object Home : Screen("home")
    object Search : Screen("search")
    object SearchResults : Screen("search_results?query={query}") {
        fun createRoute(query: String? = null) = when (query) {
            null -> "search_results"
            else -> "search_results?query=$query"
        }
    }
    object SavedVets : Screen("favorites")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object Notifications : Screen("notifications")
    object NotificationSettings : Screen("notifications_settings")
    object Settings : Screen("settings")
    object VetDetail : Screen("vet_detail/{vetId}") {
        fun createRoute(vetId: String) = "vet_detail/$vetId"
    }

    // Vista de la Veterinaria
    object HomeVet : Screen("home_vet")
    object VetProfile : Screen("vet_profile")
    object VetEditProfile : Screen("vet_edit_profile")
    object VetServices : Screen("vet_services")
    object VetReviews : Screen("vet_reviews")
    object VetReviewDetail : Screen("review_detail/{reviewId}") {
        fun createRoute(reviewId: String) = "review_detail/$reviewId"
    }
    object VetStatistics : Screen("vet_statistics")
    object VetSettings : Screen("vet_settings")
    object VetNotifications : Screen("vet_notifications")
    object VetNotificationsSettings : Screen("vet_notifications_settings")
    object VetChangePassword : Screen("vet_change_password")



}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VetConnectApp() {
    val navController = rememberNavController()
    var isMenuOpen by remember { mutableStateOf(false) }
    var isVetUser by remember { mutableStateOf(false) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        isVetUser = when (currentRoute) {
            Screen.HomeVet.route,
            Screen.VetReviews.route,
            Screen.VetServices.route,
            Screen.VetStatistics.route,
            Screen.VetSettings.route -> true
            else -> false
        }
    }

    Box {
        NavGraph(
            navController = navController,
            onMenuClick = { isMenuOpen = true }
        )

        if (isVetUser) {
            MenuOverlayVet(
                isOpen = isMenuOpen,
                onClose = { isMenuOpen = false },
                onNavigate = { route ->
                    isMenuOpen = false
                    navController.navigate(route)
                }
            )
        } else {
            MenuOverlay(
                isOpen = isMenuOpen,
                onClose = { isMenuOpen = false },
                onNavigate = { route ->
                    isMenuOpen = false
                    navController.navigate(route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Icono de la app",
                modifier = Modifier.size(180.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Abrir menú",
                    tint = BrandColors.Secondary,
                    modifier = Modifier.size(50.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = BackgroundColors.Secondary
        )
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavController,
    startDestination: String = Screen.Splash.route,
    onMenuClick: () -> Unit
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination
    ) {
        // Auth
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }

        // Cliente
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(Screen.Search.route) {
            SearchScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }

        composable(
            route = Screen.SearchResults.route,
            arguments = listOf(
                // Opcional: Si necesitas pasar argumentos específicos
                navArgument("query") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            SearchResults(
                navController = navController,
                viewModel = viewModel()
            )
        }
        composable(Screen.SavedVets.route) {
            SavedVetScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(
            route = Screen.VetDetail.route,
            arguments = listOf(navArgument("vetId") { type = NavType.StringType })
        ) { backStackEntry ->
            VetDetailScreen(
                navController = navController,
                veterinaryId = backStackEntry.arguments?.getString("vetId") ?: "",
                onMenuClick = onMenuClick
            )
        }


        // Veterinaria
        composable(Screen.HomeVet.route) {
            val repository = VeterinaryRepository.getInstanceReal()
            HomeVetScreen(navController = navController, onMenuClick = onMenuClick, repository = repository)
        }
        composable(Screen.VetProfile.route) {
            VetProfileScreen(navController = navController)
        }
        composable(Screen.VetEditProfile.route) {
            VetEditProfileScreen(navController = navController)
        }
        composable(Screen.VetServices.route) {
            VetServicesScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(Screen.VetReviews.route) {
            VetReviewsScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(
            route = Screen.VetReviewDetail.route,
            arguments = listOf(navArgument("reviewId") { type = NavType.StringType })
        ) { backStackEntry ->
            VetReviewDetailScreen(
                navController = navController,
                reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
            )
        }
        composable(Screen.VetStatistics.route) {
            VetStatisticsScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(Screen.VetSettings.route) {
            VetSettingsScreen(navController = navController, onMenuClick = onMenuClick)
        }
        composable(Screen.VetNotifications.route) {
            VetNotificationsScreen(navController = navController)
        }
        composable(Screen.VetNotificationsSettings.route) {
            VetNotificationSettingsScreen(navController = navController)
        }
        composable(Screen.VetChangePassword.route) {
            VetChangePasswordScreen(navController = navController)
        }


    }
}


