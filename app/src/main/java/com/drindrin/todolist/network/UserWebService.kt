package com.drindrin.todolist.network

import com.drindrin.todolist.models.LoginForm
import com.drindrin.todolist.models.LoginResponse
import com.drindrin.todolist.models.SignupForm
import com.drindrin.todolist.models.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signUp(@Body user: SignupForm): Response<LoginResponse>

}

