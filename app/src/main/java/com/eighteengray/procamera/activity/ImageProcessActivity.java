package com.eighteengray.procamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.commonutillibrary.ScreenUtils;
import com.eighteengray.imageprocesslibrary.bitmapfilter.ColorBitmapFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.adapter.BitmapFilterAdapter;
import com.eighteengray.procamera.adapter.ImageFilterAdapter;
import com.eighteengray.procamera.adapter.MarkAdapter;
import com.eighteengray.procamera.bean.FilterInfo;
import com.eighteengray.procamera.bean.MarkInfo;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.widget.MyTouchImageView;
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory;
import com.eighteengray.procameralibrary.common.Constants;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.filter;


public class ImageProcessActivity extends BaseActivity implements OnSeekBarChangeListener
{
    //上部
    @BindView(R.id.rl_topmenu_gallery)
    RelativeLayout rl_topmenu_gallery;
    @BindView(R.id.iv_finish_gallery)
    ImageView iv_finish_gallery;
    @BindView(R.id.button_next_gallery)
    Button button_next_gallery;

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
//    ImageFilterAdapter imageFilterAdapter;

        //印记
    @BindView(R.id.gallery_yinji_film)
    Gallery gallery_yinji_film;
    MarkAdapter markAdapter;
    MyTouchImageView currentMyTouchImageView;

    //中部
    @BindView(R.id.fl_center_imageprocess)
    FrameLayout fl_center_imageprocess;
    @BindView(R.id.iv_center_imageprocess)
    ImageView iv_center_imageprocess;
    @BindView(R.id.tv_subtitletext_film)
    TextView tv_subtitletext_film;

    int imageViewWidth, imageViewHeight;

    //对比度
    ColorBitmapFilter colorBitmapFilter;


    //用于保存图像元数据
    SaveImage saveImage;
    Bitmap currentBitmap;  //传进来的原始图像
    Bitmap currentShowBitmap; //处理过后的图像，同样的处理过程要基于currentBitmap原始图像进行

    //用于处理图像
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Constants.IMAGEPROCESS:
                    iv_center_imageprocess.setImageBitmap(currentShowBitmap);
                    break;
            }
        }
    };
    ExecutorService executorService;



    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageprocess);
        ButterKnife.bind(this);
        saveImage = new SaveImage();
        saveImage.saveImagePath = getIntent().getStringExtra("imagePath");
        executorService = Executors.newSingleThreadExecutor();
        initView();
    }


    private void initView()
    {
        currentBitmap = ImageUtils.getBitmapFromPath(saveImage.saveImagePath);
        currentShowBitmap = currentBitmap;
        iv_center_imageprocess.setImageBitmap(currentShowBitmap);

        //滤镜
        filterAdapter = new BitmapFilterAdapter(ImageProcessActivity.this);
        gallery_filter_film.setAdapter(filterAdapter);
      /*  imageFilterAdapter = new ImageFilterAdapter(ImageProcessActivity.this);
        gallery_filter_film.setAdapter(imageFilterAdapter);*/
        gallery_filter_film.setSelection(2);
        gallery_filter_film.setSpacing(10);
        gallery_filter_film.setAnimationDuration(3000);
        gallery_filter_film.setUnselectedAlpha(5);
        /*gallery_filter_film.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                final FilterInfo filterInfo = (FilterInfo) filterAdapter.getItem(position);
                Runnable runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        currentShowBitmap = filterInfo.filter.process(currentBitmap, 0);
                        handler.sendEmptyMessage(Constants.IMAGEPROCESS);
                    }
                };
                executorService.submit(runnable);
            }
        });*/
        gallery_filter_film.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                /*IImageFilter filter = (IImageFilter) imageFilterAdapter.getItem(position);
                new ProcessImageTask(filter).execute();*/
                FilterInfo filterInfo = (FilterInfo) filterAdapter.getItem(position);
                ProcessImageTask processImageTask = new ProcessImageTask(filterInfo);
                processImageTask.execute();
            }
        });

        //印记
        markAdapter = new MarkAdapter(ImageProcessActivity.this);
        gallery_yinji_film.setAdapter(markAdapter);
        gallery_yinji_film.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
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
        imageViewWidth = iv_center_imageprocess.getWidth();
        imageViewHeight = iv_center_imageprocess.getHeight();
    }


    @OnClick({R.id.iv_finish_gallery, R.id.button_next_gallery,
            R.id.rl_cut_film, R.id.rl_filter_film, R.id.rl_subtitle_film, R.id.rl_yinji_film, R.id.rl_duibidu_film,
            R.id.tv_subtitletext_film})
    public void click(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_finish_gallery:
                finish();
                break;

            case R.id.button_next_gallery:
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



    //Seekbar滑动
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        int flag = 0;
        switch (seekBar.getId())
        {
            case R.id.seekBarSaturation: // 饱和度
                flag = 0;
                colorBitmapFilter.setSaturation(progress);
                break;

            case R.id.seekBarDuibidu: // 色相
                flag = 1;
                colorBitmapFilter.SetHue(progress);
                break;

            case R.id.seekBarLight: // 亮度
                flag = 2;
                colorBitmapFilter.SetLum(progress);
                break;
        }
        currentShowBitmap = colorBitmapFilter.process(currentShowBitmap, flag);
        iv_center_imageprocess.setImageBitmap(currentShowBitmap);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
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

                SaveImage itemSaveImage = null;
                //取出SaveImage，从saveImagePath中取出图像，然后绘制字幕，绘制印记，再保存到Constants.resultPath路径下。
                Bitmap itemBitmap = ImageUtils.getBitmapFromPath(itemSaveImage.saveImagePath);

                //水印图
                if (itemSaveImage.yinjiBitmap != null)
                {
                    itemBitmap = ImageUtils.watermarkWithBmp(itemBitmap, itemSaveImage.yinjiBitmap, itemSaveImage.x, itemSaveImage.y);
                }

                //带字幕图
                if (!TextUtils.isEmpty(itemSaveImage.subtitle))
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

                    itemBitmap = ImageUtils.watermarkWithText(itemBitmap, itemSaveImage.subtitle, x, y, fontSize, R.color.text);
                }
                File itemFile = new File(itemSaveImage.saveImagePath);
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



    public class ProcessImageTask extends AsyncTask<Void, Void, Bitmap>
    {
//        private IImageFilter filter;
        FilterInfo filterInfo;

        public ProcessImageTask(FilterInfo filterInfo)
        {
            this.filterInfo = filterInfo;
        }

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(ImageProcessActivity.this, "处理中", Toast.LENGTH_LONG).show();
        }

        @Override
        public Bitmap doInBackground(Void... params)
        {
            /*Image img = null;
            try
            {
                Bitmap bitmap = Bitmap.createBitmap(currentShowBitmap.getWidth(), currentShowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                img = new Image(bitmap);
                img.updateColorArray();
                if (filter != null)
                {
                    img = filter.process(img);
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            } catch (Exception e)
            {
                if (img != null && img.destImage.isRecycled())
                {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc(); // 提醒系统及时回收
                }
            } finally
            {
                if (img != null && img.image.isRecycled())
                {
                    img.image.recycle();
                    img.image = null;
                    System.gc(); // 提醒系统及时回收
                }
            }
            return null;*/

            Bitmap bitmap = Bitmap.createBitmap(currentShowBitmap.getWidth(), currentShowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Bitmap processBitmap = filterInfo.filter.process(bitmap, 0);
            Bitmap resultBitmap = ImageUtils.zoomBitmap(processBitmap, imageViewWidth, imageViewHeight);
            return resultBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            if (result != null)
            {
                iv_center_imageprocess.setImageBitmap(result);
                Toast.makeText(ImageProcessActivity.this, "处理完成", Toast.LENGTH_LONG).show();
            }
        }
    }

}
