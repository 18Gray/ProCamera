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

        markList.add(new MarkInfo(R.drawable.img_1, R.drawable.img01));
        markList.add(new MarkInfo(R.drawable.img_2, R.drawable.img02));
        markList.add(new MarkInfo(R.drawable.img_3, R.drawable.img03));
        markList.add(new MarkInfo(R.drawable.img_4, R.drawable.img04));
        markList.add(new MarkInfo(R.drawable.img_5, R.drawable.img05));
        markList.add(new MarkInfo(R.drawable.img_6, R.drawable.img06));
        markList.add(new MarkInfo(R.drawable.img_7, R.drawable.img07));
        markList.add(new MarkInfo(R.drawable.img_8, R.drawable.img08));
        markList.add(new MarkInfo(R.drawable.img_9, R.drawable.img09));
        markList.add(new MarkInfo(R.drawable.img_10, R.drawable.img10));
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
