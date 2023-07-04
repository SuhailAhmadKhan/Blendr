package com.project.suhail.blendr.ui.activities.chat

data class Message(
    var messageId: String = "",
    var message: String = "",
    var senderId: String = "",
    var timeStamp: Long = 0L,
    var feeling: Int = 0,
    var imageUrl: String = "",
)
