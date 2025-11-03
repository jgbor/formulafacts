package hu.formula.facts.network.util

import kotlinx.datetime.Instant

fun parseInstant(date: String?, time: String?): Instant? {
    return if (date != null && time != null) {
        Instant.parse("${date}T${time}")
    } else if (date != null) {
        Instant.parse("${date}T00:00:00Z")
    } else null
}

