package com.drindrin.todolist.models

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class LoginForm (
    @SerialName("email")
    var email: String,
    @SerialName("password")
    val password: String
): Serializable {
}
