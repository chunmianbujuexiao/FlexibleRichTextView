package com.example.daquexian.flexiblerichtextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.daquexian.flexiblerichtextview.MyFlexibleRichTextView.MyFlexibleRichTextView;

import org.scilab.forge.jlatexmath.core.AjLatexMath;

import io.github.kbiakov.codeview.classifier.CodeProcessor;

//https://www.cnblogs.com/ScorchingSun/p/3627330.html
public class MainActivity extends AppCompatActivity {
    private Button movebtn;//可拖动按钮
    private boolean clickormove = true;//点击或拖动，点击为true，拖动为false
    private int downX, downY;//按下时的X，Y坐标
    private boolean hasMeasured = false;//ViewTree是否已被测量过，是为true，否为false
    private View content;//界面的ViewTree
    private int screenWidth, screenHeight;//ViewTree的宽和高
    private ScrollView s1;
    private ScrollView s2;
    private LinearLayout l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s1 = (ScrollView) findViewById(R.id.s1);
        s2 = (ScrollView) findViewById(R.id.s2);

//        l2 = (LinearLayout) findViewById(R.id.line1);
        TextView t1 = (TextView) findViewById(R.id.t1);
        TextView t2 = (TextView) findViewById(R.id.t2);
        t1.setText("ScrollView绝对是各种教材、教程都或多或少遗漏的一个非常重要的视图。凡是这个界面的组成非常不规则，而且竖直方向长度不够就肯定需要使用Scrollview了。因为ListView处理的是规则的内容。至于带视差效果的滚动自然是ScrollView的产物。本文会通过一个简单的例子，讲述如何使用Scrollview。\n" +
                "\n" +
                "多数的Android应用都会出现内容尺寸超出屏幕的情况。比如一则新闻页，有配图，在配图下可以点击按钮了解更多，有标题，最后是全部的新闻内容，假设这则内容是勇士打败骑士队后詹姆斯又跑去哪里抱大腿的新闻。那么ListView显然不是最好的选择，但是一般的Layout，比如LinearLayout、RelativeLayout或者FrameLayout之类Layout也没法用。最后就只有ScrollView可以解决问题了。\n" +
                "\n" +
                "ScrollView就是这么一种特殊的布局。当ScrollView的内容大于他本身的size的时候，ScrollView会自动添加滚动条，并可以竖直滑动。\n" +
                "\n" +
                "ScrollView的直接子View只能有一个。也就是\n" +
                "\n" +
                "作者：uncle_charlie\n" +
                "链接：https://www.jianshu.com/p/ddd295a06d17\n" +
                "來源：简书\n" +
                "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。" + "ScrollView绝对是各种教材、教程都或多或少遗漏的一个非常重要的视图。凡是这个界面的组成非常不规则，而且竖直方向长度不够就肯定需要使用Scrollview了。因为ListView处理的是规则的内容。至于带视差效果的滚动自然是ScrollView的产物。本文会通过一个简单的例子，讲述如何使用Scrollview。\n" +
                "\n" +
                "多数的Android应用都会出现内容尺寸超出屏幕的情况。比如一则新闻页，有配图，在配图下可以点击按钮了解更多，有标题，最后是全部的新闻内容，假设这则内容是勇士打败骑士队后詹姆斯又跑去哪里抱大腿的新闻。那么ListView显然不是最好的选择，但是一般的Layout，比如LinearLayout、RelativeLayout或者FrameLayout之类Layout也没法用。最后就只有ScrollView可以解决问题了。\n" +
                "\n" +
                "ScrollView就是这么一种特殊的布局。当ScrollView的内容大于他本身的size的时候，ScrollView会自动添加滚动条，并可以竖直滑动。\n" +
                "\n" +
                "ScrollView的直接子View只能有一个。也就是\n" +
                "\n" +
                "作者：uncle_charlie\n" +
                "链接：https://www.jianshu.com/p/ddd295a06d17\n" +
                "來源：简书\n" +
                "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");
        t2.setText("ScrollView绝对是各种教材、教程都或多或少遗漏的一个非常重要的视图。凡是这个界面的组成非常不规则，而且竖直方向长度不够就肯定需要使用Scrollview了。因为ListView处理的是规则的内容。至于带视差效果的滚动自然是ScrollView的产物。本文会通过一个简单的例子，讲述如何使用Scrollview。\n" +
                "\n" +
                "多数的Android应用都会出现内容尺寸超出屏幕的情况。比如一则新闻页，有配图，在配图下可以点击按钮了解更多，有标题，最后是全部的新闻内容，假设这则内容是勇士打败骑士队后詹姆斯又跑去哪里抱大腿的新闻。那么ListView显然不是最好的选择，但是一般的Layout，比如LinearLayout、RelativeLayout或者FrameLayout之类Layout也没法用。最后就只有ScrollView可以解决问题了。\n" +
                "\n" +
                "ScrollView就是这么一种特殊的布局。当ScrollView的内容大于他本身的size的时候，ScrollView会自动添加滚动条，并可以竖直滑动。\n" +
                "\n" +
                "ScrollView的直接子View只能有一个。也就是\n" +
                "\n" +
                "作者：uncle_charlie\n" +
                "链接：https://www.jianshu.com/p/ddd295a06d17\n" +
                "來源：简书\n" +
                "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");


        content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);//获取界面的ViewTree根节点View

        DisplayMetrics dm = getResources().getDisplayMetrics();//获取显示屏属性
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        ViewTreeObserver vto = content.getViewTreeObserver();//获取ViewTree的监听器
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    screenHeight = content.getMeasuredHeight();//获取ViewTree的高度
                    hasMeasured = true;//设置为true，使其不再被测量。
                }
                return true;//如果返回false，界面将为空。
            }

        });
        movebtn = (Button) findViewById(R.id.movebtn);
        movebtn.setText("haha");
        movebtn.setOnTouchListener(new View.OnTouchListener() {//设置按钮被触摸的时间

            int lastX, lastY; // 记录移动的最后的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();//获取事件类型
                switch (ea) {
                    case MotionEvent.ACTION_DOWN: // 按下事件
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        System.out.println("last:" + lastX + ":" + lastY);
                        downX = lastX;
                        downY = lastY;
                        break;
                    case MotionEvent.ACTION_MOVE: // 拖动事件
                        // 移动中动态设置位置
                        System.out.println((int) event.getRawY());
                        int dy = (int) event.getRawY() - lastY;//位移量Y
                        int left = v.getLeft();
                        int top = v.getTop() + dy;
                        int right = v.getRight();
                        int bottom = v.getBottom() + dy;
                        System.out.println(left + ":" + top + ":" + right + ":" + bottom);
                        int s1left = s1.getLeft();
                        int s1right = s1.getRight();
                        int s1top = s1.getTop();
                        int s1bottom = s1.getBottom() + dy;

                        int s2left = s2.getLeft();
                        int s2right = s2.getRight();
                        int s2top = s2.getTop() + dy;
                        int s2bottom = s2.getBottom();
//
                        //++限定按钮被拖动的范围
                        if (top < 100) {
                            top = 100;
                            s2top = 100 + v.getHeight();
                            s1bottom = 100;
                            bottom = top + v.getHeight();

                        }
                        if (bottom > screenHeight - 100) {
                            bottom = screenHeight - 100;
                            top = bottom - v.getHeight();
                            s2top = bottom;
                            s1bottom = top;
                        }
//
                        //--限定按钮被拖动的范围
                        v.layout(left, top, right, bottom);//按钮重画
                        s1.layout(s1left, s1top, s1right, s1bottom);
                        s2.layout(s2left, s2top, s2right, s2bottom);
                        // 记录当前的位置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;


                }
                return false;

            }


        });
    }


}