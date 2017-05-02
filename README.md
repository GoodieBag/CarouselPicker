# Carousel Picker

 A Carousel picker library for android which supports both text and icons . :pouting_cat:
 
 <img src="https://raw.githubusercontent.com/GoodieBag/CarouselPicker/master/gif/gif_image_480.gif" height="500"> <img src="https://raw.githubusercontent.com/GoodieBag/CarouselPicker/master/gif/gif_text_480.gif" height="500">
<img src="https://raw.githubusercontent.com/GoodieBag/CarouselPicker/master/gif/gif_mix_480.gif" height="500">
 
## Gradle Dependency

Add this in your root build.gradle file at the end of repositories:
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency : 
```java
dependencies {
	        compile 'com.github.Vatican-Cameos:CarouselPicker:v1.0'
	}
```
Sync the gradle and that's it! :+1:

## Features : 
* Supports icons and text or a mixture of both as items of the picker.
* Gives a nice carousel view of the items.
* Page change listener exists to monitor the current selected item.

## Usage

### XML : 

```xml
<in.goodiebag.carouselpicker.CarouselPicker
        android:id="@+id/carousel"
        android:layout_marginTop="20dp"
        android:layout_marginBottom="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#CCC"
        app:items_visible="three" />
```

```items_visible```  represents the number of pages visible on the screen. Three, five and seven are the available options.


### Java :
Carousel picker supports both images and text or a mixture of both !
```java
CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.carousel);

// Case 1 : To populate the picker with images
List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i1));
imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i2));
imageItems.add(new CarouselPicker.DrawableItem(R.drawable.i3));
//Create an adapter
CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
//Set the adapter
carouselPicker.setAdapter(imageAdapter);

//Case 2 : To populate the picker with text
List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
//20 here represents the textSize in dp, change it to the value you want.
textItems.add(new CarouselPicker.TextItem("hi", 20));
textItems.add(new CarouselPicker.TextItem("hi", 20));
textItems.add(new CarouselPicker.TextItem("hi", 20));
CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
carouselPicker.setAdapter(textAdapter);

//Case 3 : To populate the picker with both images and text
List<CarouselPicker.PickerItem> mixItems = new ArrayList<>();
mixItems.add(new CarouselPicker.DrawableItem(R.drawable.i1));
mixItems.add(new CarouselPicker.TextItem("hi", 20));
mixItems.add(new CarouselPicker.DrawableItem(R.drawable.i2));
mixItems.add(new CarouselPicker.TextItem("hi", 20));
CarouselPicker.CarouselViewAdapter mixAdapter = new CarouselPicker.CarouselViewAdapter(this, mixItems, 0);
carouselPicker.setAdapter(mixAdapter);
```

## Listeners :
```java
carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //position of the selected item
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
```

## LICENSE : 
```
MIT License

Copyright (c) 2017 GoodieBag

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
