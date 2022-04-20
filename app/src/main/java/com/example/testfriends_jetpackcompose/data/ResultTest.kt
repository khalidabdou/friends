package com.example.testfriends_jetpackcompose.data

import com.google.gson.annotations.SerializedName

data class ResultTest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("sender")
    val Sender: Int,
    @SerializedName("receiver")
    val Receiver: Int,
    @SerializedName("answers")
    val answers: String,
    @SerializedName("receiverName")
    val ReceiverName: String
)

data class ListResults(
    @SerializedName("results")
    val listResults: List<ResultTest>
)
