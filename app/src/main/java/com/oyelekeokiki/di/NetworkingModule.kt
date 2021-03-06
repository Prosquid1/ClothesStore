package com.oyelekeokiki.di

import com.oyelekeokiki.BuildConfig
import com.oyelekeokiki.networking.RemoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkingModule {


    @Provides
    fun provideFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideAuthorizationInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val new = originalRequest.newBuilder()
                .addHeader(BuildConfig.HEADER_AUTH_KEY, BuildConfig.API_KEY)
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
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    fun buildApiService(retrofit: Retrofit): RemoteApiService =
        retrofit.create(RemoteApiService::class.java)
}