package com.nicole.data.model

import com.google.gson.annotations.SerializedName

data class CommonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<CommonItemApiResource>
)

data class CommonItemApiResource(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)