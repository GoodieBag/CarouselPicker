package in.goodiebag.example.carouselview;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselview.CarouselView;
import in.goodiebag.carouselview.CustomPageTransformer;

public class MainActivity extends AppCompatActivity {
    CarouselView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (CarouselView) findViewById(R.id.view);
        List<Integer> items = new ArrayList<>();

        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
        items.add(R.drawable.icon_checkbox_tune);
//        items.add(R.drawable.i1);
//        items.add(R.drawable.i2);
//        items.add(R.drawable.i3);
//        items.add(R.drawable.i4);
//        items.add(R.drawable.i5);
//        items.add(R.drawable.i6);
//        items.add(R.drawable.i7);
//        items.add(R.drawable.i8);
//        items.add(R.drawable.i9);

        CarouselView.CarouselViewAdapter adapter = new CarouselView.CarouselViewAdapter(this, items);
        view.setOffscreenPageLimit(adapter.getCount());
        //view.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        //view.setPageTransformer(false, new CustomPageTransformer(this));
        view.setClipChildren(false);
        view.setFadingEdgeLength(0);
        view.setAdapter(adapter);

    }
}
