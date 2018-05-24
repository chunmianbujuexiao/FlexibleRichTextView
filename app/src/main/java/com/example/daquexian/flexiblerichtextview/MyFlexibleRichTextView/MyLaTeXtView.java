package com.example.daquexian.flexiblerichtextview.MyFlexibleRichTextView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;

import org.scilab.forge.jlatexmath.core.AjLatexMath;
import org.scilab.forge.jlatexmath.core.Insets;
import org.scilab.forge.jlatexmath.core.TeXConstants;
import org.scilab.forge.jlatexmath.core.TeXFormula;
import org.scilab.forge.jlatexmath.core.TeXIcon;

import java.util.List;

/**
 * Created by daquexian on 17-2-16.
 */

public class MyLaTeXtView extends android.support.v7.widget.AppCompatTextView {
    public MyLaTeXtView(Context context) {
        super(context);
        this.setTextSize(30);
    }

    public MyLaTeXtView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }


    public void setTextWithFormula(TextWithFormula textWithFormula) {

        List<TextWithFormula.Formula> formulas = textWithFormula.getFormulas();
        final SpannableStringBuilder builder = textWithFormula;

        for (final TextWithFormula.Formula formula : formulas) {
            TeXFormula teXFormula = TeXFormula.getPartialTeXFormula(formula.content);
            System.out.println("得到："+formula.content);
           try {
                Bitmap bitmap = getBitmap(teXFormula);
                if (bitmap.getWidth() > MyFlexibleRichTextView.MAX_IMAGE_WIDTH) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, MyFlexibleRichTextView.MAX_IMAGE_WIDTH,
                            bitmap.getHeight() * MyFlexibleRichTextView.MAX_IMAGE_WIDTH / bitmap.getWidth(),
                            false);
                }

                builder.setSpan(new CenteredImageSpan(getContext(), bitmap),
                        formula.start, formula.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                /* Glide.with(getContext())
                        .load(Constants.FORMULA_SITE + formula.content)
                        .asBitmap()
                        .placeholder(new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                final int HEIGHT_THRESHOLD = 60;
                                // post to avoid ConcurrentModificationException, from https://github.com/bumptech/glide/issues/375
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap newImage;
                                        if (resource.getWidth() > Constants.MAX_IMAGE_WIDTH) {
                                            int newHeight = resource.getHeight() * Constants.MAX_IMAGE_WIDTH / resource.getWidth();
                                            newImage = Bitmap.createScaledBitmap(resource, Constants.MAX_IMAGE_WIDTH, newHeight, true);
                                        } else {
                                            newImage = resource;
                                        }

                                        builder.setSpan(new CenteredImageSpan(getContext(), newImage), formula.start, formula.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                        setText(builder);
                                        // retrieveFormulaOnlineImg(formulaList, view, builder, i + 1, start);
                                    }
                                });
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                if (e != null) e.printStackTrace();
                            }
                        });*/
            }
        }
        setText(builder);
    }

    private Bitmap getBitmap(TeXFormula formula) {
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(getPaint().getTextSize() / getPaint().density)
                .setWidth(TeXConstants.UNIT_SP, getPaint().getTextSize() / getPaint().density, TeXConstants.ALIGN_LEFT)
                .setIsMaxWidth(true)
                .setInterLineSpacing(TeXConstants.UNIT_SP,
                        AjLatexMath.getLeading(getPaint().getTextSize() / getPaint().density))
                .build();
        icon.setInsets(new Insets(5, 5, 5, 5));

        Bitmap image = Bitmap.createBitmap(icon.getIconWidth(), icon.getIconHeight(),
                Bitmap.Config.ARGB_4444);

        Canvas g2 = new Canvas(image);
        g2.drawColor(Color.TRANSPARENT);
        icon.paintIcon(g2, 0, 0);
        return image;
    }
}
