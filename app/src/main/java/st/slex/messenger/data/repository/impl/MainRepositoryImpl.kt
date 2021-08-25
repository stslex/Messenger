package st.slex.messenger.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import st.slex.messenger.data.model.UserModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.ui.main_screen.model.base.MainMessage
import st.slex.messenger.utilites.AppValueEventListener
import st.slex.messenger.utilites.Const.CURRENT_UID
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT

class MainRepositoryImpl : MainRepository {

    private var _currentUser = MutableLiveData<UserModel>()
    val currentUser: LiveData<UserModel> get() = _currentUser

    suspend fun getCurrentUser() = withContext(Dispatchers.IO) {
        REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID)
                .addValueEventListener(AppValueEventListener { snapshot ->
                    _currentUser.value = snapshot.getValue(UserModel::class.java) ?: UserModel()
                })
    }

    fun getTestList(): LiveData<List<MainMessage>> = liveData {
        this.emit(
                listOf(
                        MainMessage("1", "Helen", "Hello, Alex", "12:30"),
                        MainMessage("1", "Bob", "Wow", "11:30"),
                        MainMessage("1", "Anya", "I don't know", "10:30"),
                        MainMessage("1", "Olga", "May be it is not a good idea", "09:30")
                )
        )
    }

}