package com.eighteengray.procamera.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.eighteengray.procamera.R;



public class TextViewUtils
{

    private void htmlTextView(TextView textView){
        textView.setText(Html.fromHtml("北京市发布霾黄色预警，<font color='#ff0000'><big>外出携带好</big></font>口罩"));
        textView.setText(Html.fromHtml("北京市发布霾黄色预警，<h3><font color='#ff0000'>外出携带好</font></h3>口罩"));
    }

    private void apannableTextView(TextView textView, Context context){
        SpannableStringBuilder spanStringBuilder = new SpannableStringBuilder("中间部分字体变色部分有背景色");
        spanStringBuilder.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.primary_text)),
                0,20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStringBuilder.setSpan(new ForegroundColorSpan(Color.RED),7,9,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringBuilder.setSpan(new AbsoluteSizeSpan(58), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanStringBuilder);
    }

    private void imageTextHtml(final Context context, TextView textView){
        Html.ImageGetter imageGetter = new Html.ImageGetter()
        {
            @Override
            public Drawable getDrawable(String source)
            {
                int id = Integer.parseInt(source);
                //根据id从资源文件中获取图片对象
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
                return d;
            }
        };
        textView.append(Html.fromHtml("图片前文字  <img src=\""+R.drawable.albumset_selected+"\">图片后面文字", imageGetter, null) );

    }

}
