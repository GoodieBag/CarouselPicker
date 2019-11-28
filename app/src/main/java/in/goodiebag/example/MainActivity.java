package in.goodiebag.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class MainActivity extends AppCompatActivity {
    CarouselPicker imageCarousel, textCarousel, mixCarousel;
    TextView tvSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageCarousel = findViewById(R.id.imageCarousel);
        textCarousel = findViewById(R.id.textCarousel);
        mixCarousel = findViewById(R.id.mixCarousel);
        tvSelected = findViewById(R.id.tvSelectedItem);

        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i1));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i2));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i3));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i4));
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
        imageCarousel.setAdapter(imageAdapter);

        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("hi", 20));
        textItems.add(new CarouselPicker.TextItem("hi", 20));
        textItems.add(new CarouselPicker.TextItem("hi", 20));
        textItems.add(new CarouselPicker.TextItem("hi", 20));
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
        textCarousel.setAdapter(textAdapter);

        List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.i1));
        mixItems.add(new CarouselPicker.TextItem("hi", 20));
        mixItems.add(new CarouselPicker.DrawableItem(R.drawable.i2));
        mixItems.add(new CarouselPicker.TextItem("hi", 20));

        CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
        mixAdapter.setTextColor(getResources().getColor(R.color.colorPrimary));
        mixCarousel.setAdapter(mixAdapter);

        imageCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSelected.setText("Selected item in image carousel is  : "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        textCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSelected.setText("Selected item in text carousel is  : "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mixCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSelected.setText("Selected item in mix carousel is  : "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
