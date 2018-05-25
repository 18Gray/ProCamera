package com.eighteengray.procamera.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.MarkInfo;
import java.util.ArrayList;
import java.util.List;


public class MarkAdapter extends BaseAdapter
{
    private Context context;
    private List<MarkInfo> markList = new ArrayList<MarkInfo>();


    public MarkAdapter(Context c)
    {
        this.context = c;

        markList.add(new MarkInfo(R.mipmap.img_1, R.mipmap.img01));
        markList.add(new MarkInfo(R.mipmap.img_2, R.mipmap.img02));
        markList.add(new MarkInfo(R.mipmap.img_3, R.mipmap.img03));
        markList.add(new MarkInfo(R.mipmap.img_4, R.mipmap.img04));
        markList.add(new MarkInfo(R.mipmap.img_5, R.mipmap.img05));
        markList.add(new MarkInfo(R.mipmap.img_6, R.mipmap.img06));
        markList.add(new MarkInfo(R.mipmap.img_7, R.mipmap.img07));
        markList.add(new MarkInfo(R.mipmap.img_8, R.mipmap.img08));
        markList.add(new MarkInfo(R.mipmap.img_9, R.mipmap.img09));
        markList.add(new MarkInfo(R.mipmap.img_10, R.mipmap.img10));
    }

    @Override
    public int getCount()
    {
        return markList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return markList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MarkInfo markInfo = markList.get(position);
        ImageView imageview = new ImageView(context);
        imageview.setImageResource(markInfo.getMarkAdapterResource());
        imageview.setLayoutParams(new Gallery.LayoutParams(100, 150));
        imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageview;
    }

}
