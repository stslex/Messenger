package st.slex.messenger.di.module.main

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import st.slex.messenger.ui.main.ContactsManager

@Module
interface ManagersModule {
    @ExperimentalCoroutinesApi
    @Binds
    fun bindsContactsManager(manager: ContactsManager.Base): ContactsManager
}