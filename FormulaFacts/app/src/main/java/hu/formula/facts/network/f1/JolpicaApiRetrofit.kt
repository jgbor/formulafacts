package hu.formula.facts.network.f1

import hu.formula.facts.network.model.JolpicaAnswer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JolpicaApiRetrofit {
    @GET("seasons/")
    suspend fun getSeasons(
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("drivers/{driverId}/seasons/")
    suspend fun getDriverSeasons(
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("constructors/{constructorId}/seasons/")
    suspend fun getConstructorSeasons(
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/driverstandings/")
    suspend fun getDriverStandings(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/constructorstandings/")
    suspend fun getConstructorStandings(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/drivers/{driverId}/driverstandings/")
    suspend fun getDriverStandingsOfDriver(
        @Path("year") year: String = "current",
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/constructors/{constructorId}/constructorstandings/")
    suspend fun getConstructorStandingsOfConstructor(
        @Path("year") year: String = "current",
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/races/")
    suspend fun getRaces(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/drivers/")
    suspend fun getDrivers(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/constructors/")
    suspend fun getConstructors(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/results/")
    suspend fun getRaceResults(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/results/")
    suspend fun getRaceResultsOfSeason(
        @Path("year") year: String = "current",
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/qualifying/")
    suspend fun getQualifyingResults(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/sprint/")
    suspend fun getSprintResults(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/drivers/{driverId}/results/")
    suspend fun getDriverRaceResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/drivers/{driverId}/results/")
    suspend fun getDriverRaceResultOfSeason(
        @Path("year") year: String = "current",
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/constructors/{constructorId}/results/")
    suspend fun getConstructorRaceResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/constructors/{constructorId}/results/")
    suspend fun getConstructorRaceResultOfSeason(
        @Path("year") year: String = "current",
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/drivers/{driverId}/qualifying/")
    suspend fun getDriverQualifyingResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/constructors/{constructorId}/qualifying/")
    suspend fun getConstructorQualifyingResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/drivers/{driverId}/sprint/")
    suspend fun getDriverSprintResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("driverId") driverId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer

    @GET("{year}/{round}/constructors/{constructorId}/sprint/")
    suspend fun getConstructorSprintResult(
        @Path("year") year: String = "current",
        @Path("round") round: String,
        @Path("constructorId") constructorId: String,
        @Query("limit") limit: Int? = 100,
        @Query("offset") offset: Int? = null,
    ): JolpicaAnswer
}
