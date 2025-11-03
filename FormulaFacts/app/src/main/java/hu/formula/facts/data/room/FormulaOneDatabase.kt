package hu.formula.facts.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.formula.facts.data.room.converter.InstantConverter
import hu.formula.facts.data.room.converter.LocalDateConverter
import hu.formula.facts.data.room.converter.RaceTypeConverter
import hu.formula.facts.data.room.converter.StandingKeyConverter
import hu.formula.facts.data.room.dao.ConstructorDao
import hu.formula.facts.data.room.dao.DriverDao
import hu.formula.facts.data.room.dao.RaceDao
import hu.formula.facts.data.room.dao.SeasonDao
import hu.formula.facts.data.room.dao.StandingDao
import hu.formula.facts.data.room.entities.ConstructorEntity
import hu.formula.facts.data.room.entities.ConstructorSeasonCrossRef
import hu.formula.facts.data.room.entities.ConstructorStandingEntity
import hu.formula.facts.data.room.entities.DriverEntity
import hu.formula.facts.data.room.entities.DriverSeasonCrossRef
import hu.formula.facts.data.room.entities.DriverStandingConstructorCrossRef
import hu.formula.facts.data.room.entities.DriverStandingEntity
import hu.formula.facts.data.room.entities.QualifyingResultEntity
import hu.formula.facts.data.room.entities.RaceEntity
import hu.formula.facts.data.room.entities.RaceResultEntity
import hu.formula.facts.data.room.entities.SeasonEntity

@Database(
    entities = [DriverEntity::class, ConstructorEntity::class, RaceEntity::class,
        SeasonEntity::class, DriverSeasonCrossRef::class, ConstructorSeasonCrossRef::class,
        ConstructorStandingEntity::class, DriverStandingEntity::class,
        DriverStandingConstructorCrossRef::class, RaceResultEntity::class, QualifyingResultEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    LocalDateConverter::class,
    InstantConverter::class,
    RaceTypeConverter::class,
    StandingKeyConverter::class
)
abstract class FormulaOneDatabase : RoomDatabase() {
    abstract val driverDao: DriverDao
    abstract val constructorDao: ConstructorDao
    abstract val raceDao: RaceDao
    abstract val seasonDao: SeasonDao
    abstract val standingDao: StandingDao
}
