# GS Rate application
**Gradle**
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```css
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
**Step 2.** Add the dependency
```css
dependencies {
	        implementation 'com.github.nguyenvuong0308:GSrate:1.1.6'
	}
```
**Requirement:**
Application:

    RateInApp.instance.registerActivityLifecycle(this)

Call Dialog Rate: 

    RateInApp.instance.showDialogRateAndFeedback()



**Flow**

***UseCase 1: Khi không có mạng***
Sẽ ko show dialog, nếu vẫn muốn show thì set forceShow = true (dùng khi ấn rate ở màn setting)

***UseCase 2: Khi có mạng***
1.Show dialog rate (callback **onShowDialogRate**), sau đó call luôn **alwaysIgnore**()
2. Nếu user chọn 5 sao và click gửi thì sẽ gọi call back **onRate**(star), Nếu muốn bật inappReview thì set **inAppReview** = true
2.1. Nếu user chọn **1-4 sao** và click gửi thì sẽ gọi call back **onRate**(star) và chuyển đến màn **feedback**, sau khi ấn gửi ở màn feedback thì callback **onShowThanks** sẽ được gọi, nếu muốn tự xử lý thì có thể implement lại callBack **onShowThanks** và trả về true, nếu trả về false thì mặc định sẽ show dialog thanks có sẵn.
2.2. Nếu user ấn back không rate thì call back **onIgnoreRate**() sẽ được gọi

**Chặn quảng cáo sau khi feedback**, check thêm điều kiện **isCanShowAppOpen** trước  khi show resume app

    RateInApp.instance.isCanShowAppOpen

**Edit email feedback**

    <string name="fb_email_feedback" translatable="false">kunkunnapps@gmail.com</string>


# Custom UI

**Create file config_rate.xml**

 1. Common:
Màu nút submit của các màn 
> fb_color_button_submit_enable
> fb_color_button_submit_disable

Radius của nút submit, để 99dp nếu muốn bo tròn

> fb_radius_submit_size
> 
Text hiện tại đang để sp, ai dùng dp thì overide lại: 

	    <dimen name="fb_text_size_feedback_title">25dp</dimen>
        <dimen name="fb_text_size_feedback_matter">15dp</dimen>
        <dimen name="fb_text_size_button_send">20dp</dimen>
        <dimen name="fb_text_size_title_dialog_thanks">20dp</dimen>
        <dimen name="fb_text_size_title_dialog_feedback">20dp</dimen>
        <dimen name="fb_text_size_body_dialog_thanks">16dp</dimen>

**Dialog Rate**
 1. Màu của nút đánh giá

> fb_color_button_submit_enable 
> fb_color_button_submit_disable

2. Đổi icon cho các sao đánh giá

> fb_ic_smile_1, fb_ic_smile_2 ....

3. Đổi ảnh tay chỉ lên sao

> fb_ic_hand

4. Đổi màu text hạng tốt nhất chúng tôi có thể ...

> fb_text_color_best_rate

**Feedback screen**

1. Icon Back

> fb_ic_back_feedback
2. Icon Header
> 
> fb_ic_feedback_screen

Custom lại style cho icon nếu icon dài:  

    <style name="LogoFeedback">   
	    <item name="android:layout_height">wrap_content</item>
        <item name="android:layout_width">match_parent</item>
        <item name="android:adjustViewBounds">true</item>
        <item name="android:layout_gravity">center_horizontal</item>
        <item name="android:layout_marginTop">-32dp</item>
        <item name="android:layout_marginEnd">10dp</item> 
        <item name="android:layout_marginStart">10dp</item> 
        <item name="android:src">@drawable/ic_logofeedback</item>   
    </style>
    
3. List các lý do
- Nếu list dạng 1 thì để mặc định nếu dạng 2 (chipgroup) thì set fb_matter_feedback_flex_ui = true
- set radius cho nút

> fb_button_feedback_matter_radius
- set màu background cho nút

> fb_feedback_matter_selected 
> fb_feedback_matter_unselected

- set màu text cho nút

> fb_text_feedback_matter_unselected
>  fb_text_feedback_matter_selected

- set background input

> fb_bg_input_feedback

- set radius cho tất cả nút submit

> fb_radius_submit_size

- nếu muốn nút submit nằm trong background của input text

> fb_button_submit_feedback_inside_input = true

- Show text *loại* ở màn feedback

> fb_show_text_type_in_feedback = true

4. Padding bottom feedback screen
> fb_padding_bottom_feedback

**Dialog Thanks**
- icon nút thanks, có thể custom lại imageView bằng style LogoThanks

> fb_ic_feedback_thank

