package com.eighteengray.procameralibrary.album;

import java.util.List;

import com.eighteengray.commonutillibrary.FileOperateUtil;
import com.eighteengray.procameralibrary.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;



/**
 * @ClassName: AlbumViewPager
 * @Description: 自定义viewpager 优化了事件拦截
 * @author LinJ
 * @date 2015-1-9 下午5:33:33
 * 
 */
public class AlbumViewPager extends ViewPager implements MatrixImageView.OnMovingListener
{
	public final static String TAG = "AlbumViewPager";


	/** 当前子控件是否处理拖动状态 */
	private boolean mChildIsBeingDragged = false;

	/** 界面单击事件 用以显示和隐藏菜单栏 */
	private MatrixImageView.OnSingleTapListener onSingleTapListener;

	/** 播放按钮点击事件 */
	private OnPlayVideoListener onPlayVideoListener;

	public AlbumViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 删除当前项
	 * 
	 * @return “当前位置/总数量”
	 */
	public String deleteCurrentPath()
	{
		return ((ViewPagerAdapter) getAdapter())
				.deleteCurrentItem(getCurrentItem());

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		if (mChildIsBeingDragged)
			return false;
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void startDrag()
	{
		// TODO Auto-generated method stub
		mChildIsBeingDragged = true;
	}

	@Override
	public void stopDrag()
	{
		// TODO Auto-generated method stub
		mChildIsBeingDragged = false;
	}

	public void setOnSingleTapListener(MatrixImageView.OnSingleTapListener onSingleTapListener)
	{
		this.onSingleTapListener = onSingleTapListener;
	}

	public void setOnPlayVideoListener(OnPlayVideoListener onPlayVideoListener)
	{
		this.onPlayVideoListener = onPlayVideoListener;
	}

	public interface OnPlayVideoListener
	{
		void onPlay(String path);
	}

	public class ViewPagerAdapter extends PagerAdapter
	{
		private List<String> paths;// 大图地址 如果为网络图片 则为大图url

		public ViewPagerAdapter(List<String> paths)
		{
			this.paths = paths;
		}

		@Override
		public int getCount()
		{
			return paths.size();
		}

		@Override
		public Object instantiateItem(ViewGroup viewGroup, int position)
		{
			// 注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
			// 因为直接加到viewGroup下会导致返回的view为viewGroup
			View imageLayout = inflate(getContext(), R.layout.item_album_pager,
					null);
			viewGroup.addView(imageLayout);
			assert imageLayout != null;
			MatrixImageView imageView = (MatrixImageView) imageLayout
					.findViewById(R.id.image);
			imageView.setOnMovingListener(AlbumViewPager.this);
			imageView.setOnSingleTapListener(onSingleTapListener);
			String path = paths.get(position);
			// final ProgressBar spinner = (ProgressBar)
			// imageLayout.findViewById(R.id.loading);

			ImageButton videoIcon = (ImageButton) imageLayout
					.findViewById(R.id.videoicon);
			if (path.contains("video"))
			{
				videoIcon.setVisibility(View.VISIBLE);
			} else
			{
				videoIcon.setVisibility(View.GONE);
			}
			videoIcon.setOnClickListener(playVideoListener);
			videoIcon.setTag(path);
			imageLayout.setTag(path);
//			mImageLoader.loadImageSync(path, mOptions);
//			loadImage(path, imageView, mOptions);
			return imageLayout;
		}

		OnClickListener playVideoListener = new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				String path = v.getTag().toString();
				path = path.replace(
						getContext().getResources().getString(
								R.string.Thumbnail), getContext()
								.getResources().getString(R.string.Video));
				path = path.replace(".jpg", ".3gp");
				if (onPlayVideoListener != null)
					onPlayVideoListener.onPlay(path);
				else
				{
					Toast.makeText(getContext(), "onPlayVideoListener",
							Toast.LENGTH_SHORT).show();
					// throw new
					// RuntimeException("onPlayVideoListener is null");
				}
			}
		};

		@Override
		public int getItemPosition(Object object)
		{
			// 在notifyDataSetChanged时返回None，重新绘制
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int arg1, Object object)
		{
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		// 自定义获取当前view方法
		public String deleteCurrentItem(int position)
		{
			String path = paths.get(position);
			if (path != null)
			{
				FileOperateUtil.deleteSourceFile(path, getContext());
				paths.remove(path);
				notifyDataSetChanged();
				if (paths.size() > 0)
					return (getCurrentItem() + 1) + "/" + paths.size();
				else
				{
					return "0/0";
				}
			}
			return null;
		}
	}

}
