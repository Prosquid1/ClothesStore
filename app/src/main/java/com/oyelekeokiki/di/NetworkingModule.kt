package com.oyelekeokiki.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.oyelekeokiki.BuildConfig
import com.oyelekeokiki.networking.NetworkStatusChecker
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
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkingModule {

    private const val HEADER_AUTHORIZATION_KEY = "X-API-KEY"
    private const val BASE_URL = "https://2klqdzs603.execute-api.eu-west-2.amazonaws.com/cloths/"

    @Provides
    fun provideFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideAuthorizationInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val new = originalRequest.newBuilder()
                .addHeader(HEADER_AUTHORIZATION_KEY, BuildConfig.API_KEY)
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

    @Provides
    fun provideNetworkChecker(app: Application): NetworkStatusChecker =
        NetworkStatusChecker(app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)


}