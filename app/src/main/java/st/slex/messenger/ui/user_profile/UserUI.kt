package st.slex.messenger.ui.user_profile

import com.google.android.material.appbar.MaterialToolbar
import st.slex.messenger.ui.core.AbstractView
import st.slex.messenger.ui.core.CustomCardView
import st.slex.messenger.utilites.base.SetImageWithGlide

interface UserUI {
    fun mapMainScreen(
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
    )

    fun mapProfile(
        glide: SetImageWithGlide,
        phoneNumber: AbstractView.Text,
        userName: AbstractView.Text,
        avatar: AbstractView.Image,
        bioText: AbstractView.Text,
        fullName: AbstractView.Text,
        usernameCard: AbstractView.Card,
        toolbar: MaterialToolbar
    )

    fun mapChat(
        userName: AbstractView.Text,
        stateText: AbstractView.Text
    )

    fun changeUsername(function: (CustomCardView, String) -> Unit)

    val getUrl: String

    fun id(): String
    fun phone(): String
    fun username(): String
    fun url(): String
    fun bio(): String
    fun fullName(): String
    fun state(): String

    data class Base(
        private val id: String = "",
        private val phone: String = "",
        private val username: String = "",
        private val url: String = "",
        private val bio: String = "",
        private val full_name: String = "",
        private val state: String = "",
    ) : UserUI {

        override val getUrl = url
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
            glide: SetImageWithGlide,
            phoneNumber: AbstractView.Text,
            userName: AbstractView.Text,
            avatar: AbstractView.Image,
            bioText: AbstractView.Text,
            fullName: AbstractView.Text,
            usernameCard: AbstractView.Card,
            toolbar: MaterialToolbar
        ) {
            phoneNumber.map(phone)
            userName.map(username)
            glide.setImage(avatar.getImage(), url, needCrop = false)
            bioText.map(bio)
            fullName.map(full_name)
            usernameCard.transit(username)
            _usernameCard = usernameCard.getCard()
            toolbar.title = username
        }

        override fun mapChat(userName: AbstractView.Text, stateText: AbstractView.Text) {
            userName.map(username)
            stateText.map(state)
        }

        override fun changeUsername(function: (CustomCardView, String) -> Unit) =
            function(usernameCard, username)

        override fun id(): String = id
        override fun phone(): String = phone
        override fun username(): String = username
        override fun url(): String = url
        override fun bio(): String = bio
        override fun fullName(): String = full_name
        override fun state(): String = state
    }
}