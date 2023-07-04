package com.project.suhail.blendr.ui.activities.home

data class UserStatus(
    var name: String = "",
    var profileImage: String = "",
    var lastUpdated: Long = 0L,
    var statuses: ArrayList<Status> = arrayListOf()
)
