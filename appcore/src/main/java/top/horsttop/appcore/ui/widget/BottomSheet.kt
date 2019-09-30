package top.horsttop.appcore.ui.widget

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.annotation.UiThread
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import top.horsttop.appcore.R
import top.horsttop.appcore.extention.dp2px


class BottomSheet protected constructor(val builder: Builder?) {
    lateinit var iconImageView: ImageView
        protected set
    lateinit var titleTextView: TextView
        protected set
    lateinit var contentTextView: TextView
        protected set
    lateinit protected var vCustomView: FrameLayout
    lateinit var negativeButton: TextView
        protected set
    lateinit var positiveButton: TextView
        protected set
    protected lateinit var llBottom: LinearLayout
    protected lateinit var llHead: LinearLayout
    protected lateinit var root: LinearLayout

    init {
        this.builder!!.bottomDialog = initBottomDialog(builder)
    }

    @UiThread
    fun show() {
        if (builder != null && builder.bottomDialog != null)
            builder.bottomDialog!!.show()
    }

    @UiThread
    fun dismiss() {
        if (builder != null && builder.bottomDialog != null)
            builder.bottomDialog!!.dismiss()
    }

    @UiThread
    private fun initBottomDialog(builder: Builder): Dialog {
        val bottomDialog = Dialog(builder.context, R.style.BottomDialogs)
        val view = LayoutInflater.from(builder.context).inflate(R.layout.layout_bottom_dialog, null)

        iconImageView = view.findViewById<View>(R.id.bottomDialog_icon) as ImageView
        titleTextView = view.findViewById<View>(R.id.bottomDialog_title) as TextView
        contentTextView = view.findViewById<View>(R.id.bottomDialog_content) as TextView
        vCustomView = view.findViewById<View>(R.id.bottomDialog_custom_view) as FrameLayout
        negativeButton = view.findViewById<View>(R.id.bottomDialog_cancel) as TextView
        positiveButton = view.findViewById<View>(R.id.bottomDialog_ok) as TextView
        llBottom = view.findViewById<View>(R.id.ll_bottom) as LinearLayout
        llHead = view.findViewById<View>(R.id.ll_head) as LinearLayout
        root = view.findViewById<View>(R.id.root) as LinearLayout



        if (builder.icon != null) {
            iconImageView.visibility = View.VISIBLE
            iconImageView.setImageDrawable(builder.icon)
        }

        if (builder.title != null) {
            titleTextView.text = builder.title
            titleTextView.visibility = View.VISIBLE
            llHead.visibility = View.VISIBLE
        }

        if (builder.content != null) {
            contentTextView.text = builder.content
        }

        if (builder.customView != null) {
            if (builder.customView!!.parent != null)
                (builder.customView!!.parent as ViewGroup).removeAllViews()
            vCustomView.addView(builder.customView)
            vCustomView.setPadding(builder.customViewPaddingLeft, builder.customViewPaddingTop, builder.customViewPaddingRight, builder.customViewPaddingBottom)
            vCustomView.visibility = View.VISIBLE
        }

        if (builder.btn_positive != null) {
            positiveButton.visibility = View.VISIBLE
            positiveButton.text = builder.btn_positive
            positiveButton.setOnClickListener {
                if (builder.btn_positive_callback != null)
                    builder.btn_positive_callback!!.onClick(this@BottomSheet)
                if (builder.isAutoDismiss)
                    bottomDialog.dismiss()
            }

            if (builder.btn_colorPositive != 0) {
                positiveButton.setTextColor(builder.btn_colorPositive)
            }

            if (builder.btn_colorPositiveBackground == 0) {
                val v = TypedValue()
                val hasColorPrimary = builder.context.theme.resolveAttribute(R.attr.colorPrimary, v, true)
                builder.btn_colorPositiveBackground = if (!hasColorPrimary) v.data else ContextCompat.getColor(builder.context, R.color.colorPrimary)
            }

            val buttonBackground = createButtonBackgroundDrawable(builder.context, builder.btn_colorPositiveBackground)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                positiveButton.background = buttonBackground
            } else {

                positiveButton.setBackgroundDrawable(buttonBackground)
            }
        }

        if (builder.btn_negative != null) {
            negativeButton.visibility = View.VISIBLE
            negativeButton.text = builder.btn_negative
            negativeButton.setOnClickListener {
                if (builder.btn_negative_callback != null)
                    builder.btn_negative_callback!!.onClick(this@BottomSheet)
                if (builder.isAutoDismiss)
                    bottomDialog.dismiss()
            }

            if (builder.btn_colorNegative != 0) {
                negativeButton.setTextColor(builder.btn_colorNegative)
            }
        } else if (builder.btn_positive == null) {
            llBottom.visibility = View.GONE
        }


        bottomDialog.setContentView(view)
        bottomDialog.setCancelable(builder.isCancelable)
        bottomDialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        if (builder.height != 0) {

            val params = root.layoutParams
            params.height = builder.height
            root.layoutParams = params
        }
        bottomDialog.window!!.setGravity(Gravity.BOTTOM)

        return bottomDialog
    }

    class Builder(var context: Context) {

        // Bottom Dialog
        var bottomDialog: Dialog? = null

        // Icon, Title and Content
        var icon: Drawable? = null
        var title: CharSequence? = null
        var content: CharSequence? = null

        // Buttons
        var btn_negative: CharSequence? = null
        var btn_positive: CharSequence? = null
        var btn_negative_callback: ButtonCallback? = null
        var btn_positive_callback: ButtonCallback? = null
        var isAutoDismiss: Boolean = false

        // Button text colors
        var btn_colorNegative: Int = 0
        var btn_colorPositive: Int = 0

        // Button background colors
        var btn_colorPositiveBackground: Int = 0

        // Custom View
        var customView: View? = null
        var customViewPaddingLeft: Int = 0
        var customViewPaddingTop: Int = 0
        var customViewPaddingRight: Int = 0
        var customViewPaddingBottom: Int = 0

        // Other options
        var isCancelable: Boolean = false

        var height: Int = 0


        init {
            this.isCancelable = true
            this.isAutoDismiss = true
        }

        fun setTitle(@StringRes titleRes: Int): Builder {
            setTitle(this.context.getString(titleRes))
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setContent(@StringRes contentRes: Int): Builder {
            setContent(this.context.getString(contentRes))
            return this
        }

        fun setContent(content: CharSequence): Builder {
            this.content = content
            return this
        }

        fun setIcon(icon: Drawable): Builder {
            this.icon = icon
            return this
        }

        fun setIcon(@DrawableRes iconRes: Int): Builder {
            this.icon = ResourcesCompat.getDrawable(context.resources, iconRes, null)
            return this
        }

        fun setHeight(height: Int): Builder {
            this.height = height
            return this
        }

        fun setPositiveBackgroundColorResource(@ColorRes buttonColorRes: Int): Builder {
            this.btn_colorPositiveBackground = ResourcesCompat.getColor(context.resources, buttonColorRes, null)
            return this
        }

        fun setPositiveBackgroundColor(color: Int): Builder {
            this.btn_colorPositiveBackground = color
            return this
        }

        fun setPositiveTextColorResource(@ColorRes textColorRes: Int): Builder {
            this.btn_colorPositive = ResourcesCompat.getColor(context.resources, textColorRes, null)
            return this
        }

        fun setPositiveTextColor(color: Int): Builder {
            this.btn_colorPositive = color
            return this
        }

        fun setPositiveText(@StringRes buttonTextRes: Int): Builder {
            setPositiveText(this.context.getString(buttonTextRes))
            return this
        }

        fun setPositiveText(buttonText: CharSequence): Builder {
            this.btn_positive = buttonText
            return this
        }

        fun onPositive(buttonCallback: ButtonCallback): Builder {
            this.btn_positive_callback = buttonCallback
            return this
        }

        fun setNegativeTextColorResource(@ColorRes textColorRes: Int): Builder {
            this.btn_colorNegative = ResourcesCompat.getColor(context.resources, textColorRes, null)
            return this
        }

        fun setNegativeTextColor(color: Int): Builder {
            this.btn_colorNegative = color
            return this
        }

        fun setNegativeText(@StringRes buttonTextRes: Int): Builder {
            setNegativeText(this.context.getString(buttonTextRes))
            return this
        }

        fun setNegativeText(buttonText: CharSequence): Builder {
            this.btn_negative = buttonText
            return this
        }

        fun onNegative(buttonCallback: ButtonCallback): Builder {
            this.btn_negative_callback = buttonCallback
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.isCancelable = cancelable
            return this
        }

        fun autoDismiss(autodismiss: Boolean): Builder {
            this.isAutoDismiss = autodismiss
            return this
        }

        fun setCustomView(customView: View): Builder {
            this.customView = customView
            this.customViewPaddingLeft = 0
            this.customViewPaddingRight = 0
            this.customViewPaddingTop = 0
            this.customViewPaddingBottom = 0
            return this
        }

        fun setCustomView(customView: View, left: Int, top: Int, right: Int, bottom: Int): Builder {
            this.customView = customView
            this.customViewPaddingLeft = context.dp2px(left.toFloat())
            this.customViewPaddingRight = context.dp2px(right.toFloat())
            this.customViewPaddingTop = context.dp2px(top.toFloat())
            this.customViewPaddingBottom = context.dp2px(bottom.toFloat())
            return this
        }

        @UiThread
        fun build(): BottomSheet {
            return BottomSheet(this)
        }

        @UiThread
        fun show(): BottomSheet {
            val bottomDialog = build()
            bottomDialog.show()
            return bottomDialog
        }

    }

    interface ButtonCallback {

        fun onClick(dialog: BottomSheet)
    }


    private fun createButtonBackgroundDrawable(context: Context, fillColor: Int): Drawable {
        val buttonCornerRadius = context.dp2px(2F)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val v = TypedValue()
            val hasAttribute = context.theme.resolveAttribute(R.attr.colorControlHighlight, v, true)
            val rippleColor = if (hasAttribute) v.data else Color.parseColor("#88CCCCCC")
            return createButtonBackgroundDrawableLollipop(fillColor, rippleColor, buttonCornerRadius)
        }
        return createButtonBackgroundDrawableBase(fillColor, buttonCornerRadius)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createButtonBackgroundDrawableLollipop(fillColor: Int, rippleColor: Int, cornerRadius: Int): Drawable {
        val d = createButtonBackgroundDrawableBase(fillColor, cornerRadius)
        return RippleDrawable(ColorStateList.valueOf(rippleColor), d, null)
    }

    private fun createButtonBackgroundDrawableBase(color: Int, cornerRadius: Int): Drawable {
        val d = GradientDrawable()
        d.shape = GradientDrawable.RECTANGLE
        d.cornerRadius = cornerRadius.toFloat()
        d.setColor(color)
        return d
    }

}
