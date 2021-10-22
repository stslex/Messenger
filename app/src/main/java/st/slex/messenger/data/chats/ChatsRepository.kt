package st.slex.messenger.data.chats

import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
interface ChatsRepository {


    class Base @Inject constructor() : ChatsRepository
}