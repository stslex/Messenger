package st.slex.messenger.ui.single_chat.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ServerValue
import st.slex.messenger.utilites.AppChildEventListener
import st.slex.messenger.utilites.AppValueEventListener
import st.slex.messenger.utilites.Const.CHILD_FROM
import st.slex.messenger.utilites.Const.CHILD_ID
import st.slex.messenger.utilites.Const.CHILD_STATE
import st.slex.messenger.utilites.Const.CHILD_TEXT
import st.slex.messenger.utilites.Const.CHILD_TIMESTAMP
import st.slex.messenger.utilites.Const.CURRENT_UID
import st.slex.messenger.utilites.Const.NODE_MAIN_LIST
import st.slex.messenger.utilites.Const.NODE_MESSAGES
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT

class ChatRepository {

    val status = MutableLiveData<String>()
    val message = MutableLiveData<Message>()

    fun initStatus(uid: String) {
        REF_DATABASE_ROOT
            .child(NODE_USER)
            .child(uid)
            .child(CHILD_STATE)
            .addValueEventListener(AppValueEventListener {
                status.value = it.value.toString()
            })
    }

    fun initMessages(uid: String, countMessage: Int) {
        REF_DATABASE_ROOT
            .child(NODE_MESSAGES)
            .child(CURRENT_UID)
            .child(uid)
            .limitToLast(countMessage)
            .addChildEventListener(AppChildEventListener {
                message.value = it.getValue(Message::class.java) ?: Message()
            })

    }

    fun sendMessage(message: String, uid: String) {

        val refDialogUser = "$NODE_MESSAGES/$CURRENT_UID/$uid"
        val refDialogReceivingUser = "$NODE_MESSAGES/$uid/$CURRENT_UID"

        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

        val mapMessage = hashMapOf<String, Any>()

        mapMessage[CHILD_FROM] = CURRENT_UID
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        REF_DATABASE_ROOT
            .updateChildren(mapDialog)
            .addOnSuccessListener { setInMainList(uid) }

    }

    private fun setInMainList(uid: String) {
        REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID).child(uid)
            .addListenerForSingleValueEvent(AppValueEventListener {
                var refUser = "$NODE_MAIN_LIST/$CURRENT_UID/$uid"
                var refReceived = "$NODE_MAIN_LIST/$uid/$CURRENT_UID"
                val mapUser = hashMapOf<String, Any>()
                val mapReceived = hashMapOf<String, Any>()

                val commonMap = hashMapOf<String, Any>()

                mapUser[CHILD_ID] = uid

                mapReceived[CHILD_ID] = CURRENT_UID

                commonMap[refUser] = mapUser
                commonMap[refReceived] = mapReceived

                REF_DATABASE_ROOT.updateChildren(commonMap)
            })

    }


}