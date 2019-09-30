package top.horsttop.appcore.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.makeramen.roundedimageview.RoundedImageView

/**
 * An image view which always remains square with respect to its width.
 */
class SquaredImageView : RoundedImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}
