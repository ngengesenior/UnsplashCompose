package com.ngengeapps.unsplashcompose.networking

import com.ngengeapps.unsplashcompose.models.Photo
import com.ngengeapps.unsplashcompose.utils.Constants
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface UnsplashAPI {

    @GET("{topic}/photos")
    suspend fun getPhotos(@Path("topic") topic:String,
                          @Query("page") page:Int,
                          @Query("per_page") perPage:Int):List<Photo>
    companion object {

        private fun create(httpUrl: HttpUrl):UnsplashAPI {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(UnsplashAPI::class.java)

        }

        fun create():UnsplashAPI = create(Constants.UNSPLASH_BASE_URL.toHttpUrlOrNull()!!)

    }
}