package com.drindrin.todolist.models

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class LoginResponse (
    @SerialName("token")
    var token: String,
): Serializable {
}
