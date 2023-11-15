package `in`.goodiebag.example

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.graphics.Typeface
import android.os.Build
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.goodiebag.carouselpicker.CarouselPicker
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class MainActivity : AppCompatActivity() {

    private lateinit var tvSelected: TextView
    private lateinit var mixCarousel: CarouselPicker
    private lateinit var imageCarousel: CarouselPicker
    private lateinit var textCarousel: CarouselPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageCarousel = findViewById(R.id.imageCarousel)
        textCarousel = findViewById(R.id.textCarousel)
        mixCarousel = findViewById(R.id.mixCarousel)
        tvSelected = findViewById(R.id.tvSelectedItem)

        val imageItems: MutableList<CarouselPicker.PickerItem> = ArrayList()
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.i1))
        imageItems.add(
            CarouselPicker.DrawableItem(
                R.drawable.i2,
                getResources().getColor(android.R.color.holo_red_light)
            )
        )

        imageItems.add(CarouselPicker.DrawableItem(R.drawable.i3))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.i4))

        val imageAdapter = CarouselPicker.CarouselViewAdapter(this, imageItems, 0)
        imageCarousel.setAdapter(imageAdapter)
        val textItems: MutableList<CarouselPicker.PickerItem> = ArrayList()
        textItems.add(
            CarouselPicker.TextItem(
                "hi", 20,
                getResources().getColor(android.R.color.holo_blue_bright), Typeface.MONOSPACE,
                CarouselPicker.TextItem.FontStyle.BOLD_ITALIC
            )
        )
        textItems.add(
            CarouselPicker.TextItem(
                "hi, welcome to the carousel picker", 20,
                getResources().getColor(android.R.color.holo_red_light),
                Typeface.SANS_SERIF, CarouselPicker.TextItem.FontStyle.NORMAL
            )
        )
        textItems.add(
            CarouselPicker.TextItem(
                "hi", 20,
                Typeface.SERIF, CarouselPicker.TextItem.FontStyle.ITALIC
            )
        )
        textItems.add(CarouselPicker.TextItem("hi", 20))
        val textAdapter = CarouselPicker.CarouselViewAdapter(this, textItems, 0)
        textCarousel.setAdapter(textAdapter)
        // set default color
        textAdapter.textColor = getResources().getColor(android.R.color.holo_green_light)
        textAdapter.selectedItemTextColor = Color.GREEN
        textAdapter.selectedIndex = 1

        val mixItems: MutableList<CarouselPicker.PickerItem> = ArrayList()
        mixItems.add(CarouselPicker.DrawableItem(R.drawable.i1))
        mixItems.add(CarouselPicker.TextItem("hi", 20))
        mixItems.add(CarouselPicker.DrawableItem(R.drawable.i2))
        mixItems.add(CarouselPicker.TextItem("hi", 20))
        val mixAdapter = CarouselPicker.CarouselViewAdapter(this, mixItems, 0)
        mixAdapter.textColor = getResources().getColor(R.color.colorPrimary)
        mixAdapter.selectedItemTextColor = getResources().getColor(R.color.colorAccent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mixAdapter.selectedItemTintColor = getResources().getColor(R.color.colorAccent)
        }
        mixCarousel.setAdapter(mixAdapter)
        imageCarousel.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                tvSelected.setText("Selected item in image carousel is  : $position")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        textCarousel.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                tvSelected.setText("Selected item in text carousel is  : $position")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mixCarousel.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                tvSelected.setText("Selected item in mix carousel is  : $position")
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        findViewById<Button>(R.id.btJump).setOnClickListener {
            mixAdapter.selectedIndex = 2
        }
    }
}
