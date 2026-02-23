package com.alex.ps.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Unloading(
    @SerialName("unloaddata") val unloadDate: String,
    @SerialName("beginhour") val beginHour: Int,
    @SerialName("beginminute") val beginMinute: Int,
    @SerialName("endhour") val endHour: Int,
    @SerialName("endminute") val endMinute: Int,
    val volume: Int,
    @SerialName("unloadingtypeid") val unloadingTypeId: Int,
    @SerialName("unloadingtypename") val unloadingTypeName: String,
    @SerialName("unloadingreasonid") val unloadingReasonId: Int,
    @SerialName("unloadingreasonname") val unloadingReasonName: String,
    val note: String? = null,
    @SerialName("createddate") val createdDate: String,
    @SerialName("unloading_queue_number") val unloadingQueueNumber: Int,
    @SerialName("import_percent") val importPercent: String
)