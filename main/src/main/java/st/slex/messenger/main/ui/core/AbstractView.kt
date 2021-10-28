package st.slex.messenger.main.ui.core

import st.slex.messenger.core.TextMapper

interface AbstractView {

    fun show()
    fun hide()

    interface Text : AbstractView, TextMapper.Void
    interface Card : AbstractView {
        fun transit(transitionName: String)
        fun getCard(): CustomCardView
    }

    interface Image : AbstractView {

        fun load(url: String)
        fun getImage(): CustomImageView
    }
}