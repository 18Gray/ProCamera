package com.eighteengray.procameralibrary.album;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.eighteengray.procameralibrary.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;

/**
 * @ClassName: AlbumView
 * @Description: 相册View，继承于GridView，封装了Adapter和图片加载方法
 * @author LinJ
 * @date 2015-1-5 下午5:09:08
 * 
 */
public class AlbumGridView extends GridView
{
	public final static String TAG = "AlbumView";
	/** 当前是否处于编辑状态 true为编辑 */
	private boolean mEditable;

	public AlbumGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// 设置网络图片加载参数
		setBackgroundColor(Color.WHITE);
		// 隐藏垂直滚动条
		setVerticalScrollBarEnabled(false);
	}

	/**
	 * 全选图片
	 * 
	 * @param listener
	 *            选择图片后执行的回调函数
	 */
	public void selectAll(OnCheckedChangeListener listener)
	{
		((AlbumViewAdapter) getAdapter()).selectAll(listener);
	}

	/**
	 * 取消全选图片
	 * 
	 * @param listener
	 *            选择图片后执行的回调函数
	 */
	public void unSelectAll(OnCheckedChangeListener listener)
	{
		((AlbumViewAdapter) getAdapter()).unSelectAll(listener);
	}

	/**
	 * 设置可编辑状态
	 * 
	 * @param editable
	 *            是否可编辑
	 */
	public void setEditable(boolean editable)
	{
		mEditable = editable;
		((AlbumViewAdapter) getAdapter()).notifyDataSetChanged(null);
	}

	/**
	 * 设置可编辑状态
	 * 
	 * @param editable
	 *            是否可编辑
	 * @param listener
	 *            选择图片后执行的回调函数
	 */
	public void setEditable(boolean editable,
			OnCheckedChangeListener listener)
	{
		mEditable = editable;
		((AlbumViewAdapter) getAdapter()).notifyDataSetChanged(listener);
	}

	/**
	 * 获取可编辑状态
	 * 
	 * @return
	 */
	public boolean getEditable()
	{
		return mEditable;
	}

	/**
	 * 获取当前选择的图片路径集合
	 * 
	 * @return
	 */
	public Set<String> getSelectedItems()
	{
		return ((AlbumViewAdapter) getAdapter()).getSelectedItems();
	}

	public void notifyDataSetChanged()
	{
		// TODO Auto-generated method stub
		((AlbumViewAdapter) getAdapter()).notifyDataSetChanged();
	}

	/**
	 * @ClassName: OnCheckedChangeListener
	 * @Description: 图片选中后的监听接口，用以在activity内做回调处理
	 * @author LinJ
	 * @date 2015-1-5 下午5:13:43
	 * 
	 */
	public interface OnCheckedChangeListener
	{
		public void onCheckedChanged(Set<String> set);
	}

	/**
	 * @ClassName: AlbumViewAdapter
	 * @Description: 相册GridView适配器
	 * @author LinJ
	 * @date 2015-1-5 下午5:14:14
	 * 
	 */
	public class AlbumViewAdapter extends BaseAdapter implements
			OnClickListener, CompoundButton.OnCheckedChangeListener
	{

		/** 加载的文件路径集合 */
		List<String> mPaths;

		/** 当前选中的文件的集合 */
		Set<String> itemSelectedSet = new HashSet<String>();

		/** 选中图片后执行的回调函数 */
		OnCheckedChangeListener listener = null;

		public AlbumViewAdapter(List<String> paths)
		{
			super();
			this.mPaths = paths;
		}

		/**
		 * 适配器内容改变时，重新绘制
		 * 
		 * @param listener
		 */
		public void notifyDataSetChanged(
				OnCheckedChangeListener listener)
		{
			// 重置map
			itemSelectedSet = new HashSet<String>();
			this.listener = listener;
			super.notifyDataSetChanged();
		}

		/**
		 * 选中所有文件
		 * 
		 * @param listener
		 */
		public void selectAll(OnCheckedChangeListener listener)
		{
			for (String path : mPaths)
			{
				itemSelectedSet.add(path);
			}
			this.listener = listener;
			super.notifyDataSetChanged();
			if (listener != null)
				listener.onCheckedChanged(itemSelectedSet);
		}

		/**
		 * 取消选中所有文件
		 * 
		 * @param listener
		 */
		public void unSelectAll(OnCheckedChangeListener listener)
		{
			notifyDataSetChanged(listener);
			if (listener != null)
				listener.onCheckedChanged(itemSelectedSet);
		}

		/**
		 * 获取当前选中文件的集合
		 * 
		 * @return
		 */
		public Set<String> getSelectedItems()
		{
			return itemSelectedSet;
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mPaths.size();
		}

		@Override
		public String getItem(int position)
		{
			// TODO Auto-generated method stub
			return mPaths.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ThumbnaiImageView albumItemView = (ThumbnaiImageView) convertView;
			if (albumItemView == null)
				albumItemView = new ThumbnaiImageView(getContext());
			albumItemView.setOnCheckedChangeListener(this);
			// 设置点击事件，将ItemClick事件转化为AlbumItemView的Click事件
			albumItemView.setOnClickListener(this);
			String path = getItem(position);
			albumItemView.setTags(path, position, mEditable,
					itemSelectedSet.contains(path));
			return albumItemView;
		}

		@Override
		public void onClick(View v)
		{
			if (getOnItemClickListener() != null)
			{
				// 这里取了上两层父类，因为真正触onClick的是FilterImageView
				ThumbnaiImageView view = (ThumbnaiImageView) v.getParent()
						.getParent();
				getOnItemClickListener().onItemClick(AlbumGridView.this, view,
						view.getPosition(), 0L);
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked)
		{
			// TODO Auto-generated method stub
			if (buttonView.getTag() == null)
				return;
			if (isChecked)
				itemSelectedSet.add(buttonView.getTag().toString());
			else
				itemSelectedSet.remove(buttonView.getTag().toString());
			if (listener != null)
				listener.onCheckedChanged(itemSelectedSet);
		}
	}
}
