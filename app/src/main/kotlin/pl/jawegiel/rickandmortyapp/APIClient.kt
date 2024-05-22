package pl.jawegiel.rickandmortyapp

import okhttp3.OkHttpClient
import pl.jawegiel.rickandmortyapp.interfaces.APIInterface
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

// @formatter:off
object APIClient {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"
    fun getInstance(): APIInterface {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }
}