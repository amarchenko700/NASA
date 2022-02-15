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
    // for code response 403: API_KEY_INVALID
    val error: PDOErrorDetail
)

@Serializable
data class PDOErrorDetail(
    val code: String,
    val message: String
)

@Serializable
data class PDOErrorDetail400(
    // for code response 400
    val code: String,
    val msg: String
)