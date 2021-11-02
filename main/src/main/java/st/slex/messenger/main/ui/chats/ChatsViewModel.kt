package st.slex.messenger.main.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import st.slex.messenger.core.Resource
import st.slex.messenger.main.data.chats.ChatsDataMapper
import st.slex.messenger.main.data.chats.ChatsRepository
import st.slex.messenger.main.data.user.UserDataMapper
import st.slex.messenger.main.data.user.UserRepository
import st.slex.messenger.main.domain.ChatsInteractor
import st.slex.messenger.main.ui.user_profile.UserUI
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserDataMapper,
    private val chatsInteractor: ChatsInteractor,
    private val chatsRepository: ChatsRepository,
    private val chatsMapper: ChatsDataMapper
) : ViewModel() {

    suspend fun getChats(): StateFlow<Resource<List<ChatsUI>>> =
        chatsRepository.getAllChats()
            .flatMapLatest { flowOf(it.map(chatsMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Resource.Loading
            )

    suspend fun getChatUIHead(chat: ChatsUI): StateFlow<Resource<ChatsUI>> =
        chatsInteractor.getChatUIHead(chat).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading
        )

    suspend fun currentUser(): StateFlow<Resource<UserUI>> =
        userRepository.getCurrentUser()
            .flatMapLatest { flowOf(it.map(userMapper)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Resource.Loading
            )
}


