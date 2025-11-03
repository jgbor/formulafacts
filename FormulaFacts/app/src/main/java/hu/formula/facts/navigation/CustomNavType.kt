package hu.formula.facts.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import kotlinx.serialization.json.Json

object CustomNavType {
    val GrandPrixType = object : NavType<GrandPrix>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): GrandPrix? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): GrandPrix {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: GrandPrix): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: GrandPrix) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val DriverType = object : NavType<Driver>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): Driver? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Driver {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Driver): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Driver) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val ConstructorType = object : NavType<Constructor>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): Constructor? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Constructor {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Constructor): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Constructor) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}