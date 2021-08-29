package st.slex.messenger.data.repository.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import st.slex.messenger.data.model.MessageModel
import st.slex.messenger.data.repository.interf.MainRepository
import st.slex.messenger.data.service.DatabaseSnapshot
import st.slex.messenger.utilites.Const.CURRENT_UID
import st.slex.messenger.utilites.Const.NODE_USER
import st.slex.messenger.utilites.Const.REF_DATABASE_ROOT
import st.slex.messenger.utilites.result.EventResponse
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainRepositoryImpl @Inject constructor(private val service: DatabaseSnapshot) :
    MainRepository {

    override suspend fun getCurrentUser(): Flow<EventResponse> =
        service.valueEventFlow(REF_DATABASE_ROOT.child(NODE_USER).child(CURRENT_UID))

    override suspend fun getTestList(): Flow<List<MessageModel>> = callbackFlow {
        val event = trySend(
            listOf(
                MessageModel("1", "Helen", "Hello, Alex", "12:30"),
                MessageModel("1", "Bob", "Wow", "11:30"),
                MessageModel("1", "Anya", "I don't know", "10:30"),
                MessageModel("1", "Olga", "May be it is not a good idea", "09:30")
            )
        )
        awaitClose { event.isClosed }

    }
}