package com.st.slex.common.messenger.contacts.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.st.slex.common.messenger.activity.activity_model.ActivityConst
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_PHONE_CONTACT
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.NODE_USER
import com.st.slex.common.messenger.activity.activity_model.ActivityConst.REF_DATABASE_ROOT
import com.st.slex.common.messenger.utilites.AppValueEventListener

class ContactRepository {

    val contact = MutableLiveData<Contact>()
    val flag = MutableLiveData<Boolean>()
    init {
        flag.value = false
    }

    fun getContacts(){
        REF_DATABASE_ROOT
            .child(NODE_PHONE_CONTACT)
            .child(ActivityConst.CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val listOfPrimaryContacts = it.children.map { snapshot ->
                    snapshot.getValue(Contact::class.java) ?: Contact()
                }
                val list = mutableListOf<Contact>()
                listOfPrimaryContacts.forEach { itemContact->
                    REF_DATABASE_ROOT
                        .child(NODE_USER)
                        .child(itemContact.id)
                        .addValueEventListener(AppValueEventListener{ upSnapshot->
                            val contactPrimary = upSnapshot.getValue(Contact::class.java)?:Contact()
                            val item = contactPrimary.copy(fullname = itemContact.fullname)
                            list.add(item)
                            contact.value = item
                            if (list.size == listOfPrimaryContacts.size){
                                flag.value = true
                            }

                        })
                }
            })
    }
}

