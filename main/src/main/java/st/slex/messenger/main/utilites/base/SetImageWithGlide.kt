package st.slex.messenger.main.utilites.base

import android.widget.ImageView

class SetImageWithGlide(
    val makeGlideImage: (
        imageView: ImageView,
        url: String,
        needCrop: Boolean,
        needCircleCrop: Boolean,
        needOriginal: Boolean
    ) -> Unit
) {

    fun setImage(
        imageView: ImageView,
        url: String,
        needCrop: Boolean = false,
        needCircleCrop: Boolean = false,
        needOriginal: Boolean = false
    ) = makeGlideImage(imageView, url, needCrop, needCircleCrop, needOriginal)

}