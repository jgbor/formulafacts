package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import hu.formula.facts.domain.model.QualifyingResult

@Entity(tableName = "qualifying_results", primaryKeys = ["year", "round", "number"])
data class QualifyingResultEntity(
    val year: Int,
    val round: Int,
    val number: Int,
    val position: Int? = null,
    val driverId: String,
    val consId: String? = null,
    val q1Time: String? = null,
    val q2Time: String? = null,
    val q3Time: String? = null,
)

data class QualiResult(
    @Embedded val result: QualifyingResultEntity,
    @Relation(
        parentColumn = "consId",
        entityColumn = "constructorId",
    )
    val constructor: ConstructorEntity?,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId",
    )
    val driver: DriverEntity
) {
    fun toQualifyingResult() = QualifyingResult(
        position = result.position,
        number = result.number,
        driver = driver.toDriver(),
        constructor = constructor?.toConstructor(),
        q1Time = result.q1Time,
        q2Time = result.q2Time,
        q3Time = result.q3Time,
    )
}

fun QualifyingResult.toQualifyingResultEntity(year: Int, round: Int) = QualifyingResultEntity(
    year = year,
    round = round,
    number = number,
    position = position,
    driverId = driver.id,
    consId = constructor?.id,
    q1Time = q1Time,
    q2Time = q2Time,
    q3Time = q3Time,
)
