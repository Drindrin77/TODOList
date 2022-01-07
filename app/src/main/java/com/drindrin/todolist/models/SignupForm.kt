package com.drindrin.todolist.models

import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class SignupForm (
    @SerialName("firstname")
    var firstname: String,
    @SerialName("lastname")
    var lastname: String,
    @SerialName("email")
    var email: String,
    @SerialName("password")
    var password: String,
    @SerialName("password_confirmation")
    val password_confirmation: String
): Serializable {
}


