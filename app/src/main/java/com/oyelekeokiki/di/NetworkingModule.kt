package com.oyelekeokiki.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.oyelekeokiki.BuildConfig
import com.oyelekeokiki.networking.RemoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkingModule {

  private const val HEADER_AUTHORIZATION = "Authorization"
  private const val BASE_URL = "https://2klqdzs603.execute-api.eu-west-2.amazonaws.com/cloths/"

  @Provides
  fun provideFactory(): Converter.Factory {
    val contentType = "application/json".toMediaType()

    return Json.nonstrict.asConverterFactory(contentType)
  }

  @Provides
  fun provideAuthorizationInterceptor() = object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
      val originalRequest = chain.request()
      val new = originalRequest.newBuilder()
        .addHeader(HEADER_AUTHORIZATION, BuildConfig.API_KEY)
        .build()

      return chain.proceed(new)
    }
  }

  @Provides
  fun provideClient(interceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
      .addInterceptor(interceptor)
      .build()


  @Provides
  fun buildRetrofit(client: OkHttpClient, factory: Converter.Factory): Retrofit {
    return Retrofit.Builder()
      .client(client)
      .baseUrl(BASE_URL)
      .addConverterFactory(factory)
      .build()
  }

  @Provides
  fun buildApiService(retrofit: Retrofit): RemoteApiService =
    retrofit.create(RemoteApiService::class.java)
}