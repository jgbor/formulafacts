package hu.formula.facts.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.formula.facts.domain.model.Driver

@Entity(tableName = "drivers")
data class DriverEntity(
    @PrimaryKey @ColumnInfo(name = "driverId") val id: String,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: String?,
    val number: Int?,
    val nationality: String?,
    val url: String?,
    val code: String?,
) {
    fun toDriver() = Driver(
        id,
        permanentNumber = number,
        code = code,
        url = url,
        givenName = givenName,
        familyName = familyName,
    )
}

fun Driver.toEntity() = DriverEntity(
    id = id,
    givenName = givenName,
    familyName = familyName,
    dateOfBirth = dateOfBirth?.toString(),
    number = permanentNumber,
    nationality = nationality,
    url = url,
    code = code,
)
