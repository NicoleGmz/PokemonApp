package com.nicole.data.model

data class GenerationsListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GenerationsListItem>
)

data class GenerationsListItem(
    val name: String,
    val url: String
)