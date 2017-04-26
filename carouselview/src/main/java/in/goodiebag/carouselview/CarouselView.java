package in.goodiebag.carouselview;

import android.content.Context;
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

public class CarouselView extends ViewPager {
    public CarouselView(Context context) {
        super(context);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static class CarouselViewAdapter extends PagerAdapter {

        List<Integer> items = new ArrayList<>();
        Context context;

        public CarouselViewAdapter(Context context, List<Integer> items){
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.page,null);
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
