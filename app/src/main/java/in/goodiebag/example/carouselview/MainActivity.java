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
        items.add(R.mipmap.ic_launcher);
        items.add(R.mipmap.ic_launcher);
        items.add(R.mipmap.ic_launcher);

        CarouselView.CarouselViewAdapter adapter = new CarouselView.CarouselViewAdapter(this, items);
        view.setOffscreenPageLimit(3);
        view.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        view.setPageTransformer(false, new CustomPageTransformer(this));
        view.setClipChildren(false);
        view.setAdapter(adapter);
    }
}
