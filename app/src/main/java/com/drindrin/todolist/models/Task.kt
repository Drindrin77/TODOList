package com.drindrin.todolist.models
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Task (
    @SerialName("id")
    var id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = ""
): Serializable
{

    fun displayTask(): String{
        return "$title $description"
    }
}