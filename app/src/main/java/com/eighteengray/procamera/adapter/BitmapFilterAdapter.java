package com.eighteengray.procamera.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eighteengray.imageprocesslibrary.bitmapfilter.GrayBitmapFilter;
import com.eighteengray.imageprocesslibrary.bitmapfilter.ReverseBitmapFilter;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.FilterInfo;
import java.util.ArrayList;
import java.util.List;



public class BitmapFilterAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private int selectItem;
    private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();


    public BitmapFilterAdapter(Context c)
    {
        context = c;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        filterArray.add(new FilterInfo(R.drawable.filter_original, new GrayBitmapFilter(), "原图"));
        filterArray.add(new FilterInfo(R.drawable.filter_meibai, new ReverseBitmapFilter(), "美白"));
        filterArray.add(new FilterInfo(R.drawable.filter_feather, new ReverseBitmapFilter(), "淡雅"));
        filterArray.add(new FilterInfo(R.drawable.filter_baohe, new GrayBitmapFilter(), "哥特"));
        filterArray.add(new FilterInfo(R.drawable.filter_light, new ReverseBitmapFilter(), "美食"));
        filterArray.add(new FilterInfo(R.drawable.filter_lomo, new ReverseBitmapFilter(), "LOMO"));
        filterArray.add(new FilterInfo(R.drawable.filter_blackwhite, new GrayBitmapFilter(), "黑白"));
        filterArray.add(new FilterInfo(R.drawable.filter_sharp, new ReverseBitmapFilter(), "锐色"));
    }

    public int getCount()
    {
        return filterArray.size();
    }

    public Object getItem(int position)
    {
        return filterArray.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolderGallery viewHolderGallery = null;
        if (convertView == null)
        {
            viewHolderGallery = new ViewHolderGallery();
            convertView = layoutInflater.inflate(R.layout.item_gallery_film, null);
            viewHolderGallery.rl_item_gallery = (RelativeLayout) convertView.findViewById(R.id.rl_item_gallery);
            viewHolderGallery.imageView = (ImageView) convertView.findViewById(R.id.iv_item_gallery_film);
            viewHolderGallery.textView = (TextView) convertView.findViewById(R.id.tv_item_gallery_film);
            convertView.setTag(viewHolderGallery);
        } else
        {
            viewHolderGallery = (ViewHolderGallery) convertView.getTag();
        }

        ViewGroup.LayoutParams layoutParams = viewHolderGallery.imageView.getLayoutParams();
        layoutParams.width = 100;
        layoutParams.height = 130;
        viewHolderGallery.imageView.setLayoutParams(layoutParams);
        viewHolderGallery.imageView.setImageResource(filterArray.get(position).resourceId);
        viewHolderGallery.textView.setText(filterArray.get(position).filterName);
        if (selectItem == position)
        {
            viewHolderGallery.rl_item_gallery.setBackgroundColor(context.getResources().getColor(R.color.text));
        } else
        {
            viewHolderGallery.rl_item_gallery.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
        return convertView;
    }


    public void setSelectItem(int selectItem)
    {
        if (this.selectItem != selectItem)
        {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }


    public static class ViewHolderGallery
    {
        public RelativeLayout rl_item_gallery;
        public ImageView imageView;
        public TextView textView;
    }

}
