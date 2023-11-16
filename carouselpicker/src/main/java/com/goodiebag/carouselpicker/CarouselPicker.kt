package com.goodiebag.carouselpicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlin.math.max
import kotlin.math.min

/**
 * Created by pavan on 25/04/17.
 */
class CarouselPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {
    enum class Mode {
        HORIZONTAL,
        VERTICAL
    }

    private var itemsVisible = 3
    private var divisor = 0f
    private var opacity = 0f
    private var textMaxLines = 0
    private var mode = Mode.HORIZONTAL
    private var tintColor: Int? = null
    private var selectedItemTextColor: Int? = null
    private var selectedItemTintColor: Int? = null

    init {
        initAttributes(context, attrs)
        init()

        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                (adapter as CarouselViewAdapter?)!!.applyOpacity(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.CarouselPicker)
            itemsVisible = array.getInteger(R.styleable.CarouselPicker_items_visible, itemsVisible)
            divisor = 1 + 1f / (itemsVisible - 1)
            mode = Mode.entries[array.getInteger(R.styleable.CarouselPicker_orientation, 0)]
            opacity = array.getFloat(R.styleable.CarouselPicker_unselected_item_opacity, 1f)
            textMaxLines = array.getInt(R.styleable.CarouselPicker_text_max_lines, 0)

            if (array.hasValue(R.styleable.CarouselPicker_selected_item_text_color)) {
                selectedItemTextColor =
                    array.getColor(R.styleable.CarouselPicker_selected_item_text_color, Color.BLACK)
            }
            if (array.hasValue(R.styleable.CarouselPicker_selected_item_tint_color)) {
                selectedItemTintColor =
                    array.getColor(R.styleable.CarouselPicker_selected_item_tint_color, Color.BLACK)
            }
            if (array.hasValue(R.styleable.CarouselPicker_tint_color)) {
                tintColor =
                    array.getColor(R.styleable.CarouselPicker_tint_color, Color.BLACK)
            }

            array.recycle()
        }
        if (mode == Mode.VERTICAL) {
            setOverScrollMode(OVER_SCROLL_NEVER)
        }
    }

    private fun init() {
        this.setPageTransformer(false, CustomPageTransformer(mode))
        setClipChildren(false)
        setFadingEdgeLength(0)
    }

    override fun setCurrentItem(position: Int) {
        super.setCurrentItem(position)
        (adapter as CarouselViewAdapter?)!!.applyOpacity(position)
    }

    override fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        super.setCurrentItem(position, smoothScroll)
        (adapter as CarouselViewAdapter?)!!.applyOpacity(position)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mHeightMeasureSpec = heightMeasureSpec
        if (mode == Mode.HORIZONTAL || MeasureSpec.getMode(mHeightMeasureSpec) == MeasureSpec.AT_MOST) {
            // Wrap the height
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )
                val h = child.measuredHeight
                if (h > height) height = h
            }
            mHeightMeasureSpec = if (mode == Mode.HORIZONTAL) {
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            } else {
                MeasureSpec.makeMeasureSpec(height * itemsVisible, MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec)
        val w = measuredWidth
        pageMargin = (-w / divisor).toInt()
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        this.offscreenPageLimit = adapter!!.count
        val carouselAdapter = adapter as CarouselViewAdapter?
        carouselAdapter!!.setOpacity(opacity)
        carouselAdapter.setTextMaxLines(textMaxLines)
        if (selectedItemTextColor != null) {
            carouselAdapter.selectedItemTextColor = selectedItemTextColor
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (tintColor != null) carouselAdapter.tintColor = tintColor
            if (selectedItemTintColor != null) carouselAdapter.selectedItemTintColor = selectedItemTintColor
        }

        carouselAdapter.setOnPageClickedListener(object :
            CarouselViewAdapter.OnPageClickedListener {
            override fun onPageClicked(position: Int) {
                setCurrentItem(position)
            }
        })
    }

    class CarouselViewAdapter(
        private val context: Context,
        private val items: List<PickerItem>,
        private var drawable: Int = R.layout.page
    ) : PagerAdapter() {

        @JvmField
        var textColor = NOT_SPECIFIED
        private var lastSelected = 0
        private var opacity = 1f
        private var onPageClickedListener: OnPageClickedListener? = null
        private var textMaxLines = 0

        var selectedIndex: Int = 0
            set(value) {
                field = value
                onPageClickedListener!!.onPageClicked(field)
            }
            get() {
                return lastSelected
            }

        @ColorInt
        var selectedItemTextColor: Int? = null

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @ColorInt
        var selectedItemTintColor: Int? = null

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        @ColorInt
        var tintColor: Int? = null

        init {
            if (drawable == 0) {
                drawable = R.layout.page
            }
        }
        override fun getCount(): Int {
            return items.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(drawable, null)
            val iv = view.findViewById<ImageView>(R.id.iv)
            val tv = view.findViewById<TextView>(R.id.tv)
            val pickerItem = items[position]
            pickerItem.view = view
            if (position != lastSelected) {
                view.setAlpha(opacity)
            }
            if (pickerItem.hasDrawable()) {
                iv.setVisibility(VISIBLE)
                tv.visibility = GONE
                iv.setImageResource(pickerItem.drawable)
                if (pickerItem.color != NOT_SPECIFIED) {
                    iv.setColorFilter(pickerItem.color)
                }
                iv.setOnClickListener {
                    if (onPageClickedListener != null) {
                        onPageClickedListener!!.onPageClicked(position)
                    }
                }
            } else if (pickerItem.text != null) {
                iv.setVisibility(GONE)
                tv.visibility = VISIBLE
                val textItem = pickerItem as TextItem
                tv.text = textItem.text
                if (textMaxLines > 0) {
                    tv.setMaxLines(textMaxLines)
                    tv.ellipsize = TextUtils.TruncateAt.END
                }
                var color = textColor
                if (textItem.color != NOT_SPECIFIED) {
                    color = textItem.color
                }
                if (color != NOT_SPECIFIED) {
                    tv.setTextColor(color)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (tintColor != null) {
                        iv.drawable.setTint(tintColor!!)
                    }
                }

                if (textItem.textSize != 0) {
                    tv.textSize = dpToPx(textItem.textSize).toFloat()
                }
                if (textItem.font != null && textItem.fontStyle != null) {
                    tv.setTypeface(textItem.font, textItem.fontStyle!!.ordinal)
                }
                tv.setOnClickListener {
                    if (onPageClickedListener != null) {
                        onPageClickedListener!!.onPageClicked(position)
                    }
                }
            }
            view.tag = position
            container.addView(view)

            if (selectedIndex == position) {
                onPageClickedListener!!.onPageClicked(position)
            }

            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        fun setOpacity(opacity: Float) {
            var modifiedOpacity = opacity
            modifiedOpacity = max(modifiedOpacity.toDouble(), 0.0).toFloat()
            modifiedOpacity = min(modifiedOpacity.toDouble(), 1.0).toFloat()

            this.opacity = modifiedOpacity
        }

        fun applyOpacity(position: Int) {
            if (lastSelected < items.size) {
                val previous = items[lastSelected]
                val view = previous.view

                if (view != null) {
                    view.setAlpha(opacity)

                    if (selectedItemTextColor != null) {
                        val tv = view.findViewById<TextView>(R.id.tv)
                        if (previous.color != NOT_SPECIFIED) {
                            tv?.setTextColor(previous.color)
                        } else {
                            tv?.setTextColor(textColor)
                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        val iv = view.findViewById<ImageView>(R.id.iv)
                        if (tintColor != null) {
                            iv?.imageTintList = ColorStateList.valueOf(tintColor!!)
                        } else {
                            iv?.imageTintList = null
                        }
                    }
                }
            }
            val current = items[position]
            val view = current.view

            if (view != null) {
                view.setAlpha(1f)

                if (selectedItemTextColor != null) {
                    val tv = view.findViewById<TextView>(R.id.tv)
                    tv?.setTextColor(selectedItemTextColor!!)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (selectedItemTintColor != null) {
                        val iv = view.findViewById<ImageView>(R.id.iv)
                        iv?.drawable?.setTint(selectedItemTintColor!!)
                    }
                }
            }
            lastSelected = position
        }

        fun setOnPageClickedListener(listener: OnPageClickedListener?) {
            onPageClickedListener = listener
        }

        private fun dpToPx(dp: Int): Int {
            val metrics = context.resources.displayMetrics
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp.toFloat(),
                    metrics
                )
            )
        }

        fun setTextMaxLines(textMaxLines: Int) {
            this.textMaxLines = textMaxLines
        }

        interface OnPageClickedListener {
            fun onPageClicked(position: Int)
        }
    }

    /**
     * An interface which should be implemented by all the Item classes.
     * The picker only accepts items in the form of PickerItem.
     */
    interface PickerItem {
        fun hasDrawable(): Boolean
        val text: String?

        val selected: Boolean

        @get:DrawableRes
        val drawable: Int

        @get:ColorInt
        val color: Int
        var view: View?
    }

    /**
     * A PickerItem which supports text.
     */
    class TextItem(override val text: String, val textSize: Int) : PickerItem {
        enum class FontStyle {
            NORMAL,
            BOLD,
            ITALIC,
            BOLD_ITALIC
        }

        override var color = NOT_SPECIFIED
            private set
        var font: Typeface? = null
            private set
        var fontStyle: FontStyle? = null
            private set
        override var view: View? = null

        constructor(text: String, textSize: Int, @ColorInt color: Int) : this(text, textSize) {
            this.color = color
        }

        constructor(text: String, textSize: Int, font: Typeface?, style: FontStyle?) : this(
            text,
            textSize
        ) {
            this.font = font
            fontStyle = style
        }

        constructor(
            text: String,
            textSize: Int,
            @ColorInt color: Int,
            font: Typeface?,
            style: FontStyle?
        ) : this(text, textSize, color) {
            this.font = font
            fontStyle = style
        }

        override fun hasDrawable(): Boolean {
            return false
        }

        override val drawable: Int
            get() = 0

        override val selected: Boolean
            get() = false
    }

    /**
     * A PickerItem which supports drawables.
     */
    class DrawableItem(
        @field:DrawableRes @get:DrawableRes
        @param:DrawableRes override val drawable: Int,
    ) : PickerItem {
        override var color = NOT_SPECIFIED
            private set
        override var view: View? = null

        constructor(@DrawableRes drawable: Int, @ColorInt color: Int) : this(drawable) {
            this.color = color
        }

        override val text: String?
            get() = null

        override val selected: Boolean
            get() = false

        override fun hasDrawable(): Boolean {
            return true
        }
    }

    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted: Boolean
        if (mode == Mode.VERTICAL) {
            intercepted = super.onInterceptTouchEvent(swapXY(ev))
            swapXY(ev) // return touch coordinates to original reference frame for any child views
        } else {
            intercepted = super.onInterceptTouchEvent(ev)
        }
        return intercepted
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val handled = if (mode == Mode.VERTICAL) {
            super.onTouchEvent(swapXY(ev))
        } else {
            super.onTouchEvent(ev)
        }
        return handled
    }

    companion object {
        var NOT_SPECIFIED = 0
    }
}
