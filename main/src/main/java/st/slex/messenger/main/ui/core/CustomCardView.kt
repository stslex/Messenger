package st.slex.messenger.main.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CustomCardView : com.google.android.material.card.MaterialCardView, AbstractView.Card {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun transit(transitionName: String) {
        this.transitionName = transitionName
    }

    override fun getCard(): CustomCardView = this

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}