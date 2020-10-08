package in.goodiebag.carouselpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * Created by pavan on 25/04/17.
 */

public class CarouselPicker extends ViewPager {
    public static int NOT_SPECIFIED;

    public enum Mode {
        HORIZONTAL,
        VERTICAL
    }

    private int itemsVisible = 3;
    private float divisor;
    private float opacity;
    private Mode mode = Mode.HORIZONTAL;

    public CarouselPicker(Context context) {
        this(context, null);
    }

    public CarouselPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
        init();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                ((CarouselViewAdapter)getAdapter()).applyOpacity(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CarouselPicker);
            itemsVisible = array.getInteger(R.styleable.CarouselPicker_items_visible, itemsVisible);
            divisor = 1 + ((float) 1 / (itemsVisible - 1));
            mode = Mode.values()[array.getInteger(R.styleable.CarouselPicker_orientation, 0)];
            opacity = array.getFloat(R.styleable.CarouselPicker_unselected_item_opacity, 1);
            array.recycle();
        }

        if (mode == Mode.VERTICAL) {
            setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    private void init() {
        this.setPageTransformer(false, new CustomPageTransformer(getContext(), mode));
        this.setClipChildren(false);
        this.setFadingEdgeLength(0);
    }

    @Override
    public void setCurrentItem(int position) {
        super.setCurrentItem(position);
        ((CarouselViewAdapter)getAdapter()).applyOpacity(position);
    }

    @Override
    public void setCurrentItem(int position, boolean smoothScroll) {
        super.setCurrentItem(position, smoothScroll);
        ((CarouselViewAdapter)getAdapter()).applyOpacity(position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mode == Mode.HORIZONTAL || MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            // Wrap the height
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }

            if (mode == Mode.HORIZONTAL) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            } else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * itemsVisible, MeasureSpec.EXACTLY);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        setPageMargin((int) (-w / divisor));
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.setOffscreenPageLimit(adapter.getCount());

        CarouselViewAdapter carouselAdapter = ((CarouselViewAdapter)adapter);
        carouselAdapter.setOpactiy(opacity);
        carouselAdapter.setOnPageClickedListener(new CarouselViewAdapter.OnPageClickedListener() {
            @Override
            public void onPageClicked(int position) {
                setCurrentItem(position);
            }
        });
    }

    public static class CarouselViewAdapter extends PagerAdapter {
        private List<PickerItem> items;
        private Context context;
        private int drawable;
        private int textColor = NOT_SPECIFIED;
        private int lastSelected;
        private int currentSelection = 0;
        private float opacity = 1;
        private OnPageClickedListener onPageClickedListener;

        public CarouselViewAdapter(Context context, List<PickerItem> items, int drawable) {
            this.context = context;
            this.drawable = drawable;
            this.items = items;
            if (this.drawable == 0) {
                this.drawable = R.layout.page;
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(context).inflate(this.drawable, null);
            ImageView iv = view.findViewById(R.id.iv);
            TextView tv = view.findViewById(R.id.tv);

            PickerItem pickerItem = items.get(position);

            pickerItem.setView(view);
            if (position != currentSelection) {
                view.setAlpha(opacity);
            }

            if (pickerItem.hasDrawable()) {
                iv.setVisibility(VISIBLE);
                tv.setVisibility(GONE);

                iv.setImageResource(pickerItem.getDrawable());

                if (pickerItem.getColor() != NOT_SPECIFIED) {
                    iv.setColorFilter(pickerItem.getColor());
                }

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPageClickedListener != null) {
                            onPageClickedListener.onPageClicked(position);
                        }
                    }
                });
            } else if (pickerItem.getText() != null) {
                iv.setVisibility(GONE);
                tv.setVisibility(VISIBLE);

                TextItem textItem = (TextItem) pickerItem;
                tv.setText(textItem.getText());

                int color = textColor;

                if (textItem.getColor() != NOT_SPECIFIED) {
                    color = textItem.getColor();
                }

                if (color != NOT_SPECIFIED) {
                    tv.setTextColor(color);
                }

                if (textItem.getTextSize() != 0) {
                    tv.setTextSize(dpToPx(textItem.getTextSize()));
                }

                if (textItem.getFont() != null && textItem.getFontStyle() != null) {
                    tv.setTypeface(textItem.getFont(), textItem.getFontStyle().ordinal());
                }

                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPageClickedListener != null) {
                            onPageClickedListener.onPageClicked(position);
                        }
                    }
                });
            }

            view.setTag(position);
            container.addView(view);
            return view;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(@ColorInt int textColor) {
            this.textColor = textColor;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        private void setOpactiy(float opactiy) {
            opactiy = Math.max(opactiy, 0);
            opactiy = Math.min(opactiy, 1);
            this.opacity = opactiy;
        }

        private void applyOpacity(int position) {
            if (lastSelected < items.size()) {
                PickerItem previous = items.get(lastSelected);
                previous.getView().setAlpha(opacity);
            }

            PickerItem current = items.get(position);
            current.getView().setAlpha(1);

            lastSelected = position;
        }

        public void setOnPageClickedListener(OnPageClickedListener listener) {
            this.onPageClickedListener = listener;
        }

        private int dpToPx(int dp) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics));
        }

        interface OnPageClickedListener {
            void onPageClicked(int position);
        }
    }

    /**
     * An interface which should be implemented by all the Item classes.
     * The picker only accepts items in the form of PickerItem.
     */
    public interface PickerItem {
        boolean hasDrawable();

        String getText();

        @DrawableRes
        int getDrawable();

        @ColorInt
        int getColor();

        void setView(View view);

        View getView();
    }

    /**
     * A PickerItem which supports text.
     */
    public static class TextItem implements PickerItem {
        public enum FontStyle {
            NORMAL,
            BOLD,
            ITALIC,
            BOLD_ITALIC
        }

        private String text;
        private int textSize;
        private int color = NOT_SPECIFIED;
        private Typeface font;
        private FontStyle fontStyle;
        private View view;

        public TextItem(String text, int textSize) {
            this.text = text;
            this.textSize = textSize;
        }

        public TextItem(String text, int textSize, @ColorInt int color) {
            this(text, textSize);
            this.color = color;
        }

        public TextItem(String text, int textSize, Typeface font, FontStyle style) {
            this(text, textSize);
            this.font = font;
            this.fontStyle = style;
        }

        public TextItem(String text, int textSize, @ColorInt int color, Typeface font, FontStyle style) {
            this(text, textSize, color);
            this.font = font;
            this.fontStyle = style;
        }

        public String getText() { return text; }
        public int getTextSize() { return textSize; }
        public Typeface getFont() { return font; }
        public FontStyle getFontStyle() { return fontStyle; }

        @Override
        public boolean hasDrawable() {
            return false;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public int getColor() { return color; }

        @Override
        public void setView(View view) { this.view = view; }

        @Override
        public View getView() { return this.view; }
    }

    /**
     * A PickerItem which supports drawables.
     */
    public static class DrawableItem implements PickerItem {
        @DrawableRes
        private int drawable;
        private int color = NOT_SPECIFIED;
        private View view;

        public DrawableItem(@DrawableRes int drawable) {
            this.drawable = drawable;
        }

        public DrawableItem(@DrawableRes int drawable, @ColorInt int color) {
            this(drawable);
            this.color = color;
        }

        @Override
        public String getText() {
            return null;
        }

        @DrawableRes
        public int getDrawable() {
            return drawable;
        }

        @Override
        public int getColor() { return color; }

        @Override
        public boolean hasDrawable() {
            return true;
        }

        @Override
        public void setView(View view) { this.view = view; }

        @Override
        public View getView() { return this.view; }
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean intercepted;

        if (mode == Mode.VERTICAL) {
            intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev); // return touch coordinates to original reference frame for any child views
        } else {
            intercepted = super.onInterceptTouchEvent(ev);
        }

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean handled;

        if (mode == Mode.VERTICAL) {
            handled = super.onTouchEvent(swapXY(ev));
        } else {
            handled = super.onTouchEvent(ev);
        }

        return handled;
    }
}
