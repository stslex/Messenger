package st.slex.messenger.data.contacts

interface ContactsData {

    fun id(): String
    fun phone(): String
    fun fullName(): String

    data class Base(
        val id: String = "",
        val phone: String = "",
        val full_name: String = "",
    ) : ContactsData {

        override fun id(): String = id
        override fun phone(): String = phone
        override fun fullName(): String = full_name
    }
}