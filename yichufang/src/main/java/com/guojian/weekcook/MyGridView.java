package com.guojian.weekcook;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by guojian on 12/8/16.
 *
 * 1. 使用ThinkAndroid，Xutils框架，使用httpUtils进行断点下载,使用BitmapUtils进行异步图片加载，使用Xutils中的PauseOnScrollListener实现listview快速滑动时不加载图片，使用Asmack聊天框架进行收发消息，实现服务器端主动推送信息给客户端。
 2. 自定义Viewgroup, 自定义ListView下拉刷新。
 3. 使用了webView加载能自适应手机屏幕的网页, 通过自定义标签实现在网页中打电话，调用android代码。
 4. 发送图片时用Base64将byte[ ]转成字符串，收到后再用Base64进行解码。发语音时用MediaRecorder录音，存到sdcard上再用MediaPlayer进行播放。
 5. 登录时没有直接发送密码，发送的是密码的md5串。
 6. 地图开发使用百度地图开发sdk,通过LocationClient进行地图定位。
 7. 在自定义的Adapter中用到了ViewHolder。全部使用相对布局。能自适应不同设备,用到了ListView,ScrollView，popupWindow,Dialog, mapView等控件。为了适应不同屏幕字号定义在dimens.xml中。
 8. 使用draw9patch.bat制作.9图片。为每个button使用了selector.使用了style,theme。


 1. 本项目采用了MVC设计模式，模块职责划分明确，使代码有很好的可扩展性和维护性。
 2. 采用阿里云市场数据API，使用OkHttp进行网络请求，使用Android-Universal-Image-Loader开源框架加载网络图片。
 3. 使用开源框架CircleImageView实现圆形头像，使用shape自定义了控件的圆角边框。
 4. 自定义ViewPager,ListView,GridView,ScrollView等, 使用开源的GridViewWithHeaderAndFooter为GridView增加页头和页脚。
 5. 自定义工具ScreenShotUtils实现详情页的长截图功能，使用自定义的DataCleanManager实现清理缓存的功能。
 6. 使用SQLiteDatabase存储和删除数据的等，实现了收藏菜谱的功能。
 7. 使用ShareSDK实现分享到社交平台的功能，并自定义分享菜单。


 这是一款运行在Android系统的美食、菜谱类软件。实现了每日推荐，时令流行，分类美食，热门搜索，收藏，分享，个人资料等功能。


 */

/*
*
 1. 有一年以上Android项目开发经验，熟悉Android应用程序开发流程，熟悉Linux操作系统，有扎实的Java编程基础。
 2. 熟悉Android SDK，精通Android的系统控件及自定义控件的使用，精通UI界面编程。
 3. 熟练Android四大组件，熟悉Socket和Http等网络编程，熟悉线程、XML和JSON解析，SQLite数据库等操作。
 4. 熟悉Android常用的开源框架，熟悉SVN/Git等版本控制软件。
 5. 热爱编程，热爱于移动互联网，有良好的团队精神和自学能力。
 6. GitHub:  https://github.com/jkloshhm

* */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
