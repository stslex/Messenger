package com.st.slex.common.messenger.activity.activity_model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.st.slex.common.messenger.auth.model.base.AuthUser

object ActivityConst{
    lateinit var AUTH: FirebaseAuth
    lateinit var USER: User
    lateinit var AUTH_USER: AuthUser
    lateinit var CURRENT_UID: String
    lateinit var REF_DATABASE_ROOT: DatabaseReference


    const val NODE_USER = "user"
    const val NODE_USERNAME = "username"
    const val NODE_PHONE = "phone"
    const val NODE_PHONE_CONTACT = "phone_contacts"


    const val CHILD_ID = "id"
    const val CHILD_PHONE = "phone"
    const val CHILD_USERNAME = "username"
    const val CHILD_NAME = "name"
    const val CHILD_STATE = "state"
    const val CHILD_FULLNAME = "fullname"

}