package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse (
    val count: Int?,
    val results: List<Pokemon>?
)