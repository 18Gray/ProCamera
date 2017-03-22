package com.eighteengray.procamera.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.eighteengray.procamera.R;


public class GridImageAdapter extends BaseAdapter
{
	private Context context;
	private static LayoutInflater inflater;
	private GridImageViewHolder holder = null;
	private ArrayList<Bitmap> list;
	
	
	
	public GridImageAdapter(Context context, ArrayList<Bitmap> list)
	{
		this.list = list;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	
	
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	
	
	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Bitmap currentBitmap = list.get(position);
		final int currentPosition = position;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_gridimage, null);
			holder = new GridImageViewHolder();
			holder.iv_item_gridimage = (ImageView) convertView
					.findViewById(R.id.iv_item_gridimage);

			convertView.setTag(holder);
		} 
		else
		{
			holder = (GridImageViewHolder) convertView.getTag();
			
		}
		
		holder.iv_item_gridimage.setImageBitmap(currentBitmap);
		
		
		return convertView;
	}

	
	
	public static class GridImageViewHolder
	{
		private ImageView iv_item_gridimage;
	}

	
	
}
