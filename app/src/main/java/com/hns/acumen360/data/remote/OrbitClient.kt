package com.hns.acumen360.data.remote

import com.google.gson.GsonBuilder
import com.hns.acumen360.BuildConfig
import com.hns.acumen360.data.remote.galaxy.OrbitGalaxyService
import com.hns.acumen360.data.remote.stars.OrbitStarService
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Singleton
object OrbitClient {
    private const val NETWORK_CALL_TIMEOUT = 60
    private const val CLIENT_STARS = "HnS_Stars_Server"
    private const val CLIENT_GALAXY = "HnS_Galaxy_Server"

    private var retrofitStars: Retrofit? = null

    private val okHttpClient: OkHttpClient
        get() = OkHttpClient.Builder()
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request =
                        chain.request().newBuilder() // .addHeader(Constant.Header, authToken)
                            .build()
                    return chain.proceed(request)
                }
            })
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                        else HttpLoggingInterceptor.Level.NONE
                    })
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .build()

    val clientStars: Retrofit?
        get() {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()
            val gson = gsonBuilder.create()
            if (retrofitStars == null) {
                retrofitStars = Retrofit.Builder()
                    .baseUrl(EndPoints.BASE_STARS_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }
            return retrofitStars
        }


    fun createStars(baseUrl: String, cacheDir: File, cacheSize: Long): OrbitStarService {
        val client = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OrbitStarService::class.java)
    }

    val clientGalaxy: Retrofit?
        get() {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()
            val gson = gsonBuilder.create()
            if (retrofitStars == null) {
                retrofitStars = Retrofit.Builder()
                    .baseUrl(EndPoints.BASE_STARS_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build()
            }
            return retrofitStars
        }


    fun createGalaxy(baseUrl: String, cacheDir: File, cacheSize: Long): OrbitGalaxyService {
        val client = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OrbitGalaxyService::class.java)
    }
}
