package com.alex.ps.domain

data class FieldChange<T>(
    val fieldName: String,
    val old: T,
    val new: T
)