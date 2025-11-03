package hu.formula.facts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.feature.constructor.ConstructorScreen
import hu.formula.facts.feature.driver.DriverScreen
import hu.formula.facts.feature.grand_prix.GrandPrixScreen
import hu.formula.facts.feature.home.HomeScreen
import hu.formula.facts.feature.season.SeasonScreen
import kotlin.reflect.typeOf

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(
                onGpClick = { gp ->
                    navController.navigate(Routes.GrandPrixRoute(gp))
                },
                onSeasonClick = { year, initialPage ->
                    navController.navigate(Routes.SeasonRoute(year, initialPage))
                },
                onDriverClick = { driver ->
                    navController.navigate(Routes.DriverRoute(driver))
                },
                onConstructorClick = { constructor ->
                    navController.navigate(Routes.ConstructorRoute(constructor))
                }
            )
        }

        composable<Routes.ConstructorRoute>(
            typeMap =  mapOf(
                typeOf<Constructor>() to CustomNavType.ConstructorType
            )
        ) {
            ConstructorScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onRaceClick = { gp ->
                    navController.navigate(Routes.GrandPrixRoute(gp))
                }
            )
        }

        composable<Routes.DriverRoute>(
            typeMap =  mapOf(
                typeOf<Driver>() to CustomNavType.DriverType
            )
        ) {
            DriverScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onRaceClick = { gp ->
                    navController.navigate(Routes.GrandPrixRoute(gp))
                }
            )
        }

        composable<Routes.GrandPrixRoute>(
            typeMap =  mapOf(
                typeOf<GrandPrix>() to CustomNavType.GrandPrixType,
            )
        ) {
            GrandPrixScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Routes.SeasonRoute> {
            val args = it.toRoute<Routes.SeasonRoute>()
            SeasonScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onRaceClick = { gp ->
                    navController.navigate(Routes.GrandPrixRoute(gp))
                },
                onConstructorClick = { constructor ->
                    navController.navigate(Routes.ConstructorRoute(constructor))
                },
                onDriverClick = { driver ->
                    navController.navigate(Routes.DriverRoute(driver))
                },
                initialPage = args.initialPage,
            )
        }
    }
}
