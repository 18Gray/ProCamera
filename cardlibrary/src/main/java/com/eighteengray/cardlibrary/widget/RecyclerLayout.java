package com.eighteengray.cardlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.eighteengray.cardlibrary.R;
import com.eighteengray.cardlibrary.bean.BaseDataBean;
import java.util.List;



/**
 * Created by lutao on 2017/9/29.
 */

public class RecyclerLayout extends RelativeLayout
{
    Context context;
    AttributeSet attrs;
    int layoutManagerNum;

    SwipeRefreshLayout swipe_refresh_layout;

    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;
    BaseRecyclerAdapter baseRecyclerAdapter;
    RecyclerViewScroll recyclerViewScroll;
    private int lastVisibleItem;

    ContentLoadingProgressBar loadmore_progressbar;

    RelativeLayout rl_error;


    public RecyclerLayout(Context context)
    {
        super(context);
        this.context = context;
        initView();
    }

    public RecyclerLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.layout_recycler, this);
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        loadmore_progressbar = (ContentLoadingProgressBar) findViewById(R.id.loadmore_progressbar);
        rl_error = (RelativeLayout) findViewById(R.id.rl_error);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerLayout);
        layoutManagerNum = a.getInteger(R.styleable.RecyclerLayout_LayoutManagerNum, 0);
        a.recycle();

        swipe_refresh_layout.setColorSchemeColors(getResources().getColor(R.color.primary), getResources().getColor(R.color.accent), getResources().getColor(R.color.primary_text), getResources().getColor(R.color.text));
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if(recyclerViewScroll != null){
                    recyclerViewScroll.refreshData();
                }
            }
        });

        switch (layoutManagerNum)
        {
            case 1:
                layoutManager = new LinearLayoutManager(context);
                ((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
                break;
            case 2:
                layoutManager = new LinearLayoutManager(context);
                ((LinearLayoutManager)layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
                break;
            case 3:
                layoutManager = new GridLayoutManager(context, 3);
                break;
            case 4:
                layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == baseRecyclerAdapter.getItemCount())
                {
                   if(recyclerViewScroll != null){
                       recyclerViewScroll.getMoreData();
                   }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(layoutManager instanceof LinearLayoutManager)
                {
                    lastVisibleItem = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                }
                if(recyclerViewScroll != null)
                {
                    if(dy > 0)
                    {
                        recyclerViewScroll.downScroll();
                    }
                    else
                    {
                        recyclerViewScroll.upScroll();
                    }
                }
            }
        });
    }

    public void showLoadingView(){
        swipe_refresh_layout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipe_refresh_layout.setRefreshing(true);
            }
        });

        swipe_refresh_layout.setVisibility(VISIBLE);
        recycler_view.setVisibility(GONE);
        loadmore_progressbar.setVisibility(GONE);
        rl_error.setVisibility(GONE);
    }

    public <T> void showRecyclerView(List<BaseDataBean<T>> dataBeanList, String viewModelPackage){
        if(baseRecyclerAdapter == null){
            baseRecyclerAdapter = new BaseRecyclerAdapter(context, viewModelPackage);
            recycler_view.setAdapter(baseRecyclerAdapter);
        }

        swipe_refresh_layout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipe_refresh_layout.setRefreshing(false);
            }
        });
        baseRecyclerAdapter.setData(dataBeanList);

        swipe_refresh_layout.setVisibility(VISIBLE);
        recycler_view.setVisibility(VISIBLE);
        loadmore_progressbar.setVisibility(GONE);
        rl_error.setVisibility(GONE);
    }

    public <T> void showHideMoreView(List<BaseDataBean<T>> dataBeanList, boolean isShowMoreView){
        swipe_refresh_layout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipe_refresh_layout.setRefreshing(false);
            }
        });
        baseRecyclerAdapter.setData(dataBeanList);

        swipe_refresh_layout.setVisibility(VISIBLE);
        recycler_view.setVisibility(VISIBLE);
        if(isShowMoreView){
            loadmore_progressbar.setVisibility(VISIBLE);
        }else {
            loadmore_progressbar.setVisibility(GONE);
        }
        rl_error.setVisibility(GONE);
    }

    public void showErrorView(){
        swipe_refresh_layout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipe_refresh_layout.setRefreshing(false);
            }
        });

        rl_error.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(recyclerViewScroll != null){
                    recyclerViewScroll.refreshData();
                }
            }
        });

        swipe_refresh_layout.setVisibility(GONE);
        recycler_view.setVisibility(GONE);
        loadmore_progressbar.setVisibility(GONE);
        rl_error.setVisibility(VISIBLE);
    }


    public interface RecyclerViewScroll
    {
        public void refreshData();
        public void getMoreData();
        public void upScroll();
        public void downScroll();
    }

    public void setRecyclerViewScroll(RecyclerViewScroll rvs)
    {
        this.recyclerViewScroll = rvs;
    }

}
