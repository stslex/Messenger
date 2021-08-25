package st.slex.messenger.utilites

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import st.slex.messenger.data.model.AuthUserModel
import st.slex.messenger.data.model.UserModel

object Const {
    lateinit var AUTH: FirebaseAuth
    lateinit var USER: UserModel
    lateinit var authUserModel: AuthUserModel
    lateinit var CURRENT_UID: String
    lateinit var REF_DATABASE_ROOT: DatabaseReference

    const val NODE_USER = "user"
    const val NODE_USERNAME = "username"
    const val NODE_PHONE = "phone"
    const val NODE_PHONE_CONTACT = "phone_contacts"
    const val NODE_MESSAGES = "messages"
    const val NODE_MAIN_LIST = "main_list"

    const val CHILD_ID = "id"
    const val CHILD_PHONE = "phone"
    const val CHILD_USERNAME = "username"
    const val CHILD_STATE = "state"
    const val CHILD_FULLNAME = "fullname"
    const val CHILD_FROM = "from"
    const val CHILD_TEXT = "text"
    const val CHILD_TIMESTAMP = "timestamp"
}