package com.example.daquexian.flexiblerichtextview.MyFlexibleRichTextView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by jianhao on 16-8-26.
 */
public class MyFlexibleRichTextView extends LinearLayout {
    static final int MAX_IMAGE_WIDTH = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);

    private Context mContext;
    private OnViewClickListener mOnViewClickListener;

    private List<Tokenizer.TOKEN> mTokenList;
    private int mTokenIndex;

    private boolean mCenter;


    public MyFlexibleRichTextView(Context context) {
        this(context, null);

    }

    public MyFlexibleRichTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MyFlexibleRichTextView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context);
    }

    public void setToken(List<Tokenizer.TOKEN> tokens) {
        removeAllViews();//移除所有views
        mTokenList = tokens;//设置token


        resetTokenIndex();//重置index
        List<Object> result = until(Tokenizer.END.class);//解析出来的结果
        System.out.println(result);
        System.out.println(result.size());
        if (result == null) {
            return;
        }

        for (final Object o : result) {//根据结果类型修改
            if (o instanceof TextWithFormula) {
                final TextWithFormula textWithFormula = (TextWithFormula) o;

                final MyLaTeXtView textView = new MyLaTeXtView(mContext);

                textView.setTextWithFormula(textWithFormula);

                textView.setMovementMethod(LinkMovementMethod.getInstance());
                myAddView(textView);
            }
//            else if (o instanceof ImageView) {
//                myAddView((ImageView) o);
//            } else if (o instanceof HorizontalScrollView) {
//                myAddView((HorizontalScrollView) o);
//            } else if (o instanceof QuoteView) {
//                myAddView((QuoteView) o);
//            }
        }
    }


    public void setText(String text) {
        text = text.replaceAll("\u00AD", "");
        mTokenList = com.example.daquexian.flexiblerichtextview.MyFlexibleRichTextView.Tokenizer.tokenizer(text);
        System.out.println(mTokenList);
        setToken(mTokenList);
    }

    private void myAddView(View view) {
        if (view instanceof FImageView && ((FImageView) view).centered) {
            // TODO: 17-2-13 any more efficient way?
            RelativeLayout rl = new RelativeLayout(mContext);
            RelativeLayout.LayoutParams rlLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rlLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            rl.addView(view);
            rl.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(rl);
        } else {
            addView(view);
        }
    }

    private void resetTokenIndex() {
        mTokenIndex = 0;
    }

//    private final Class[] start = {CENTER_START.class, BOLD_START.class, ITALIC_START.class,
//            UNDERLINE_START.class, DELETE_START.class, CURTAIN_START.class, TITLE_START.class,
//            COLOR_START.class, URL_START.class};
//
//    private final Class[] end = {CENTER_END.class, BOLD_END.class, ITALIC_END.class,
//            UNDERLINE_END.class, DELETE_END.class, CURTAIN_END.class, TITLE_END.class,
//            COLOR_END.class, URL_END.class};

    private final Class[] start = {Tokenizer.UNDERLINE_START.class,Tokenizer.BOLD_START.class, Tokenizer.ITALIC_START.class};

    private final Class[] end = {Tokenizer.UNDERLINE_END.class, Tokenizer.BOLD_END.class, Tokenizer.ITALIC_END.class};

    private final String UNDERLINE_OP = "underline";
    private final String BOLD_OP = "bold";
    private final String ITALIC_OP = "italic";
    private final String LATEX_OP = "latex";

    private final String[] operation = {UNDERLINE_OP,BOLD_OP, ITALIC_OP};

    private <T extends Tokenizer.TOKEN> List<Object> until(Class<T> endClass) {
        List<Object> result = new ArrayList<>();

        while (!(thisToken() instanceof Tokenizer.END) && !(endClass.isInstance(thisToken()))) {//如果不是本标签的结束标签和，不是结束标签
            boolean flag = false;
            int tmp;
            System.out.println(thisToken().getClass());
            for (Class anEnd : end) {//查看其它标签的结束标签
                if (anEnd.isInstance(thisToken())) {
                    appendTextWithFormula(result, new TextWithFormula(thisToken().value));//设置结果相加
                    System.out.println("结束："+thisToken().value);

                    flag = true;
                    break;
                }
            }

            for (int i = 0; i < start.length; i++) {
                if (start[i].isInstance(thisToken())) {
                    String operand = "";
//                    if (thisToken() instanceof CENTER_START) {
//                        mCenter = true;
//                    } else if (thisToken() instanceof COLOR_START) {
//                        operand = ((COLOR_START) thisToken()).color;
//                    } else if (thisToken() instanceof URL_START) {
//                        operand = ((URL_START) thisToken()).url;
//                    }

                    tmp = getTokenIndex();//获取当前的index
                    next();//进入下一个index
                    List<Object> shown = until(end[i]);
                    mCenter = false;
                    if (shown != null) {//第二次返回[hi!]为plain,并为其写入当前格式[b]
                        concat(result, operate(shown, operation[i], operand));//合并带格式的字符串
                    } else {
                        setTokenIndex(tmp);
                        appendTextWithFormula(result, new TextWithFormula(thisToken().value));
                    }
                    flag = true;
                }
            }

            if (!flag) {
                if (thisToken() instanceof Tokenizer.PLAIN) {
                    appendTextWithFormula(result, new TextWithFormula(thisToken().value));

                }
                else if (thisToken() instanceof Tokenizer.FORMULA) {
                    System.out.println("格式");
                    Tokenizer.FORMULA thisToken = (Tokenizer.FORMULA) thisToken();
                    System.out.println(thisToken.content);
                    TextWithFormula textWithFormula = new TextWithFormula(thisToken().value);

                    textWithFormula.addFormula(0, thisToken.value.length(),
                            thisToken.content, thisToken.contentStart,
                            thisToken.contentStart + thisToken.content.length());
                    System.out.println("结果"+textWithFormula.getFormulas().get(0).content);
                    appendTextWithFormula(result, textWithFormula);
                }
//                      else if (thisToken() instanceof IMAGE) {
//
//                    IMAGE thisToken = (IMAGE) thisToken();
//                    FImageView imageView = loadImage(thisToken.url, thisToken.width, thisToken.height);
//                    if (mCenter) {
//                        imageView.centered = true;
//                    }
//                    appendView(result, imageView);
//
//                }
            }
            next();//第一次到达TITLE_END
            System.out.println("index:"+getTokenIndex());
            System.out.println(endClass);
        }

        if (endClass.isInstance(thisToken())) {
            return result;
        }

        return null;
    }

    private void appendTextWithFormula(List<Object> viewList, Object newTextWithForluma) {
        concat(viewList, Collections.singletonList(newTextWithForluma));
    }

    private void appendView(List<Object> viewList, Object newView) {
        concat(viewList, Collections.singletonList(newView));
    }

    private FImageView loadImage(String url) {
        return loadImage(url, -1);
    }

    private FImageView loadImage(String url, int size) {
        return loadImage(url, size, size);
    }

    private FImageView loadImage(String url, int width, int height) {
        final FImageView imageView = new FImageView(mContext);

        ViewGroup.LayoutParams layoutParams;

        int phWidth, phHeight, imgWidth, imgHeight;

        if (height != -1 && width != -1) {
            imgHeight = height;
            imgWidth = width;
            phHeight = height;
            phWidth = width;
        } else if (width != -1) {
            imgHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            imgWidth = width;
            phHeight = MAX_IMAGE_WIDTH / 2;
            phWidth = width;
        } else if (height != -1) {
            imgHeight = height;
            imgWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
            phHeight = height;
            phWidth = MAX_IMAGE_WIDTH;
        } else {
            imgHeight = imgWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
            phHeight = MAX_IMAGE_WIDTH / 2;
            phWidth = MAX_IMAGE_WIDTH;
        }

        if (imageView.centered) {
            layoutParams = new RelativeLayout.LayoutParams(phWidth, phHeight);
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        } else {
            layoutParams = new LinearLayout.LayoutParams(phWidth, phHeight);
        }
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(0, 0, 0, 10);

        final int finalWidth = imgWidth;
        final int finalHeight = imgHeight;
        Glide.with(mContext)
                .load(url)
                .placeholder(new ColorDrawable(ContextCompat.getColor(mContext, android.R.color.darker_gray)))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        /**
                         * adjust the size of ImageView according to image
                         */
                        if (imageView.centered) {
                            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                            imageView.setLayoutParams(params);
                        } else {
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(finalWidth, finalHeight));
                        }

                        imageView.setImageDrawable(resource);

                        imageView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mOnViewClickListener != null) {
                                    mOnViewClickListener.onImgClick(imageView);
                                }
                            }
                        });
                        return false;
                    }
                })
                .into(imageView);
        return imageView;
    }

    private List<Object> operate(List<Object> viewList, String sytleType, final String... operand) {
        switch (sytleType) {
            case BOLD_OP:
                for (Object o : viewList) {
                    if (o instanceof TextWithFormula) {
                        final TextWithFormula textWithFormula = (TextWithFormula) o;
                        textWithFormula.setSpan(new StyleSpan(Typeface.BOLD), 0, textWithFormula.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
                break;

            case ITALIC_OP:
                for (Object o : viewList) {
                    if (o instanceof TextWithFormula) {
                        final TextWithFormula textWithFormula = (TextWithFormula) o;
                        textWithFormula.setSpan(new StyleSpan(Typeface.ITALIC), 0, textWithFormula.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case UNDERLINE_OP:
                for (Object o : viewList) {
                    if (o instanceof TextWithFormula) {
                        final TextWithFormula textWithFormula = (TextWithFormula) o;
                        textWithFormula.setSpan(new UnderlineSpan(), 0, textWithFormula.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
                break;
            case LATEX_OP:
//                for (Object o : viewList) {
//                    if (o instanceof TextWithFormula) {
//                        final TextWithFormula textWithFormula = (TextWithFormula) o;
//                        textWithFormula.setSpan(new UnderlineSpan(), 0, textWithFormula.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    }
//                }
//                System.out.println("格式");
//                Tokenizer.FORMULA thisToken = (Tokenizer.FORMULA) thisToken();
//                System.out.println(thisToken.content);
//                TextWithFormula textWithFormula = new TextWithFormula(thisToken().value);
//
//                textWithFormula.addFormula(0, thisToken.value.length(),
//                        thisToken.content, thisToken.contentStart,
//                        thisToken.contentStart + thisToken.content.length());
//
//                appendTextWithFormula(viewList, textWithFormula);
                break;

        }
        return viewList;
    }

    private List<Object> operate(List<Object> list, String operation) {
        return operate(list, operation, "");
    }

    private <T> void concat(List<Object> viewList, List<T> newTextOrView) {
        if (viewList.size() == 0) {
            viewList.addAll(newTextOrView);
        } else {
            if (newTextOrView.size() > 0) {
                if (viewList.get(viewList.size() - 1) instanceof TextWithFormula &&
                        newTextOrView.get(0) instanceof TextWithFormula) {

                    TextWithFormula a = (TextWithFormula) viewList.get(viewList.size() - 1);
                    TextWithFormula b = (TextWithFormula) newTextOrView.get(0);
                    for (TextWithFormula.Formula formula : b.getFormulas()) {//带latex格式的字符串
                        formula.start += a.length();
                        formula.end += a.length();
                        formula.contentStart += a.length();
                        formula.contentEnd += a.length();
                    }
                    a.getFormulas().addAll(b.getFormulas());
                    a.append(b);

                    viewList.addAll(newTextOrView.subList(1, newTextOrView.size()));
                } else {
                    viewList.addAll(newTextOrView);
                }
            }
        }
    }

    private Tokenizer.TOKEN thisToken() {
        return mTokenList.get(mTokenIndex);
    }

    private void next() {
        mTokenIndex++;
    }

    public int getTokenIndex() {
        return mTokenIndex;
    }

    public void setTokenIndex(int tokenIndex) {
        this.mTokenIndex = tokenIndex;
    }

    private List<String> format(List<String> strings) {
        for (int i = strings.size() - 1; i >= 0; i--) {
            String str = strings.get(i);
            if (TextUtils.isEmpty(str) || str.equals("\n")) {
                strings.remove(i);
            }
        }

        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, strings.get(i).trim());
        }

        return strings;
    }

    public static MyFlexibleRichTextView newInstance(Context context, String string) {

        MyFlexibleRichTextView flexibleRichTextView = new MyFlexibleRichTextView(context);

        if (!TextUtils.isEmpty(string)) {
            flexibleRichTextView.setText(string);
        }

        return flexibleRichTextView;
    }

    private View getHorizontalDivider() {
        View horizontalDivider = new View(mContext);
        horizontalDivider.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        horizontalDivider.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.black));

        return horizontalDivider;
    }

    private View getVerticalDivider() {
        View verticalDivider = new View(mContext);
        verticalDivider.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        verticalDivider.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.black));

        return verticalDivider;
    }

    private void init(Context context) {
        init(context, null);
    }

    private void init(Context context, OnViewClickListener onViewClickListener) {
        init(context, onViewClickListener, true);
    }

    private void init(Context context, OnViewClickListener onViewClickListener, boolean showRemainingAtt) {
        setOrientation(VERTICAL);
        mOnViewClickListener = onViewClickListener;
        mContext = context;
        removeAllViews();
    }




    public interface OnViewClickListener {
        void onImgClick(ImageView imageView);
        void onQuoteButtonClick(View view, boolean collapsed);
    }
}