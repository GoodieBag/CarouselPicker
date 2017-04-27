package in.goodiebag.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import in.goodiebag.example.carouselview.R;

public class MainActivity extends AppCompatActivity {
    CarouselPicker carouselPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carouselPicker = (CarouselPicker) findViewById(R.id.view);
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
        CarouselPicker.CarouselViewAdapter adapter = new CarouselPicker.CarouselViewAdapter(this, items, 0);
        carouselPicker.setAdapter(adapter);

    }
}
