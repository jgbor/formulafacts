package hu.formula.facts.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.formula.facts.network.f1.FormulaService
import hu.formula.facts.network.f1.JolpicaApi
import hu.formula.facts.network.f1.JolpicaApiRetrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val SERVICE_URL = "https://api.jolpi.ca/ergast/f1/"

    @Provides
    @Singleton
    fun provideJolpicaApiRetrofit(): JolpicaApiRetrofit {
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideFormulaService(
        jolpicaApi: JolpicaApiRetrofit
    ): FormulaService = JolpicaApi(jolpicaApi)
}
