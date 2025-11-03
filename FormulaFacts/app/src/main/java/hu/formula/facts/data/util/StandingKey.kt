package hu.formula.facts.data.util

class StandingKey {
    constructor(year: Int, pos: Int) {
        value = "$year$SEPARATOR$pos"
    }
    val value: String

    val year: Int
        get() = value.split(SEPARATOR)[0].toInt()

    val pos: Int
        get() = value.split(SEPARATOR)[1].toInt()

    companion object {
        private const val SEPARATOR = "-"

        fun parse(string: String): StandingKey {
            val parts = string.split(SEPARATOR)
            return StandingKey(
                year = parts[0].toInt(),
                pos = parts[1].toInt()
            )
        }
    }
}