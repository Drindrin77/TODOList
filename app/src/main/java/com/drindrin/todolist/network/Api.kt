package com.drindrin.todolist.network

import android.content.Context
import com.google.android.material.internal.ContextUtils.getActivity
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit



const val SHARED_PREF_TOKEN_KEY = "auth_token_key"

object Api {

    private lateinit var appContext: Context

    // constantes qui serviront à faire les requêtes
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"

    fun setUpContext(context: Context) {
        appContext = context
    }

    fun getToken(): String? {
        return appContext.getSharedPreferences("MY_APP", Context.MODE_PRIVATE).getString(SHARED_PREF_TOKEN_KEY, null)
    }

    // client HTTP
    private val okHttpClient by lazy {
        val token = getToken()
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // sérializeur JSON: transforme le JSON en objets kotlin et inversement
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    // instance de convertisseur qui parse le JSON renvoyé par le serveur:
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    // permettra d'implémenter les services que nous allons créer:
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    val userWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}