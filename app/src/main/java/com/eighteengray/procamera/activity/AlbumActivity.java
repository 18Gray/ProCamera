package com.eighteengray.procamera.activity;

import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.component.DaggerAlbumComponent;
import com.eighteengray.procamera.contract.IAlbumContract;
import com.eighteengray.procamera.module.PresenterModule;
import com.eighteengray.procamera.presenter.AlbumPresenter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerAdapter;
import com.eighteengray.procamera.widget.baserecycler.BaseRecyclerViewHolder;
import com.eighteengray.procamera.widget.dialogfragment.ImageFoldersDialogFragment;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改方案：
 * AlbumActivity作为View，实现IView方法。
 * 通过Dagger注入一个Presenter，AlbumActivity执行Presenter的方法。
 * Presenter会先执行business中业务方法拿到数据，然后执行IView更新界面。business中业务方法返回Observerable。
 * business中业务方法，会调用model中持久层方法，无论是本地获取数据，还是retrofit/okhttp网络获取数据，全部都封装成Observerable返回。
 */
public class AlbumActivity extends BaseActivity implements IAlbumContract.IView
{
    @BindView(R.id.iv_back_album)
    ImageView iv_back_album;
    @BindView(R.id.tv_done_album)
    TextView tv_done_album;

    @BindView(R.id.rcv_pics_album)
    RecyclerView rcv_pics_album;
    BaseRecyclerAdapter<String> picsAdapter;


    @BindView(R.id.tv_select_album)
    TextView tv_select_album;

    private ContentResolver mContentResolver;
    private int currentImageFolderNum = 0;
    private ArrayList<ImageFolder> imageFolderArrayList = new ArrayList<>();

    @Inject
    AlbumPresenter albumPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        mContentResolver = getContentResolver();
        initView();
    }

    private void initView()
    {
        //图片表格
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumActivity.this, 3);
        rcv_pics_album.setLayoutManager(gridLayoutManager);
        picsAdapter = new BaseRecyclerAdapter<String>(R.layout.grid_item_picture)
        {
            @Override
            public void setData2ViewR(BaseRecyclerViewHolder baseRecyclerViewHolder, final String item, int position)
            {
                Bitmap bitmap = ImageUtils.getBitmapFromPath(item);
                Bitmap thumnailBitmap = ThumbnailUtils.extractThumbnail(bitmap, 320, 320);
                ImageView imageView = baseRecyclerViewHolder.getViewById(R.id.iv_item_grid);
                imageView.setImageBitmap(thumnailBitmap);

                imageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(AlbumActivity.this, ImageProcessActivity.class);
                        SaveImage saveImage = new SaveImage();
                        saveImage.saveImagePath = item;
                        intent.putExtra("saveImage", saveImage);
                        startActivity(intent);
                    }
                });
            }
        };
        rcv_pics_album.setAdapter(picsAdapter);
        tv_select_album.setText("所有图片");
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        DaggerAlbumComponent.builder().presenterModule(new PresenterModule(this)).build().inject(this);
        //获取数据
        albumPresenter.getAlbumData(AlbumActivity.this);
    }


    @OnClick({R.id.iv_back_album, R.id.tv_done_album, R.id.tv_select_album})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back_album:
                finish();
                break;

            case R.id.tv_done_album:
                finish();
                break;

            case R.id.tv_select_album:
                ImageFoldersDialogFragment imageFoldersDialogFragment = new ImageFoldersDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.IMAGEFOLDERS, imageFolderArrayList);
                bundle.putInt(Constants.CURRENTFOLDERNUM, currentImageFolderNum);
                imageFoldersDialogFragment.setArguments(bundle);
                imageFoldersDialogFragment.show(getSupportFragmentManager(), "");
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageFolderSelected(ImageFolderEvent imageFolderEvent)
    {
        currentImageFolderNum = imageFolderEvent.getCurrentImageFolderNum();
        picsAdapter.notifyDataSetChanged();
        tv_select_album.setText(imageFolderArrayList.get(currentImageFolderNum).getName());
    }


    @Override
    public void setAdapterData(List<ImageFolder> imageFolders)
    {
        imageFolderArrayList = (ArrayList<ImageFolder>) imageFolders;
        if(imageFolderArrayList != null && imageFolderArrayList.size() > 0)
        {
            picsAdapter.setData(imageFolderArrayList.get(0).getImagePathList());
        }
    }


}
