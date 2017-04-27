package in.goodiebag.carouselpicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavan on 25/04/17.
 */

public class CarouselPicker extends ViewPager {
    public CarouselPicker(Context context) {
        this(context, null);
    }

    public CarouselPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setPageTransformer(false, new CustomPageTransformer(getContext()));
        this.setClipChildren(false);
        this.setFadingEdgeLength(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        setPageMargin((int) (-w / 1.25));

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.setOffscreenPageLimit(adapter.getCount());
    }

    public static class CarouselViewAdapter extends PagerAdapter {

        List<Integer> items = new ArrayList<>();
        Context context;
        int drawable;

        public CarouselViewAdapter(Context context, List<Integer> items, @DrawableRes int drawable) {
            this.context = context;
            this.items = items;
            this.drawable = drawable;
            if(this.drawable == 0){
                this.drawable = R.layout.page;
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(this.drawable, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setImageResource(items.get(position));
            view.setTag(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

    }

}
