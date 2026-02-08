package com.alex.ps.domain

fun Schedule.diff(other: Schedule): List<FieldChange<*>> {
    if (totalElectricityInHours != other.totalElectricityInHours) {
        return listOf(
            FieldChange<Float>(
                fieldName = "totalElectricityInHours",
                old = totalElectricityInHours,
                new = other.totalElectricityInHours
            )
        )
    }

    return emptyList()
}