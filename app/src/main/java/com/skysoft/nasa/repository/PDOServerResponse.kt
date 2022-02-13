package com.skysoft.nasa.repository

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class PDOServerResponse(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
)

@Serializable
data class PDOError(
    val error: PDOErrorDetail
)

@Serializable
data class PDOErrorDetail(
    val code: String,
    val message: String
)