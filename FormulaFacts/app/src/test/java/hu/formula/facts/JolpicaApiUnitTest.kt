package hu.formula.facts

import hu.formula.facts.domain.model.Circuit
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.QualifyingResult
import hu.formula.facts.domain.model.RaceResult
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.network.f1.JolpicaApi
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class JolpicaApiUnitTest {
    private lateinit var mockWebserver: MockWebServer
    private lateinit var jolpicaApi: JolpicaApi
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockWebserver = MockWebServer()
        mockWebserver.start()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(mockWebserver.url("/"))
            .build()

        jolpicaApi = JolpicaApi(retrofit.create())
    }

    @After
    fun tearDown() {
        mockWebserver.shutdown()
    }

    @Test
    fun testDriverStanding() {
        val mockResponseBody = driverChampionshipResponse
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(mockResponseBody)
        )

        val standingOne = Standing(
            position = 1,
            positionText = "1",
            points = 131f,
            wins = 4,
            driver = piastri,
            constructors = listOf(mclaren)
        )
        val standingTwo = Standing(
            position = 2,
            positionText = "2",
            points = 115f,
            wins = 1,
            driver = norris,
            constructors = listOf(mclaren)
        )
        val standingThree = Standing(
            position = 3,
            positionText = "3",
            points = 99f,
            wins = 1,
            driver = verstappen,
            constructors = listOf(redBull)
        )

        val standings = runBlocking { jolpicaApi.getDriverStandings() }

        assertNotNull(standings)
        assertTrue(standings.isNotEmpty())
        assertEquals(3, standings.size)
        assertEquals(standingOne, standings[0])
        assertEquals(standingTwo, standings[1])
        assertEquals(standingThree, standings[2])
    }

    @Test
    fun testConstructorStandings() {
        val mockResponseBody = constructorChampionshipResponse
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(mockResponseBody)
        )

        val standingOne = Standing(
            position = 1,
            positionText = "1",
            points = 246f,
            wins = 5,
            driver = null,
            constructor = mclaren
        )
        val standingTwo = Standing(
            position = 2,
            positionText = "2",
            points = 141f,
            wins = 0,
            driver = null,
            constructor = mercedes
        )
        val standingThree = Standing(
            position = 3,
            positionText = "3",
            points = 105f,
            wins = 1,
            driver = null,
            constructor = redBull
        )
        val standings = runBlocking { jolpicaApi.getConstructorStandings() }
        assertNotNull(standings)
        assertTrue(standings.isNotEmpty())
        assertEquals(3, standings.size)
        assertEquals(standingOne, standings[0])
        assertEquals(standingTwo, standings[1])
        assertEquals(standingThree, standings[2])
    }

    @Test
    fun testRacesOfSeason() {
        val mockResponseBody = racesOfSeasonResponse
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(mockResponseBody)
        )
        val gp1 = GrandPrix(
            season = 2025,
            round = 1,
            url = "https://en.wikipedia.org/wiki/2025_Australian_Grand_Prix",
            name = "Australian Grand Prix",
            circuit = alberPark,
            startTime = Instant.parse("2025-03-16T04:00:00Z"),
            firstPracticeTime = Instant.parse("2025-03-14T01:30:00Z"),
            secondPracticeTime = Instant.parse("2025-03-14T05:00:00Z"),
            thirdPracticeTime = Instant.parse("2025-03-15T01:30:00Z"),
            qualifyingTime = Instant.parse("2025-03-15T05:00:00Z"),
        )
        val gp2 = GrandPrix(
            season = 2025,
            round = 2,
            url = "https://en.wikipedia.org/wiki/2025_Chinese_Grand_Prix",
            name = "Chinese Grand Prix",
            circuit = shanghai,
            startTime = Instant.parse("2025-03-23T07:00:00Z"),
            firstPracticeTime = Instant.parse("2025-03-21T03:30:00Z"),
            qualifyingTime = Instant.parse("2025-03-22T07:00:00Z"),
            sprintQualifyingTime = Instant.parse("2025-03-21T07:30:00Z"),
            sprintTime = Instant.parse("2025-03-22T03:00:00Z"),
        )

        val races = runBlocking { jolpicaApi.getRacesOfSeason() }

        assertNotNull(races)
        assertTrue(races.isNotEmpty())
        assertEquals(2, races.size)
        assertEquals(gp1, races[0])
        assertEquals(gp2, races[1])
    }

    @Test
    fun testSeasons() {
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(seasonsResponse1)
        )
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(seasonsResponse2)
        )

        val seasons = runBlocking { jolpicaApi.getSeasons() }

        assertNotNull(seasons)
        assertTrue(seasons.isNotEmpty())
        assertEquals(2, seasons.size)
        assertEquals(1950, seasons[0].year)
        assertEquals("https://en.wikipedia.org/wiki/1950_Formula_One_season", seasons[0].url)
        assertEquals(1951, seasons[1].year)
        assertEquals("https://en.wikipedia.org/wiki/1951_Formula_One_season", seasons[1].url)
    }

    @Test
    fun testDriverList(){
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(driverListResponse)
        )

        val driverList = listOf(norris, piastri, verstappen)

        val drivers = runBlocking { jolpicaApi.getDriversInSeason() }

        assertNotNull(drivers)
        assertTrue(drivers.isNotEmpty())
        assertEquals(3, drivers.size)
        assertEquals(driverList[0], drivers[0])
        assertEquals(driverList[1], drivers[1])
        assertEquals(driverList[2], drivers[2])
    }

    @Test
    fun constructorList(){
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(constructorListResponse)
        )

        val constructorList = listOf(mclaren, mercedes, redBull)

        val constructors = runBlocking { jolpicaApi.getConstructorsInSeason() }

        assertNotNull(constructors)
        assertTrue(constructors.isNotEmpty())
        assertEquals(3, constructors.size)
        assertEquals(constructorList[0], constructors[0])
        assertEquals(constructorList[1], constructors[1])
        assertEquals(constructorList[2], constructors[2])
    }

    @Test
    fun testRaceResult() {
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(raceResultResponse)
        )
        val gp = GrandPrix(
            season = 2025,
            round = 3,
            url = "https://en.wikipedia.org/wiki/2025_Japanese_Grand_Prix",
            name = "Japanese Grand Prix",
            circuit = suzuka,
            startTime = Instant.parse("2025-04-06T05:00:00Z"),
            raceResults = listOf(
                RaceResult(
                    position = 1,
                    positionText = "1",
                    points = 25f,
                    driver = verstappen,
                    constructor = redBull,
                    startPosition = 1,
                    raceTime = "1:22:06.983",
                    status = "Finished",
                ),
                RaceResult(
                    position = 2,
                    positionText = "2",
                    points = 18f,
                    driver = norris,
                    constructor = mclaren,
                    startPosition = 2,
                    raceTime = "+1.423",
                    status = "Finished",
                ),
                RaceResult(
                    position = 3,
                    positionText = "3",
                    points = 15f,
                    driver = piastri,
                    constructor = mclaren,
                    startPosition = 3,
                    raceTime = "+2.129",
                    status = "Finished",
                )
            )
        )

        val response = runBlocking { jolpicaApi.getRaceResults(2025, 3) }
        assertNotNull(response?.raceResults)
        assertTrue(response!!.raceResults!!.isNotEmpty())
        assertEquals(gp.raceResults!![0], response.raceResults[0])
        assertEquals(gp.raceResults[1], response.raceResults[1])
        assertEquals(gp.raceResults[2], response.raceResults[2])
    }

    @Test
    fun testQualifyingResult() {
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(qualiResponse)
        )
        val gp = GrandPrix(
            season = 2025,
            round = 3,
            url = "https://en.wikipedia.org/wiki/2025_Japanese_Grand_Prix",
            name = "Japanese Grand Prix",
            circuit = suzuka,
            startTime = Instant.parse("2025-04-06T05:00:00Z"),
            qualifyingResults = listOf(
                QualifyingResult(
                    position = 1,
                    driver = verstappen,
                    number = 1,
                    constructor = redBull,
                    q1Time = "1:27.943",
                    q2Time = "1:27.502",
                    q3Time = "1:26.983",
                ),
                QualifyingResult(
                    position = 2,
                    driver = norris,
                    number = 4,
                    constructor = mclaren,
                    q1Time = "1:27.845",
                    q2Time = "1:27.146",
                    q3Time = "1:26.995",
                ),
                QualifyingResult(
                    position = 3,
                    driver = piastri,
                    number = 81,
                    constructor = mclaren,
                    q1Time = "1:27.687",
                    q2Time = "1:27.507",
                    q3Time = "1:27.027",
                )
            )
        )

        val response = runBlocking { jolpicaApi.getQualifyingResults(2025, 3) }
        assertNotNull(response?.qualifyingResults)
        assertTrue(response!!.qualifyingResults!!.isNotEmpty())
        assertEquals(gp.qualifyingResults!![0], response.qualifyingResults[0])
        assertEquals(gp.qualifyingResults[1], response.qualifyingResults[1])
        assertEquals(gp.qualifyingResults[2], response.qualifyingResults[2])
    }

    @Test
    fun testSprintResult() {
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(sprintResponse)
        )
        val gp = GrandPrix(
            season = 2025,
            round = 2,
            url = "https://en.wikipedia.org/wiki/2025_Chinese_Grand_Prix",
            name = "Chinese Grand Prix",
            circuit = shanghai,
            startTime = Instant.parse("2025-03-23T07:00:00Z"),
            sprintResults = listOf(
                RaceResult(
                    position = 2,
                    positionText = "2",
                    points = 7f,
                    driver = piastri,
                    constructor = mclaren,
                    startPosition = 3,
                    raceTime = "+6.889",
                    status = "Finished",
                ),
                RaceResult(
                    position = 8,
                    positionText = "8",
                    points = 1f,
                    driver = norris,
                    constructor = mclaren,
                    startPosition = 6,
                    raceTime = "+23.471",
                    status = "Finished",
                ),
            )
        )

        val response = runBlocking { jolpicaApi.getSprintResults(2025, 2) }
        assertNotNull(response?.sprintResults)
        assertTrue(response!!.sprintResults!!.isNotEmpty())
        assertEquals(gp.sprintResults!![0], response.sprintResults[0])
        assertEquals(gp.sprintResults[1], response.sprintResults[1])
    }

    @Test
    fun testDriverSeasons() {
        mockWebserver.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (request.path == "/drivers/piastri/seasons/?limit=100&offset=0") {
                    MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                        .setBody(piastriSeasonsResponse)
                } else {
                    MockResponse()
                        .setResponseCode(200)
                        .setHeader("Content-Type", "application/json")
                }
            }
        }
        val seasons = runBlocking { jolpicaApi.getDriverSeasons("piastri") }
        assertNotNull(seasons)
        assertTrue(seasons.isNotEmpty())
        assertEquals(2025, seasons[2].year)
        assertEquals("https://en.wikipedia.org/wiki/2025_Formula_One_World_Championship", seasons[2].url)
        assertEquals(2024, seasons[1].year)
        assertEquals("https://en.wikipedia.org/wiki/2024_Formula_One_World_Championship", seasons[1].url)
        assertEquals(2023, seasons[0].year)
        assertEquals("https://en.wikipedia.org/wiki/2023_Formula_One_World_Championship", seasons[0].url)
    }

    companion object {
        private val redBull = Constructor(
            id = "red_bull",
            name = "Red Bull",
            nationality = "Austrian",
            url = "http://en.wikipedia.org/wiki/Red_Bull_Racing"
        )
        private val mclaren = Constructor(
            id = "mclaren",
            name = "McLaren",
            nationality = "British",
            url = "http://en.wikipedia.org/wiki/McLaren"
        )
        private val mercedes = Constructor(
            id = "mercedes",
            name = "Mercedes",
            nationality = "German",
            url = "http://en.wikipedia.org/wiki/Mercedes-Benz_in_Formula_One"
        )
        private val piastri = Driver(
            id = "piastri",
            givenName = "Oscar",
            familyName = "Piastri",
            dateOfBirth = LocalDate.parse("2001-04-06"),
            permanentNumber = 81,
            code = "PIA",
            url = "http://en.wikipedia.org/wiki/Oscar_Piastri",
            nationality = "Australian"
        )
        private val norris = Driver(
            id = "norris",
            givenName = "Lando",
            familyName = "Norris",
            dateOfBirth = LocalDate.parse("1999-11-13"),
            permanentNumber = 4,
            code = "NOR",
            url = "http://en.wikipedia.org/wiki/Lando_Norris",
            nationality = "British"
        )
        private val verstappen = Driver(
            id = "max_verstappen",
            givenName = "Max",
            familyName = "Verstappen",
            dateOfBirth = LocalDate.parse("1997-09-30"),
            permanentNumber = 33,
            code = "VER",
            url = "http://en.wikipedia.org/wiki/Max_Verstappen",
            nationality = "Dutch"
        )
        private val alberPark = Circuit(
            id = "albert_park",
            name = "Albert Park Grand Prix Circuit",
            url = "https://en.wikipedia.org/wiki/Albert_Park_Circuit",
            country = "Australia",
            city = "Melbourne"
        )
        private val shanghai = Circuit(
            id = "shanghai",
            name = "Shanghai International Circuit",
            url = "https://en.wikipedia.org/wiki/Shanghai_International_Circuit",
            country = "China",
            city = "Shanghai"
        )
        private val suzuka = Circuit(
            id = "suzuka",
            name = "Suzuka Circuit",
            url = "https://en.wikipedia.org/wiki/Suzuka_International_Racing_Course",
            country = "Japan",
            city = "Suzuka"
        )

        private val driverChampionshipResponse = """
            {
                "MRData": {
                    "xmlns": "",
                    "series": "f1",
                    "url": "https://api.jolpi.ca/ergast/f1/2025/driverstandings/",
                    "limit": "30",
                    "offset": "0",
                    "total": "3",
                    "StandingsTable": {
                        "season": "2025",
                        "round": "7",
                        "StandingsLists": [
                            {
                                "season": "2025",
                                "round": "7",
                                "DriverStandings": [
                                    {
                                        "position": "1",
                                        "positionText": "1",
                                        "points": "131",
                                        "wins": "4",
                                        "Driver": {
                                            "driverId": "piastri",
                                            "permanentNumber": "81",
                                            "code": "PIA",
                                            "url": "http://en.wikipedia.org/wiki/Oscar_Piastri",
                                            "givenName": "Oscar",
                                            "familyName": "Piastri",
                                            "dateOfBirth": "2001-04-06",
                                            "nationality": "Australian"
                                        },
                                        "Constructors": [
                                            {
                                                "constructorId": "mclaren",
                                                "url": "http://en.wikipedia.org/wiki/McLaren",
                                                "name": "McLaren",
                                                "nationality": "British"
                                            }
                                        ]
                                    },
                                    {
                                        "position": "2",
                                        "positionText": "2",
                                        "points": "115",
                                        "wins": "1",
                                        "Driver": {
                                            "driverId": "norris",
                                            "permanentNumber": "4",
                                            "code": "NOR",
                                            "url": "http://en.wikipedia.org/wiki/Lando_Norris",
                                            "givenName": "Lando",
                                            "familyName": "Norris",
                                            "dateOfBirth": "1999-11-13",
                                            "nationality": "British"
                                        },
                                        "Constructors": [
                                            {
                                                "constructorId": "mclaren",
                                                "url": "http://en.wikipedia.org/wiki/McLaren",
                                                "name": "McLaren",
                                                "nationality": "British"
                                            }
                                        ]
                                    },
                                    {
                                        "position": "3",
                                        "positionText": "3",
                                        "points": "99",
                                        "wins": "1",
                                        "Driver": {
                                            "driverId": "max_verstappen",
                                            "permanentNumber": "33",
                                            "code": "VER",
                                            "url": "http://en.wikipedia.org/wiki/Max_Verstappen",
                                            "givenName": "Max",
                                            "familyName": "Verstappen",
                                            "dateOfBirth": "1997-09-30",
                                            "nationality": "Dutch"
                                        },
                                        "Constructors": [
                                            {
                                                "constructorId": "red_bull",
                                                "url": "http://en.wikipedia.org/wiki/Red_Bull_Racing",
                                                "name": "Red Bull",
                                                "nationality": "Austrian"
                                            }
                                        ]
                                    }
                                ]
                            }
                        ]
                    }
                }
            }
        """.trimIndent()
    }

    private val constructorChampionshipResponse = """
        {
        "MRData": {
            "xmlns": "",
            "series": "f1",
            "url": "https://api.jolpi.ca/ergast/f1/2025/constructorstandings/",
            "limit": "30",
            "offset": "0",
            "total": "3",
            "StandingsTable": {
                "season": "2025",
                "round": "7",
                "StandingsLists": [
                    {
                        "season": "2025",
                        "round": "7",
                        "ConstructorStandings": [
                            {
                            "position": "1",
                            "positionText": "1",
                            "points": "246",
                            "wins": "5",
                            "Constructor": {
                                "constructorId": "mclaren",
                                "url": "http://en.wikipedia.org/wiki/McLaren",
                                "name": "McLaren",
                                "nationality": "British"
                            }
                            },
                            {
                            "position": "2",
                            "positionText": "2",
                            "points": "141",
                            "wins": "0",
                            "Constructor": {
                                "constructorId": "mercedes",
                                "url": "http://en.wikipedia.org/wiki/Mercedes-Benz_in_Formula_One",
                                "name": "Mercedes",
                                "nationality": "German"
                            }
                            },
                            {
                            "position": "3",
                            "positionText": "3",
                            "points": "105",
                            "wins": "1",
                            "Constructor": {
                                "constructorId": "red_bull",
                                "url": "http://en.wikipedia.org/wiki/Red_Bull_Racing",
                                "name": "Red Bull",
                                "nationality": "Austrian"
                            }
                            }
                        ]
                    }
                ]
            }
        }
    }
    """.trimIndent()

    private val racesOfSeasonResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/races/",
                "limit": "30",
                "offset": "0",
                "total": "2",
                "RaceTable": {
                    "season": "2025",
                    "Races": [
                        {
                            "season": "2025",
                            "round": "1",
                            "url": "https://en.wikipedia.org/wiki/2025_Australian_Grand_Prix",
                            "raceName": "Australian Grand Prix",
                            "Circuit": {
                                "circuitId": "albert_park",
                                "url": "https://en.wikipedia.org/wiki/Albert_Park_Circuit",
                                "circuitName": "Albert Park Grand Prix Circuit",
                                "Location": {
                                    "lat": "-37.8497",
                                    "long": "144.968",
                                    "locality": "Melbourne",
                                    "country": "Australia"
                                }
                            },
                            "date": "2025-03-16",
                            "time": "04:00:00Z",
                            "FirstPractice": {
                                "date": "2025-03-14",
                                "time": "01:30:00Z"
                            },
                            "SecondPractice": {
                                "date": "2025-03-14",
                                "time": "05:00:00Z"
                            },
                            "ThirdPractice": {
                                "date": "2025-03-15",
                                "time": "01:30:00Z"
                            },
                            "Qualifying": {
                                "date": "2025-03-15",
                                "time": "05:00:00Z"
                            }
                        },
                        {
                            "season": "2025",
                            "round": "2",
                            "url": "https://en.wikipedia.org/wiki/2025_Chinese_Grand_Prix",
                            "raceName": "Chinese Grand Prix",
                            "Circuit": {
                                "circuitId": "shanghai",
                                "url": "https://en.wikipedia.org/wiki/Shanghai_International_Circuit",
                                "circuitName": "Shanghai International Circuit",
                                "Location": {
                                    "lat": "31.3389",
                                    "long": "121.22",
                                    "locality": "Shanghai",
                                    "country": "China"
                                }
                            },
                            "date": "2025-03-23",
                            "time": "07:00:00Z",
                            "FirstPractice": {
                                "date": "2025-03-21",
                                "time": "03:30:00Z"
                            },
                            "Qualifying": {
                                "date": "2025-03-22",
                                "time": "07:00:00Z"
                            },
                            "Sprint": {
                                "date": "2025-03-22",
                                "time": "03:00:00Z"
                            },
                            "SprintQualifying": {
                                "date": "2025-03-21",
                                "time": "07:30:00Z"
                            }
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val seasonsResponse1 = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/seasons/",
                "limit": "1",
                "offset": "0",
                "total": "2",
                "SeasonTable": {
                    "Seasons": [
                        {
                            "season": "1950",
                            "url": "https://en.wikipedia.org/wiki/1950_Formula_One_season"
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val seasonsResponse2 = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/seasons/",
                "limit": "1",
                "offset": "1",
                "total": "2",
                "SeasonTable": {
                    "Seasons": [
                        {
                            "season": "1951",
                            "url": "https://en.wikipedia.org/wiki/1951_Formula_One_season"
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val driverListResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/drivers/",
                "limit": "30",
                "offset": "0",
                "total": "3",
                "DriverTable": {
                    "season": "2025",
                    "Drivers": [
                        {
                            "driverId": "norris",
                            "permanentNumber": "4",
                            "code": "NOR",
                            "url": "http://en.wikipedia.org/wiki/Lando_Norris",
                            "givenName": "Lando",
                            "familyName": "Norris",
                            "dateOfBirth": "1999-11-13",
                            "nationality": "British"
                        },
                        {
                            "driverId": "piastri",
                            "permanentNumber": "81",
                            "code": "PIA",
                            "url": "http://en.wikipedia.org/wiki/Oscar_Piastri",
                            "givenName": "Oscar",
                            "familyName": "Piastri",
                            "dateOfBirth": "2001-04-06",
                            "nationality": "Australian"
                        },
                        {
                            "driverId": "max_verstappen",
                            "permanentNumber": "33",
                            "code": "VER",
                            "url": "http://en.wikipedia.org/wiki/Max_Verstappen",
                            "givenName": "Max",
                            "familyName": "Verstappen",
                            "dateOfBirth": "1997-09-30",
                            "nationality": "Dutch"
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val constructorListResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/constructors/",
                "limit": "30",
                "offset": "0",
                "total": "3",
                "ConstructorTable": {
                    "season": "2025",
                    "Constructors": [
                        {
                            "constructorId": "mclaren",
                            "url": "http://en.wikipedia.org/wiki/McLaren",
                            "name": "McLaren",
                            "nationality": "British"
                        },
                        {
                            "constructorId": "mercedes",
                            "url": "http://en.wikipedia.org/wiki/Mercedes-Benz_in_Formula_One",
                            "name": "Mercedes",
                            "nationality": "German"
                        },
                        {
                            "constructorId": "red_bull",
                            "url": "http://en.wikipedia.org/wiki/Red_Bull_Racing",
                            "name": "Red Bull",
                            "nationality": "Austrian"
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val raceResultResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/3/results/",
                "limit": "3",
                "offset": "0",
                "total": "3",
                "RaceTable": {
                    "season": "2025",
                    "round": "3",
                    "Races": [
                        {
                            "season": "2025",
                            "round": "3",
                            "url": "https://en.wikipedia.org/wiki/2025_Japanese_Grand_Prix",
                            "raceName": "Japanese Grand Prix",
                            "Circuit": {
                                "circuitId": "suzuka",
                                "url": "https://en.wikipedia.org/wiki/Suzuka_International_Racing_Course",
                                "circuitName": "Suzuka Circuit",
                                "Location": {
                                    "lat": "34.8431",
                                    "long": "136.541",
                                    "locality": "Suzuka",
                                    "country": "Japan"
                                }
                            },
                            "date": "2025-04-06",
                            "time": "05:00:00Z",
                            "Results": [
                                {
                                    "number": "1",
                                    "position": "1",
                                    "positionText": "1",
                                    "points": "25",
                                    "Driver": {
                                        "driverId": "max_verstappen",
                                        "permanentNumber": "33",
                                        "code": "VER",
                                        "url": "http://en.wikipedia.org/wiki/Max_Verstappen",
                                        "givenName": "Max",
                                        "familyName": "Verstappen",
                                        "dateOfBirth": "1997-09-30",
                                        "nationality": "Dutch"
                                    },
                                    "Constructor": {
                                        "constructorId": "red_bull",
                                        "url": "http://en.wikipedia.org/wiki/Red_Bull_Racing",
                                        "name": "Red Bull",
                                        "nationality": "Austrian"
                                    },
                                    "grid": "1",
                                    "laps": "53",
                                    "status": "Finished",
                                    "Time": {
                                        "millis": "4926983",
                                        "time": "1:22:06.983"
                                    },
                                    "FastestLap": {
                                        "rank": "3",
                                        "lap": "52",
                                        "Time": {
                                            "time": "1:31.041"
                                        }
                                    }
                                },
                                {
                                    "number": "4",
                                    "position": "2",
                                    "positionText": "2",
                                    "points": "18",
                                    "Driver": {
                                        "driverId": "norris",
                                        "permanentNumber": "4",
                                        "code": "NOR",
                                        "url": "http://en.wikipedia.org/wiki/Lando_Norris",
                                        "givenName": "Lando",
                                        "familyName": "Norris",
                                        "dateOfBirth": "1999-11-13",
                                        "nationality": "British"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "grid": "2",
                                    "laps": "53",
                                    "status": "Finished",
                                    "Time": {
                                        "millis": "4928406",
                                        "time": "+1.423"
                                    },
                                    "FastestLap": {
                                        "rank": "5",
                                        "lap": "51",
                                        "Time": {
                                            "time": "1:31.116"
                                        }
                                    }
                                },
                                {
                                    "number": "81",
                                    "position": "3",
                                    "positionText": "3",
                                    "points": "15",
                                    "Driver": {
                                        "driverId": "piastri",
                                        "permanentNumber": "81",
                                        "code": "PIA",
                                        "url": "http://en.wikipedia.org/wiki/Oscar_Piastri",
                                        "givenName": "Oscar",
                                        "familyName": "Piastri",
                                        "dateOfBirth": "2001-04-06",
                                        "nationality": "Australian"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "grid": "3",
                                    "laps": "53",
                                    "status": "Finished",
                                    "Time": {
                                        "millis": "4929112",
                                        "time": "+2.129"
                                    },
                                    "FastestLap": {
                                        "rank": "2",
                                        "lap": "53",
                                        "Time": {
                                            "time": "1:31.039"
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val qualiResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/3/qualifying/",
                "limit": "3",
                "offset": "0",
                "total": "3",
                "RaceTable": {
                    "season": "2025",
                    "round": "3",
                    "Races": [
                        {
                            "season": "2025",
                            "round": "3",
                            "url": "https://en.wikipedia.org/wiki/2025_Japanese_Grand_Prix",
                            "raceName": "Japanese Grand Prix",
                            "Circuit": {
                                "circuitId": "suzuka",
                                "url": "https://en.wikipedia.org/wiki/Suzuka_International_Racing_Course",
                                "circuitName": "Suzuka Circuit",
                                "Location": {
                                    "lat": "34.8431",
                                    "long": "136.541",
                                    "locality": "Suzuka",
                                    "country": "Japan"
                                }
                            },
                            "date": "2025-04-06",
                            "time": "05:00:00Z",
                            "QualifyingResults": [
                                {
                                    "number": "1",
                                    "position": "1",
                                    "Driver": {
                                        "driverId": "max_verstappen",
                                        "permanentNumber": "33",
                                        "code": "VER",
                                        "url": "http://en.wikipedia.org/wiki/Max_Verstappen",
                                        "givenName": "Max",
                                        "familyName": "Verstappen",
                                        "dateOfBirth": "1997-09-30",
                                        "nationality": "Dutch"
                                    },
                                    "Constructor": {
                                        "constructorId": "red_bull",
                                        "url": "http://en.wikipedia.org/wiki/Red_Bull_Racing",
                                        "name": "Red Bull",
                                        "nationality": "Austrian"
                                    },
                                    "Q1": "1:27.943",
                                    "Q2": "1:27.502",
                                    "Q3": "1:26.983"
                                },
                                {
                                    "number": "4",
                                    "position": "2",
                                    "Driver": {
                                        "driverId": "norris",
                                        "permanentNumber": "4",
                                        "code": "NOR",
                                        "url": "http://en.wikipedia.org/wiki/Lando_Norris",
                                        "givenName": "Lando",
                                        "familyName": "Norris",
                                        "dateOfBirth": "1999-11-13",
                                        "nationality": "British"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "Q1": "1:27.845",
                                    "Q2": "1:27.146",
                                    "Q3": "1:26.995"
                                },
                                {
                                    "number": "81",
                                    "position": "3",
                                    "Driver": {
                                        "driverId": "piastri",
                                        "permanentNumber": "81",
                                        "code": "PIA",
                                        "url": "http://en.wikipedia.org/wiki/Oscar_Piastri",
                                        "givenName": "Oscar",
                                        "familyName": "Piastri",
                                        "dateOfBirth": "2001-04-06",
                                        "nationality": "Australian"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "Q1": "1:27.687",
                                    "Q2": "1:27.507",
                                    "Q3": "1:27.027"
                                }
                            ]
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val sprintResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/2025/sprint/",
                "limit": "10",
                "offset": "0",
                "total": "2",
                "RaceTable": {
                    "season": "2025",
                    "Races": [
                        {
                            "season": "2025",
                            "round": "2",
                            "url": "https://en.wikipedia.org/wiki/2025_Chinese_Grand_Prix",
                            "raceName": "Chinese Grand Prix",
                            "Circuit": {
                                "circuitId": "shanghai",
                                "url": "https://en.wikipedia.org/wiki/Shanghai_International_Circuit",
                                "circuitName": "Shanghai International Circuit",
                                "Location": {
                                    "lat": "31.3389",
                                    "long": "121.22",
                                    "locality": "Shanghai",
                                    "country": "China"
                                }
                            },
                            "date": "2025-03-23",
                            "time": "07:00:00Z",
                            "SprintResults": [
                                {
                                    "number": "81",
                                    "position": "2",
                                    "positionText": "2",
                                    "points": "7",
                                    "Driver": {
                                        "driverId": "piastri",
                                        "permanentNumber": "81",
                                        "code": "PIA",
                                        "url": "http://en.wikipedia.org/wiki/Oscar_Piastri",
                                        "givenName": "Oscar",
                                        "familyName": "Piastri",
                                        "dateOfBirth": "2001-04-06",
                                        "nationality": "Australian"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "grid": "3",
                                    "laps": "19",
                                    "status": "Finished",
                                    "Time": {
                                        "millis": "1846854",
                                        "time": "+6.889"
                                    },
                                    "FastestLap": {
                                        "rank": "4",
                                        "lap": "7",
                                        "Time": {
                                            "time": "1:35.854"
                                        }
                                    }
                                },
                                {
                                    "number": "4",
                                    "position": "8",
                                    "positionText": "8",
                                    "points": "1",
                                    "Driver": {
                                        "driverId": "norris",
                                        "permanentNumber": "4",
                                        "code": "NOR",
                                        "url": "http://en.wikipedia.org/wiki/Lando_Norris",
                                        "givenName": "Lando",
                                        "familyName": "Norris",
                                        "dateOfBirth": "1999-11-13",
                                        "nationality": "British"
                                    },
                                    "Constructor": {
                                        "constructorId": "mclaren",
                                        "url": "http://en.wikipedia.org/wiki/McLaren",
                                        "name": "McLaren",
                                        "nationality": "British"
                                    },
                                    "grid": "6",
                                    "laps": "19",
                                    "status": "Finished",
                                    "Time": {
                                        "millis": "1863436",
                                        "time": "+23.471"
                                    },
                                    "FastestLap": {
                                        "rank": "11",
                                        "lap": "4",
                                        "Time": {
                                            "time": "1:36.708"
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    private val piastriSeasonsResponse = """
        {
            "MRData": {
                "xmlns": "",
                "series": "f1",
                "url": "https://api.jolpi.ca/ergast/f1/drivers/piastri/seasons/",
                "limit": "30",
                "offset": "0",
                "total": "3",
                "SeasonTable": {
                    "driverId": "piastri",
                    "Seasons": [
                        {
                            "season": "2023",
                            "url": "https://en.wikipedia.org/wiki/2023_Formula_One_World_Championship"
                        },
                        {
                            "season": "2024",
                            "url": "https://en.wikipedia.org/wiki/2024_Formula_One_World_Championship"
                        },
                        {
                            "season": "2025",
                            "url": "https://en.wikipedia.org/wiki/2025_Formula_One_World_Championship"
                        }
                    ]
                }
            }
        }
    """.trimIndent()
}