package hu.formula.facts.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.formula.facts.domain.model.Constructor

@Entity(tableName = "constructors")
data class ConstructorEntity(
    @PrimaryKey @ColumnInfo(name = "constructorId") val id: String,
    val url: String? = null,
    val name: String,
    val nationality: String? = null,
) {
    fun toConstructor() = Constructor(
        id = id,
        url = url,
        name = name,
        nationality = nationality
    )
}

fun Constructor.toEntity() = ConstructorEntity(
    id = id,
    url = url,
    name = name,
    nationality = nationality
)
