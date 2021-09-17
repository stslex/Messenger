package st.slex.messenger.ui.user_profile

import st.slex.messenger.ui.core.AbstractView
import st.slex.messenger.ui.core.CustomCardView

interface UserUI {

    fun mapMainScreen(
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
    )

    fun mapProfile(
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
        bioText: AbstractView.Text,
        fullName: AbstractView.Text,
        usernameCard: AbstractView.Card
    )

    fun changeUsername(function: (CustomCardView, String) -> Unit)

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val username: String = "",
        private val url: String = "",
        private val bio: String = "",
        private val full_name: String = "",
        private val state: String = "",
    ) : UserUI {

        private var _usernameCard: CustomCardView? = null
        private val usernameCard get() = _usernameCard!!
        override fun mapMainScreen(
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            avatar.load(url)
        }

        override fun mapProfile(
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
            bioText: AbstractView.Text,
            fullName: AbstractView.Text,
            usernameCard: AbstractView.Card
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            avatar.load(url)
            bioText.map(bio)
            fullName.map(full_name)
            usernameCard.transit(id)
            _usernameCard = usernameCard.getCard()
        }

        override fun changeUsername(function: (CustomCardView, String) -> Unit) =
            function(usernameCard, username)
    }
}