package com.example.testfriends_jetpackcompose.data

import com.google.gson.annotations.SerializedName

data class Language(
    val id: Int,
    var label: String,
    var language_code: String,
    var image: String?,
    val questions: String,
) {
    companion object {
        val LANGUAGES = listOf(
            Language(
                id = 1,
                label = "English",
                language_code = "en",
                image = "https://cdn-icons-png.flaticon.com/512/197/197374.png",
                questions = "What is the capital of the United States?"
            ),
            Language(
                id = 1,
                label = "العربية",
                language_code = "ar",
                image = "https://cdn-icons-png.flaticon.com/512/323/323301.png",
                questions = "What is the capital of the United States?"
            ),
            Language(
                id = 2,
                label = "Spanish",
                language_code = "es",
                image = "spanish_flag.png",
                questions = "¿Cuál es la capital de España?"
            ),
            Language(
                id = 3,
                label = "French",
                language_code = "fr",
                image = "https://cdn-icons-png.flaticon.com/512/197/197560.png",
                questions = "Quelle est la capitale de la France?"
            ),
            Language(
                id = 4,
                label = "German",
                language_code = "de",
                image = "german_flag.png",
                questions = "Was ist die Hauptstadt Deutschlands?"
            )
        )
    }
}

data class Languages(
    @SerializedName("languages")
    var listLanguages: List<Language>
)




