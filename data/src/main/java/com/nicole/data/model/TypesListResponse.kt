package com.nicole.data.model

data class TypeListResponse (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<TypesListItem>
)

data class TypesListItem (
    val name: String,
    val url: String
)