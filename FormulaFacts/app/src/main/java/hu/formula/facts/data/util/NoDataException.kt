package hu.formula.facts.data.util

import kotlin.reflect.KClass

class NoDataException(val type: KClass<*>) : Exception("Data not found for ${type.simpleName}")