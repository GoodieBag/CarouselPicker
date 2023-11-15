package in.goodiebag.carouselpicker;

import android.content.Context;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by pavan on 25/04/17.
 */

public class CustomPageTransformer implements ViewPager.PageTransformer {
    private ViewPager viewPager;
    private CarouselPicker.Mode mode;

    public CustomPageTransformer(Context context, CarouselPicker.Mode mode) {
        this.mode = mode;
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }

        view.setScaleY(1-Math.abs(position));
        view.setScaleX(1-Math.abs(position));

        if (mode == CarouselPicker.Mode.VERTICAL) {
            // Counteract the default slide transition
            view.setTranslationX(view.getWidth() * -position);

            //set Y position to swipe in from top
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }
}
