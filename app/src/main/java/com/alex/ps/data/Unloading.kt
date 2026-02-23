package com.alex.ps.data

data class Unloading(
    val unloaddata: String,
    val beginhour: Int,
    val beginminute: Int,
    val endhour: Int,
    val endminute: Int,
    val volume: Int,
    val unloadingtypeid: Int,
    val unloadingtypename: String,
    val unloadingreasonid: Int,
    val unloadingreasonname: String,
    val note: String?,
    val createddate: String,
    val unloading_queue_number: Int,
    val import_percent: String
)