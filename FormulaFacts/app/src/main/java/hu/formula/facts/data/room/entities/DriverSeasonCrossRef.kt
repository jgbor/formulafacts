package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    primaryKeys = ["driverId", "year"],
    indices = [Index("year")]
)
data class DriverSeasonCrossRef(
    val driverId: String,
    val year: Int,
)

data class DriverWithSeasons(
    @Embedded val driver: DriverEntity,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "year",
        associateBy = Junction(DriverSeasonCrossRef::class)
    )
    val seasons: List<SeasonEntity>,
)

data class SeasonWithDrivers(
    @Embedded val season: SeasonEntity,
    @Relation(
        parentColumn = "year",
        entityColumn = "driverId",
        associateBy = Junction(DriverSeasonCrossRef::class)
    )
    val drivers: List<DriverEntity>,
)
