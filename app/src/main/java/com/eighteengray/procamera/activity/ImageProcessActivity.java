package com.eighteengray.procamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eighteengray.commonutillibrary.ImageProcessUtils;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.imageprocesslibrary.bitmapfilter.ColorBitmapFilter;
import com.eighteengray.imageprocesslibrary.bitmapfilter.IBitmapFilter;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.adapter.BitmapFilterAdapter;
import com.eighteengray.procamera.adapter.MarkAdapter;
import com.eighteengray.procamera.bean.FilterInfo;
import com.eighteengray.procamera.bean.MarkInfo;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.widget.MyTouchImageView;
import com.eighteengray.procamera.widget.ProcessImageView;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.common.Constants;
import com.eighteengray.procameralibrary.dataevent.BitmapProcess;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ImageProcessActivity extends BaseActivity
{

    //下部
    @BindView(R.id.ll_bottommenu_film)
    LinearLayout ll_bottommenu_film;

    @BindView(R.id.rl_cut_film)
    RelativeLayout rl_cut_film;
    @BindView(R.id.iv_cut_film)
    ImageView iv_cut_film;
    @BindView(R.id.tv_cut_film)
    TextView tv_cut_film;

    @BindView(R.id.rl_filter_film)
    RelativeLayout rl_filter_film;
    @BindView(R.id.iv_filter_film)
    ImageView iv_filter_film;
    @BindView(R.id.tv_filter_film)
    TextView tv_filter_film;

    @BindView(R.id.rl_subtitle_film)
    RelativeLayout rl_subtitle_film;
    @BindView(R.id.iv_subtitle_film)
    ImageView iv_subtitle_film;
    @BindView(R.id.tv_subtitle_film)
    TextView tv_subtitle_film;

    @BindView(R.id.rl_yinji_film)
    RelativeLayout rl_yinji_film;
    @BindView(R.id.iv_yinji_film)
    ImageView iv_yinji_film;
    @BindView(R.id.tv_yinji_film)
    TextView tv_yinji_film;

    @BindView(R.id.rl_duibidu_film)
    RelativeLayout rl_duibidu_film;
    @BindView(R.id.iv_duibidu_film)
    ImageView iv_duibidu_film;
    @BindView(R.id.tv_duibidu_film)
    TextView tv_duibidu_film;

    //中下部
    @BindView(R.id.ll_gallery_film)
    LinearLayout ll_gallery_film;
        //滤镜
    @BindView(R.id.gallery_filter_film)
    Gallery gallery_filter_film;
    BitmapFilterAdapter filterAdapter;

        //印记
    @BindView(R.id.gallery_yinji_film)
    Gallery gallery_yinji_film;
    MarkAdapter markAdapter;
    MyTouchImageView currentMyTouchImageView;

    //中部
    @BindView(R.id.fl_center_imageprocess)
    FrameLayout fl_center_imageprocess;
    @BindView(R.id.piv_center_imageprocess)
    ProcessImageView piv_center_imageprocess;
    @BindView(R.id.tv_subtitletext_film)
    TextView tv_subtitletext_film;

    //对比度
    ColorBitmapFilter colorBitmapFilter;


    //用于保存图像元数据
    SaveImage saveImage;
    Bitmap currentBitmap;  //传进来的原始图像
    Bitmap currentShowBitmap; //处理过后的图像，同样的处理过程要基于currentBitmap原始图像进行



    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        saveImage = new SaveImage();
        saveImage.saveImagePath = getIntent().getStringExtra("imagePath");
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutResId()
    {
        return R.layout.activity_imageprocess;
    }



    private void initView()
    {
        btn_right.setVisibility(View.VISIBLE);

        //滤镜
        filterAdapter = new BitmapFilterAdapter(ImageProcessActivity.this);
        gallery_filter_film.setAdapter(filterAdapter);
        gallery_filter_film.setSelection(2);
        gallery_filter_film.setSpacing(10);
        gallery_filter_film.setAnimationDuration(3000);
        gallery_filter_film.setUnselectedAlpha(5);
        gallery_filter_film.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                FilterInfo filterInfo = (FilterInfo) filterAdapter.getItem(position);
                IBitmapFilter bitmapFilter = filterInfo.filter;
                ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(bitmapFilter.createColorMatrix(10));
                piv_center_imageprocess.setColorFilter(colorMatrixColorFilter);
            }
        });

        //印记
        markAdapter = new MarkAdapter(ImageProcessActivity.this);
        gallery_yinji_film.setAdapter(markAdapter);
        gallery_yinji_film.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                if(currentMyTouchImageView != null)
                {
                    fl_center_imageprocess.removeView(currentMyTouchImageView);
                }
                currentMyTouchImageView = new MyTouchImageView(ImageProcessActivity.this);
                MarkInfo markInfo = (MarkInfo) markAdapter.getItem(position);
                currentMyTouchImageView.setImageResource(markInfo.getMarkAdapterResource());
                currentMyTouchImageView.setVisibility(View.VISIBLE);
                fl_center_imageprocess.addView(currentMyTouchImageView);
            }
        });

        //对比度
        colorBitmapFilter = new ColorBitmapFilter(currentShowBitmap);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        int maxWidth = ScreenUtils.getScreenWidth(ImageProcessActivity.this);
        int maxHeight = ScreenUtils.getScreenHeight(ImageProcessActivity.this);
        currentBitmap = ImageUtils.getBitmapFromPathSimple(saveImage.saveImagePath);
        currentShowBitmap = Bitmap.createBitmap(currentBitmap);
        piv_center_imageprocess.setImageBitmap(currentShowBitmap);
    }



    @OnClick({R.id.rl_cut_film, R.id.rl_filter_film, R.id.rl_subtitle_film, R.id.rl_yinji_film, R.id.rl_duibidu_film,
            R.id.tv_subtitletext_film})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_right:
                saveResultImages(saveImage);
                break;

            case R.id.rl_cut_film:
                Intent intent_cut = new Intent(ImageProcessActivity.this, CutActivity.class);
                intent_cut.putExtra(Constants.CROPIMAGEPATH, saveImage.saveImagePath);
                startActivityForResult(intent_cut, Constants.CUT_IMAGE);
                break;

            case R.id.rl_filter_film:
                gallery_filter_film.setVisibility(View.VISIBLE);
                gallery_yinji_film.setVisibility(View.GONE);
                break;

            case R.id.rl_subtitle_film:
                Toast.makeText(ImageProcessActivity.this, "未完待续", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_yinji_film:
                gallery_filter_film.setVisibility(View.GONE);
                gallery_yinji_film.setVisibility(View.VISIBLE);
                break;

            case R.id.rl_duibidu_film:
                gallery_filter_film.setVisibility(View.GONE);
                gallery_yinji_film.setVisibility(View.GONE);
                PopupWindow popupWindow_Contrast = PopupWindowFactory.createContrastPopupWindow(ImageProcessActivity.this);
                popupWindow_Contrast.showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 5, 150);
                popupWindow_Contrast.update();
                break;

            case R.id.tv_subtitletext_film:

                break;
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    //EventBus处理
    @Subscribe(threadMode = ThreadMode.MAIN)  //轻按：显示焦点，完成聚焦和测光。
    public void onSeekBarChanged(BitmapProcess.ContrastEvent contrastEvent)
    {
        int seekBarNum = contrastEvent.getSeekBarNum();
        int progress = contrastEvent.getProgress();
        switch (seekBarNum)
        {
            case 0:
                colorBitmapFilter.setSaturation(progress);
                break;

            case 1:
                colorBitmapFilter.SetHue(progress);
                break;

            case 2:
                colorBitmapFilter.SetLum(progress);
                break;
        }
        currentShowBitmap = colorBitmapFilter.process(currentShowBitmap, seekBarNum);
        piv_center_imageprocess.setImageBitmap(currentShowBitmap);
    }


    private void saveResultImages(final SaveImage saveImage)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //先保存currentShowBitmap到currentSaveImage.saveImagePath中，此时所有Bitmap都保存到了SD卡中。
                if (currentMyTouchImageView != null)
                {
                    saveImage.yinjiBitmap = currentMyTouchImageView.getResultBitmap();
                    saveImage.x = (int) currentMyTouchImageView.getStartX();
                    saveImage.y = (int) currentMyTouchImageView.getStartY();
                    saveImage.x = (int) currentMyTouchImageView.x_down;
                    saveImage.y = (int) currentMyTouchImageView.y_down;
                }
                File file = new File(saveImage.saveImagePath);
                ImageUtils.saveBitmap(currentBitmap, file.getParent().toString(), file.getName().toString());

                //取出SaveImage，从saveImagePath中取出图像，然后绘制字幕，绘制印记，再保存到Constants.resultPath路径下。
                Bitmap itemBitmap = ImageUtils.getBitmapFromPathSimple(saveImage.saveImagePath);

                //水印图
                if (saveImage.yinjiBitmap != null)
                {
                    itemBitmap = ImageProcessUtils.watermarkWithBmp(itemBitmap, saveImage.yinjiBitmap, saveImage.x, saveImage.y);
                }

                //带字幕图
                if (!TextUtils.isEmpty(saveImage.subtitle))
                {
                    int bitmapwidth = itemBitmap.getWidth();
                    Paint paint = new Paint();
                    paint.setTextSize(tv_subtitletext_film.getTextSize());
                    int subtitlewidth = (int) paint.measureText(tv_subtitletext_film.getText().toString());

                    int x = (bitmapwidth - subtitlewidth) / 2;
                    int y = itemBitmap.getHeight() - 30;

                    int fontSize = 0;
                    int maxSize = Math.max(itemBitmap.getWidth(), itemBitmap.getHeight());
                    if (maxSize >= 800)
                    {
                        fontSize = 30;
                    } else if (maxSize >= 600)
                    {
                        fontSize = 25;
                    } else if (maxSize >= 400)
                    {
                        fontSize = 20;
                    } else if (maxSize >= 200)
                    {
                        fontSize = 15;
                    } else
                    {
                        fontSize = 12;
                    }
                    itemBitmap = ImageProcessUtils.watermarkWithText(itemBitmap, saveImage.subtitle, x, y, fontSize, R.color.text);
                }
                File itemFile = new File(saveImage.saveImagePath);
                ImageUtils.saveBitmap(itemBitmap, itemFile.getParent().toString(), itemFile.getName().toString());
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(ImageProcessActivity.this, "Save Success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case Constants.CUT_IMAGE:
                if(resultCode == RESULT_OK)
                {
                    String path = data.getStringExtra(Constants.CROPIMAGEPATH);
                    currentShowBitmap = ImageUtils.getBitmapFromPathSimple(path);
                    piv_center_imageprocess.setImagePath(path);
                    piv_center_imageprocess.setImageBitmap(currentShowBitmap);
                }
                break;
        }

    }
}
