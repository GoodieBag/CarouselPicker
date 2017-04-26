package in.goodiebag.carouselview;

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
        Log.d("position", "" + view.getTag()+ "Co-ordinate position is " +  position + "\n");
    }

}
/*public class CustomPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        Log.d("position",position+"");
    *//*    if(position == 0){
            //Full width
            page.setScaleX(1);
            page.setScaleY(1);
        }else if(position < 0 && position > -0.5){
            //Reduce
            page.setScaleX((float) 0.5);
            page.setScaleY((float) 0.5);

        }else if(position > 0 && position < 0.5){
            page.setScaleX((float) 0.5);
            page.setScaleY((float) 0.5);
        }else if(position == 1 || position ==-1){
            page.setScaleY(1);
            page.setScaleX(1);
        }*//*
    }*/
//}
