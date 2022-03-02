package com.example.jokesapp.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
class NetworkManager{

    companion object {
        private lateinit var baseUrl: String
        private  var serviceClass : Any? =null

        /**
         * Create the logging interceptor for network call
         */
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        /**
         * Apply base url for the network call
         */
        @JvmStatic
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        /**
         * Update the service class type based on network call
         */
        @JvmStatic
        fun <T> serviceClass(serviceClass : Class<T>?) = apply { this.serviceClass = serviceClass }


        /**
         * Initialize the retrofit build URL passes with service class
         */
        @JvmStatic
        fun <T> build(withNull : Boolean = false) : T {
            val client = OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
                connectTimeout(60, TimeUnit.SECONDS)
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
            }.build()

            val gsonConverterFactory = when(withNull) { //Enable retrofit parameter with null values
                true ->  GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
                false -> GsonConverterFactory.create()
            }

            return Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .build().create(serviceClass as Class<T>)
        }


    }
}