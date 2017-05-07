package com.eighteengray.procamera.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.eighteengray.imageprocesslibrary.imagefilter.Gradient;
import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.BlackWhiteFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.BrightContrastFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.ColorToneFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.FeatherFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.FilmFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.HslModifyFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.LightFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.NoiseFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.PaintBorderFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.RadialDistortionFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.SaturationModifyFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.SharpFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.concretefilter.VignetteFilter;
import com.eighteengray.procamera.R;
import java.util.ArrayList;
import java.util.List;



public class ImageFilterAdapter extends BaseAdapter
{
    private class FilterInfo
    {
        public int filterID;
        public IImageFilter filter;

        public FilterInfo(int filterID, IImageFilter filter)
        {
            this.filterID = filterID;
            this.filter = filter;
        }
    }

    private Context mContext;
    private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

    public ImageFilterAdapter(Context c)
    {
        mContext = c;

        filterArray.add(new FilterInfo(R.drawable.filter_meibai, new HslModifyFilter(20f)));
        filterArray.add(new FilterInfo(R.drawable.filter_feather, new HslModifyFilter(40f)));
        filterArray.add(new FilterInfo(R.drawable.filter_baohe, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
        filterArray.add(new FilterInfo(R.drawable.filter_light, new SharpFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_blackwhite, new FilmFilter(80f)));
        filterArray.add(new FilterInfo(R.drawable.filter_original, new PaintBorderFilter(0x00FF00)));// green
        filterArray.add(new FilterInfo(R.drawable.filter_blackwhite, new BlackWhiteFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_baohe, new BrightContrastFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_feather, new SaturationModifyFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_light, new NoiseFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_feather, new LightFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_blackwhite, new RadialDistortionFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_baohe, new VignetteFilter()));
        filterArray.add(new FilterInfo(R.drawable.filter_lomo, new FeatherFilter()));
    }

    public int getCount()
    {
        return filterArray.size();
    }

    public Object getItem(int position)
    {
        return position < filterArray.size() ? filterArray.get(position).filter
                : null;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Bitmap bmImg = BitmapFactory
                .decodeResource(mContext.getResources(),
                        filterArray.get(position).filterID);
        int width = 100;// bmImg.getWidth();
        int height = 100;// bmImg.getHeight();
        bmImg.recycle();
        ImageView imageview = new ImageView(mContext);
        imageview.setImageResource(filterArray.get(position).filterID);
        imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
        imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置显示比例类型
        return imageview;
    }
}