package com.drindrin.todolist.userinfo

import com.drindrin.todolist.models.UserInfo
import com.drindrin.todolist.network.Api
import okhttp3.MultipartBody

class UserInfoRepository {
    private val webService = Api.userWebService

    suspend fun getInfo(): UserInfo? {
        val response = webService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun updateAvatar(part : MultipartBody.Part ): UserInfo? {
        val response = webService.updateAvatar(part)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUserInfo(userInfo: UserInfo): UserInfo? {
        val response = webService.update(userInfo)
        return if (response.isSuccessful) response.body() else null
    }
}