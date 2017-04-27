package in.goodiebag.carouselpicker;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by pavan on 25/04/17.
 */

import android.content.Context;

public class CustomPageTransformer implements ViewPager.PageTransformer {

    private ViewPager viewPager;

    public CustomPageTransformer(Context context) {
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }
        view.setScaleY(1-Math.abs(position));
        view.setScaleX(1-Math.abs(position));
    }

}
