package dev.vnu.survey

import com.google.firebase.auth.FirebaseUser
import  dev.vnu.survey.profile.UserItem

fun FirebaseUser.toUserItem(): UserItem {
    return UserItem().apply {
        uid = this@toUserItem.uid
        url = this@toUserItem.photoUrl.toString()
        name = this@toUserItem.displayName!!
    }
}