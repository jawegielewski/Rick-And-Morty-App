package pl.jawegiel.rickandmortyapp.interfaces

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// @formatter:off
interface APIInterface {

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Response<String>
}