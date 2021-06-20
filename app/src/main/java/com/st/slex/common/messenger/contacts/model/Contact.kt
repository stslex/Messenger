package com.st.slex.common.messenger.contacts.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: String = "",
    val phone: String = "",
    val fullname: String = "",
    val url: String = ""
) : Parcelable