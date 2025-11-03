package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    primaryKeys = ["constructorId", "year"],
    indices = [Index("year")]
)
data class ConstructorSeasonCrossRef(
    val constructorId: String,
    val year: Int,
)

data class ConstructorWithSeasons(
    @Embedded val constructor: ConstructorEntity,
    @Relation(
        parentColumn = "constructorId",
        entityColumn = "year",
        associateBy = Junction(ConstructorSeasonCrossRef::class)
    )
    val seasons: List<SeasonEntity>,
)

data class SeasonWithConstructors(
    @Embedded val season: SeasonEntity,
    @Relation(
        parentColumn = "year",
        entityColumn = "constructorId",
        associateBy = Junction(ConstructorSeasonCrossRef::class)
    )
    val constructors: List<ConstructorEntity>,
)
