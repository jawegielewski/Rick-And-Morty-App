package pl.jawegiel.rickandmortyapp.model

// @formatter:off
data class RickAndMortyData (
    val id: Int,
    val image: String,
    val name: String,
    val status: String,
    val additionalData: AdditionalData = AdditionalData(),
)
