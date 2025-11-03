package hu.formula.facts.navigation

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Home : Routes()

    @Serializable
    data class SeasonRoute(
        val year: Int,
        val initialPage: Int,
    ) : Routes()

    @Serializable
    data class GrandPrixRoute(
        val gp: GrandPrix
    ) : Routes()

    @Serializable
    data class DriverRoute(
        val driver: Driver,
    ) : Routes()

    @Serializable
    data class ConstructorRoute(
        val constructor: Constructor,
    ) : Routes()
}
