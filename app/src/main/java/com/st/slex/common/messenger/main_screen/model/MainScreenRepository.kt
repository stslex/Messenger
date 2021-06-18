package com.st.slex.common.messenger.main_screen.model

import androidx.lifecycle.LiveData
import com.st.slex.common.messenger.main_screen.model.base.MainMessage

class MainScreenRepository(db: MainScreenDatabase) {

    val mainMessage: LiveData<List<MainMessage>> = db.getMainList()

}