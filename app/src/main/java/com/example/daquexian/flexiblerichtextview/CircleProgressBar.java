package com.example.daquexian.flexiblerichtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆环显示正确率
 * 参考：https://blog.csdn.net/TMACADAI/article/details/76223112
 * 参考：https://blog.csdn.net/kongTy/article/details/73732570
 *
 * @author chen
 * @date 2018/5/9
 */

public class CircleProgressBar extends View {
    /**
     * 绘制百分比的圆，一共有四部分，分别是里面的文字、背景圆、默认圆环、开始绘制的圆环；
     * 思路：首先需要四支画笔, 设置画笔对应的属性等；
     */
    private Paint roundColorPaint;//外圆画笔
    private Paint mCirclePaint;//中心园的画笔
    private Paint mTextPaint;//文字的画笔
    private Paint mArcPaint;//外圆环的画笔
    private int mCircleX;//设置圆心x坐标
    private int mCircleY;//设置圆心y坐标
    private float mCurrentAngle;//当前角度
    private RectF mArcRectF;//画中心园的外接矩形，用来画圆环用
    private float mStartSweepValue;//圆环开始角度
    private int mTargetPercent;//设置目标的百分比(也就是后台返回的数据)
    private float mCurrentPercent;//当前百分比

    private int mRadius;//圆的半径
    private int mCircleBackground;//中间圆的背景颜色
    private int mRingColor;//外圆环的颜色  第二次画圆的颜色
    private int roundColor; //外圆环的颜色 第一次画圆的颜色
    private int mTextSize;//字体大小
    private int mTextColor;//字体颜色

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    private boolean isComplete = false;

    public CircleProgressBar(Context context) {
        super(context);
        //初始化数据
        init(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 自定义属性，attr
        // 使用TypedArray
        //自定义属性 values/attr
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PercentageRing);
        //中间圆的背景颜色  默认 - 白色
        mCircleBackground = typedArray.getColor(R.styleable.PercentageRing_circleBackground, 0xff000000);
        //外圆环的颜色  默认 - 灰色
        roundColor = typedArray.getColor(R.styleable.PercentageRing_circleColor, 0xfff5f5f5);
        //外圆环的颜色  动态画圆 - 默认-红色
        mRingColor = typedArray.getColor(R.styleable.PercentageRing_ringColor, 0xfff5f5f5);
        //中间圆的半径 默认为80
        mRadius = typedArray.getInt(R.styleable.PercentageRing_radius, 80);
        //字体颜色 默认 - 灰色
        mTextColor = typedArray.getColor(R.styleable.PercentageRing_textColor, 0xff999999);
        //最后一定要调用这个 释放掉TypedArray
        typedArray.recycle();
        //初始化数据
        init(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context){
        //圆环开始角度 -90° 正北方向
        mStartSweepValue = -90;
        //当前角度
        mCurrentAngle = 0;
        //当前百分比
        mCurrentPercent = 0;
        //设置中心园的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleBackground);
        mCirclePaint.setStyle(Paint.Style.FILL);
        //设置文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth((float) (0.025*mRadius));
        mTextPaint.setTextSize(mRadius/2);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        //设置外圆环的画笔
        mArcPaint = new Paint();
        roundColorPaint = new Paint();
        roundColorPaint.setColor(roundColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mRingColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth((float) (0.075*mRadius));

        //获得文字的字号 因为要设置文字在圆的中心位置
        mTextSize = (int) mTextPaint.getTextSize();
        isComplete = false;


    }
    //主要是测量wrap_content时候的宽和高，因为宽高一样，只需要测量一次宽即可，高等于宽
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(widthMeasureSpec));
        //设置圆心坐标
        mCircleX = getMeasuredWidth()/2;
        mCircleY = getMeasuredHeight()/2;
        //如果半径大于圆心横坐标，需要手动缩小半径的值，否则就画到外面去了
        if (mRadius>mCircleX) {
            //设置半径大小为圆心横坐标到原点的距离
            mRadius = mCircleX;
            mRadius = (int) (mCircleX-0.075*mRadius);
            //因为半径改变了，所以要重新设置一下字体宽度
            mTextPaint.setStrokeWidth((float) (0.025*mRadius));
            //重新设置字号
            mTextPaint.setTextSize(mRadius/2);
            //重新设置外圆环宽度
            mArcPaint.setStrokeWidth((float) (0.075*mRadius));
            //重新获得字号大小
            mTextSize = (int) mTextPaint.getTextSize();
        }
        //画中心园的外接矩形，用来画圆环用
        mArcRectF = new RectF(mCircleX-mRadius, mCircleY-mRadius, mCircleX+mRadius, mCircleY+mRadius);
    }

    //当wrap_content的时候，view的大小根据半径大小改变，但最大不会超过屏幕
    private int measure(int measureSpec){
        int result=0;
        //1、先获取测量模式 和 测量大小
        //2、如果测量模式是MatchParent 或者精确值，则宽为测量的宽
        //3、如果测量模式是WrapContent ，则宽为 直径值 与 测量宽中的较小值；否则当直径大于测量宽时，会绘制到屏幕之外；
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }else {
            result =(int) (1.075*mRadius*2);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;

    }

    //开始画中间圆、文字和外圆环
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias( true );
        paint.setStrokeWidth( 20 );
        paint.setStyle(Paint.Style.STROKE ); // 默认是填充 Paint.Style.FILL
        paint.setColor( Color.parseColor("#0000ff") );

//        Path path = new Path();
//        path.moveTo( 100, 100 ); // 路径path默认是在原点(0,0)，当前移植到(100,100)
//        path.lineTo( 400, 100 );
//        path.lineTo( 700, 200 );
//        paint.setStrokeJoin(Paint.Join.ROUND );
//
//        canvas.drawPath( path, paint );
//
//        paint.setStyle(Paint.Style.STROKE );
//        path.moveTo( 100, 300 ); // 路径path默认是在原点(0,0)，当前移植到(100,300)
//        path.lineTo( 500, 300 );
//        path.lineTo( 600, 500 );
//        paint.setStrokeJoin(Paint.Join.ROUND );
//
//        canvas.drawPath( path, paint );
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.parseColor("#0000ff"));
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(100, 100, 50, mCirclePaint);
//
        Paint roundColorPaint = new Paint();
        roundColorPaint.setStyle(Paint.Style.STROKE);
        //设置外圆环的宽度（默认灰色的圆环）
        roundColorPaint.setColor(Color.parseColor("#00ff00"));
        roundColorPaint.setStrokeWidth(10);
        canvas.drawCircle(100, 100, 50, roundColorPaint);
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth((float) (5));
        mTextPaint.setTextSize(100);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("未完成", 400, 400, mTextPaint);
//        if(mTargetPercent<10){
//            mCurrentAngle = (float) 3.6*10;
//        }else{
//            mCurrentAngle = (float) 3.6*mTargetPercent;
//        }
//        //画中间圆
//        canvas.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint);
//        //画圆环
//        roundColorPaint.setStyle(Paint.Style.STROKE);
//        //设置外圆环的宽度（默认灰色的圆环）
//        roundColorPaint.setStrokeWidth((float) (0.075*mRadius));
//        canvas.drawCircle(mCircleX, mCircleY, mRadius, roundColorPaint);
//        //动态绘制圆环，未完成为红色环，完成为蓝色环
//        if(!isComplete){
//            mArcPaint.setColor(0xffea4745);
//            canvas.drawArc(mArcRectF, mStartSweepValue ,mCurrentAngle, false, mArcPaint);
//        }else {
//            mArcPaint.setColor(0xFF00EE00);
//            canvas.drawArc(mArcRectF, mStartSweepValue ,mCurrentAngle, false, mArcPaint);
//        }
//        //画文字 -  此处可以注销(原因：不能绘制有小数的百分比，例如：1.4%   如果是5%、12%这些可以不用注销下面代码)，自定义布局将获取的值写在Textview上，
//        // 再使用下面的setTargetPercent方法获取数据动态画圆
//
//        if(!isComplete){
//            canvas.drawText("未完成", mCircleX, mCircleY+mTextSize/4, mTextPaint);
//            mTargetPercent = 100;
//        }else {
//            mTextPaint.setTextSize(mTextSize/2);
//            canvas.drawText("正确率", mCircleX, mCircleY-mTextSize/2, mTextPaint);
//            mTextPaint.setTextSize(mTextSize);
//            canvas.drawText(String.valueOf(mTargetPercent)+"%", mCircleX, mCircleY+mTextSize/2, mTextPaint);
//        }
    }
    //设置目标的百分比
    public void setTargetPercent(int percent){
        this.mTargetPercent = percent;
    }

}

