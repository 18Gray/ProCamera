package com.eighteengray.procamera.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
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

import com.eighteengray.commonutillibrary.DataConvertUtil;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.imageprocesslibrary.imagefilter.BlackWhiteFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.BrightContrastFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.FeatherFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.FilmFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.HslModifyFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.IImageFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.Image;
import com.eighteengray.imageprocesslibrary.imagefilter.IncreaseProcessImage;
import com.eighteengray.imageprocesslibrary.imagefilter.LightFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.LomoFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.OriginFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.SaturationModifyFilter;
import com.eighteengray.imageprocesslibrary.imagefilter.SharpFilter;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.common.Constants;
import com.eighteengray.procamera.widget.AllDialog;
import com.eighteengray.procamera.widget.MyTouchImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 多照片处理类
 * 功能：关闭界面；切换照片，添加照片相册；多比例裁剪改进（一定要做图片大小的限制，强制调整），图像滤镜改进，添加字幕改进，印记图片改进，对比度改进；
 * 看其他应用做功能改进。
 * 下一步操作：图像合成改进，字幕位置移动、大小自动调整、字体调整；图像中印记图片的合成图大小调整。
 * <p>
 * <p>
 * 逻辑说明：
 * 原始图像来源：一类是通过照相，一类是通过相册。照相返回的Bitmap保存到自定义路径下;
 * 相册返回的是uri，从uri取出图像保存到自定义路径下。
 * 生成图像的缩略图在内存，显示出来。保存完成后销毁内存中图像。
 * 通过路径+文件名操作图像。
 * 内存中取出一张图像，在内存中形成一张原图（保存图），一张处理图（最起始时跟原图一样）。然后根据其<Image>显示字幕、显示遮幅，用遮幅图片生成一个MyTouchImageView，并根据位置显示在指定位置。
 * 处理图只是作为显示使用，图像处理都针对原图。显示时候需要根据前面传过来的模式，分大片和普通模式显示，参数用mode。
 * 裁剪、滤镜、对比度三类操作，每类操作的每个操作都对处理图做处理，并显示。
 * 如果是功能的切换，则把处理图赋值给原图。下一个操作/每一类操作都针对了原图。
 * 如果是图片的切换，则把处理图赋值给原图，把原图按路径+文件名写到外存上。
 * <p>
 * 内存中有缩略图、印记图，当前原图（保存图）、当前处理图（显示图）。
 * <p>
 * SaveImage:保存图路径，缩略图Bitmap(遍历取出一张保存图，形成缩略图；取出当前选中保存图到内存为currentImage)，
 * 字幕内容、是否遮幅、印记图片、印记图片的外接矩形顶坐标。
 * <p>
 * 三个Activity：CameraAty、SelectPictureActivity、ActivityFilm，且前两个都有相机和相册两种选择，并且存在第一次的
 * startActivityForResult和第二次以后的setResult，跳转逻辑及传递数据很复杂，因此这里屏蔽掉startActivityForResult和setResult，
 * 解决办法：只采用startActivity/onCreate/finish，图像保存在SD卡上，ArrayList保存在为SharedPreference
 * （CameraAty每次启动时取出）。
 * ActivityFilm跳转到其他两个Activity时保存图像，保存ArrayList，建立时获取ArrayList和图像；
 * CameraAty、SelectPictureActivity跳转时保存图像，取出ArrayList，并add已经选定的SaveImage。
 * <p>
 * 问题：1，通过Activity的Intent传递，每个Activity在onCreate时要getIntent接收，然后跳转时再传到下一个Activity。
 * 即两个Activity之间的传递数据，并结束掉当前Activity。这是一个有生命周期（并非全局永久如在Application或SharePreferen或sqlite的生命周期）的数据流，当切换到这三个循环Activity之外的Activity时这个数据流生命周期结束。
 * 2，存到SharePreferen或sqlite中，逻辑简单，随取随存，缺点就是数据是否是实时的，会取出没用的过时数据。如果用CameraAty初始化时清零数据，则实际上会多次调用CameraAty从而把不该清理的数据清理掉了。
 * 存mode和ArrayList<SaveImage>。先getIntent，如果为null，则给一个值。
 */

public class ActivityFilm extends Activity implements OnClickListener, OnSeekBarChangeListener, OnItemSelectedListener {
    //上部
    ImageView iv_finish_gallery;
    Button button_next_gallery;

    //相册gallery
    Gallery gallery_xiangce_film;
    HorizontalGalleryAdapter horizontalGalleryAdapter;
    ArrayList<Bitmap> horizontalBitmaps = new ArrayList<Bitmap>();
    @BindView(R.id.iv_finish_gallery)
    ImageView ivFinishGallery;
    @BindView(R.id.button_next_gallery)
    Button buttonNextGallery;
    @BindView(R.id.gallery_xiangce_film)
    Gallery galleryXiangceFilm;
    @BindView(R.id.rl_topmenu_gallery)
    RelativeLayout rlTopmenuGallery;
    @BindView(R.id.iv_cut_film)
    ImageView ivCutFilm;
    @BindView(R.id.tv_cut_film)
    TextView tvCutFilm;
    @BindView(R.id.rl_cut_film)
    RelativeLayout rlCutFilm;
    @BindView(R.id.iv_filter_film)
    ImageView ivFilterFilm;
    @BindView(R.id.tv_filter_film)
    TextView tvFilterFilm;
    @BindView(R.id.rl_filter_film)
    RelativeLayout rlFilterFilm;
    @BindView(R.id.iv_subtitle_film)
    ImageView ivSubtitleFilm;
    @BindView(R.id.tv_subtitle_film)
    TextView tvSubtitleFilm;
    @BindView(R.id.rl_subtitle_film)
    RelativeLayout rlSubtitleFilm;
    @BindView(R.id.iv_yinji_film)
    ImageView ivYinjiFilm;
    @BindView(R.id.tv_yinji_film)
    TextView tvYinjiFilm;
    @BindView(R.id.rl_yinji_film)
    RelativeLayout rlYinjiFilm;
    @BindView(R.id.iv_duibidu_film)
    ImageView ivDuibiduFilm;
    @BindView(R.id.tv_duibidu_film)
    TextView tvDuibiduFilm;
    @BindView(R.id.rl_duibidu_film)
    RelativeLayout rlDuibiduFilm;
    @BindView(R.id.ll_bottommenu_film)
    LinearLayout llBottommenuFilm;
    @BindView(R.id.gallery_filter_film)
    Gallery galleryFilterFilm;
    @BindView(R.id.gallery_yinji_film)
    Gallery galleryYinjiFilm;
    @BindView(R.id.ll_gallery_film)
    LinearLayout llGalleryFilm;
    @BindView(R.id.tv_subtitletext_film)
    TextView tvSubtitletextFilm;
    @BindView(R.id.fl_gallery)
    FrameLayout flGallery;
    private ContentResolver mContentResolver;

    ArrayList<SaveImage> saveImages = null;
    ArrayList<String> toPostList = new ArrayList<String>();
    int currentGalleryPosition = 0;
    public static int Max = 9;


    //中部
    FrameLayout fl_gallery;

    //图像带字幕
    TextView tv_subtitletext_film;
    String uriString;
    String path;

    //当前内存中的东西
    SaveImage currentSaveImage;
    Bitmap currentThumbnail;
    Bitmap currentBitmap;
    Bitmap currentShowBitmap;
    Bitmap currentYinjiBitmap;

    String subtitle;
    String mode;

    //滤镜
    Gallery gallery_filter_film;
    ImageFilterAdapter filterAdapter;
    ViewHolderGallery viewHolderGallery = null;
    private Dialog dialog;


    //印记
    Gallery gallery_yinji_film;
    YinjiFilterAdapter yinjiFilterAdapter;
    int[] yinjiStrings = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3,
            R.drawable.img_4, R.drawable.img_5, R.drawable.img_6, R.drawable.img_7,
            R.drawable.img_8, R.drawable.img_9, R.drawable.img_10};
    int[] yinjiShowStrings = new int[]{R.drawable.img01, R.drawable.img02, R.drawable.img03,
            R.drawable.img04, R.drawable.img05, R.drawable.img06, R.drawable.img07,
            R.drawable.img08, R.drawable.img09, R.drawable.img_10};

    List<Integer> yinjiList = new ArrayList<Integer>();
    MyTouchImageView currentMyTouchImageView;
    Bitmap yinjiBitmap;

    //对比度
    IncreaseProcessImage increaseProcessImage;


    //底部
    RelativeLayout rl_cut_film, rl_filter_film, rl_subtitle_film, rl_yinji_film, rl_duibidu_film;
    boolean isSubtitled = true;
    LayoutInflater inflater;
    View popView;
    PopupWindow popupWindow;


    private Handler handler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case Constants.SAVEIMAGE:
                    //然后把点击了的SaveImage获取数据，并显示出来。
                    if (currentMyTouchImageView != null) {
                        currentMyTouchImageView.setVisibility(View.GONE);
                        currentMyTouchImageView = null;
                    }

                    checkBitmapRecyled();
                    currentSaveImage = saveImages.get(currentGalleryPosition);
                    currentBitmap = ImageUtils.getBitmapFromPath(currentSaveImage.saveImagePath);

                    //currentShowBitmap是结合了印记的图片，currentBitmap用来保存原图，方便更换印记
                    currentShowBitmap = currentBitmap;

                    closeDialog();

                    if (currentSaveImage.yinjiBitmap != null) {
                        currentYinjiBitmap = ImageUtils.watermarkWithBmp(currentBitmap, currentSaveImage.yinjiBitmap, currentSaveImage.x, currentSaveImage.y);
                        if (currentYinjiBitmap != null) {
                            showBackgroundByMode(currentYinjiBitmap);
                        }
                    } else {
                        currentShowBitmap = currentBitmap;
                        if (currentShowBitmap != null) {
                            showBackgroundByMode(currentShowBitmap);
                        }
                    }

                    //显示字幕和印记
                    tv_subtitletext_film.setText(currentSaveImage.subtitle);
                    break;


                case Constants.IMAGEPROCESS:
                    closeDialog();

                    if (currentShowBitmap != null) {
                        showBackgroundByMode(currentShowBitmap);
                    }
                    break;


                case Constants.SAVERESULTIMAGES:
                    closeDialog();

                    Intent intent2 = new Intent(ActivityFilm.this, PostArticleActivity.class);
                    intent2.putExtra("toPostList", toPostList);
                    startActivity(intent2);
                    finish();
                    break;


                case Constants.ADDIMAGE:
                    Intent intent = new Intent(ActivityFilm.this, AlbumActivity.class);
                    intent.putExtra("saveImages", saveImages);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_film);
        ButterKnife.bind(this);

        //初始化
        mContentResolver = getContentResolver();

        saveImages = (ArrayList<SaveImage>) getIntent().getSerializableExtra("saveImages");
        if (saveImages == null) {
            saveImages = new ArrayList<SaveImage>();
        }

        //清理掉当前的图像和SaveImage，然后定义addSaveImage
        if (currentSaveImage != null) {
            if (currentBitmap != null && !currentBitmap.isRecycled()) {
                currentBitmap.recycle();
            }

            currentSaveImage = null;
        }

        initView();
    }


    private void initView() {
        //上部
        iv_finish_gallery = (ImageView) findViewById(R.id.iv_finish_gallery);
        button_next_gallery = (Button) findViewById(R.id.button_next_gallery);
        gallery_xiangce_film = (Gallery) findViewById(R.id.gallery_xiangce_film);


        //中部
        fl_gallery = (FrameLayout) findViewById(R.id.fl_gallery);
        gallery_filter_film = (Gallery) findViewById(R.id.gallery_filter_film);
        tv_subtitletext_film = (TextView) findViewById(R.id.tv_subtitletext_film);
        gallery_yinji_film = (Gallery) findViewById(R.id.gallery_yinji_film);

        //底部
        rl_cut_film = (RelativeLayout) findViewById(R.id.rl_cut_film);
        rl_filter_film = (RelativeLayout) findViewById(R.id.rl_filter_film);
        rl_subtitle_film = (RelativeLayout) findViewById(R.id.rl_subtitle_film);
        rl_yinji_film = (RelativeLayout) findViewById(R.id.rl_yinji_film);
        rl_duibidu_film = (RelativeLayout) findViewById(R.id.rl_duibidu_film);


        //上部
        iv_finish_gallery.setOnClickListener(this);
        button_next_gallery.setOnClickListener(this);


        //底部
        rl_cut_film.setOnClickListener(this);
        rl_filter_film.setOnClickListener(this);
        rl_subtitle_film.setOnClickListener(this);
        rl_yinji_film.setOnClickListener(this);
        rl_duibidu_film.setOnClickListener(this);
        tv_subtitletext_film.setOnClickListener(this);


        //中部
        LoadHorizontalGallery();

        LoadImageFilter();

        LoadYinjiFilter();

    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(final View v) {
        currentBitmap = DataConvertUtil.drawable2Bitmap(tv_subtitletext_film.getBackground());
        if (currentBitmap != null) {
            showBackgroundByMode(currentBitmap);
        }

        switch (v.getId()) {
            //上部
            case R.id.iv_finish_gallery:
                finish();
                break;

            case R.id.button_next_gallery:
                saveResultImages(saveImages);
                break;


            //底部，点击任何一个按钮，首先要做的就是把currentImage的destImage设置到image上，并显示image。
            //裁剪
            case R.id.rl_cut_film:
                inflater = getLayoutInflater();
                popView = inflater.inflate(R.layout.popupwindow_mode, null);
                popView.bringToFront();
                popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setTouchInterceptor(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }

                        return false;
                    }
                });

                popView.findViewById(R.id.ll_film_popup).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();

                        mode = "大片";
                        File file = new File(currentSaveImage.saveImagePath);
                        ImageUtils.saveBitmap(currentShowBitmap, file.getParent().toString(), file.getName().toString());
                        Intent intent = new Intent(ActivityFilm.this, CutActivity.class);
                        intent.putExtra("mode", mode);
                        intent.putExtra("path", currentSaveImage.saveImagePath);
                        startActivityForResult(intent, Constants.CUT_FILM);
                    }
                });

                popView.findViewById(R.id.ll_normal_popup).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();

                        mode = "普通";
                        File file = new File(currentSaveImage.saveImagePath);
                        ImageUtils.saveBitmap(currentShowBitmap, file.getParent().toString(), file.getName().toString());
                        Intent intent = new Intent(ActivityFilm.this, CutActivity.class);
                        intent.putExtra("mode", mode);
                        intent.putExtra("path", currentSaveImage.saveImagePath);
                        startActivityForResult(intent, Constants.CUT_FILM);

                    }
                });

                popupWindow.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 5, 150);
                popupWindow.update();
                break;

            //过滤
            case R.id.rl_filter_film:
                gallery_filter_film.setVisibility(View.VISIBLE);
                gallery_yinji_film.setVisibility(View.GONE);
                break;


            //字幕
            case R.id.rl_subtitle_film:
                if (isSubtitled) {
                    tv_subtitletext_film.setText("");
                    isSubtitled = false;
                } else if (!isSubtitled) {
                    if (currentSaveImage.subtitle != null && !currentSaveImage.subtitle.equals("")) {
                        tv_subtitletext_film.setText(currentSaveImage.subtitle);
                    } else {
                        tv_subtitletext_film.setText("这世界不止眼前的苟且，还有诗和远方。");
                    }
                    isSubtitled = true;
                }
                break;

            case R.id.tv_subtitletext_film:
                Intent intent2 = new Intent(ActivityFilm.this, SubtitleActivity.class);
                startActivityForResult(intent2, Constants.SUBTITLE);
                break;


            //印记
            case R.id.rl_yinji_film:
                gallery_filter_film.setVisibility(View.GONE);
                gallery_yinji_film.setVisibility(View.VISIBLE);
                break;


            //对比度
            case R.id.rl_duibidu_film:
                gallery_filter_film.setVisibility(View.GONE);
                gallery_yinji_film.setVisibility(View.GONE);

                inflater = getLayoutInflater();
                popView = inflater.inflate(R.layout.popupwindow_strength, null);
                increaseProcessImage = new IncreaseProcessImage(currentBitmap);
                final SeekBar seekBarSaturation = (SeekBar) popView.findViewById(R.id.seekBarSaturation);
                final SeekBar seekBarDuibidu = (SeekBar) popView.findViewById(R.id.seekBarDuibidu);
                final SeekBar seekBarLight = (SeekBar) popView.findViewById(R.id.seekBarLight);
                seekBarSaturation.setOnSeekBarChangeListener(this);
                seekBarDuibidu.setOnSeekBarChangeListener(this);
                seekBarLight.setOnSeekBarChangeListener(this);

                TextView tv_baohedu_strength = (TextView) popView.findViewById(R.id.tv_baohedu_strength);
                TextView tv_duibidu_strength = (TextView) popView.findViewById(R.id.tv_duibidu_strength);
                TextView tv_liangdu_strength = (TextView) popView.findViewById(R.id.tv_liangdu_strength);

                tv_baohedu_strength.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        seekBarSaturation.setVisibility(View.VISIBLE);
                        seekBarDuibidu.setVisibility(View.GONE);
                        seekBarLight.setVisibility(View.GONE);
                    }
                });

                tv_duibidu_strength.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        seekBarSaturation.setVisibility(View.GONE);
                        seekBarDuibidu.setVisibility(View.VISIBLE);
                        seekBarLight.setVisibility(View.GONE);
                    }
                });

                tv_liangdu_strength.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        seekBarSaturation.setVisibility(View.GONE);
                        seekBarDuibidu.setVisibility(View.GONE);
                        seekBarLight.setVisibility(View.VISIBLE);
                    }
                });

                popView.bringToFront();
                popupWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setTouchInterceptor(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }

                        return false;
                    }
                });

                popupWindow.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 5, 150);
                popupWindow.update();
                break;


            default:
                break;
        }

    }


    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        switch (requestCode) {
            case Constants.CUT_FILM:
                if (resultCode == RESULT_OK) {
                    checkBitmapRecyled();
                    String path = data.getStringExtra("cropImagePath");
                    currentBitmap = ImageUtils.getBitmapFromPath(path);
                    currentShowBitmap = currentBitmap;
                    if (currentBitmap != null) {
                        showBackgroundByMode(currentBitmap);
                    }
                }
                break;


            case Constants.SUBTITLE:
                if (resultCode == RESULT_OK) {
                    subtitle = data.getStringExtra("subtitle");
                    if (subtitle != null) {
                        tv_subtitletext_film.setText(subtitle);
                    }
                    currentSaveImage.subtitle = subtitle;
                }
                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    //加载水平滑动相册，除了加载选择的图像，最后加上add按钮。
    @SuppressWarnings("deprecation")
    private void LoadHorizontalGallery() {
        for (int j = 0; j < saveImages.size(); j++) {
            checkBitmapRecyled();
            currentSaveImage = saveImages.get(j);
            currentBitmap = ImageUtils.getBitmapFromPath(currentSaveImage.saveImagePath);
            currentThumbnail = ThumbnailUtils.extractThumbnail(currentBitmap, 60, 60);
            horizontalBitmaps.add(currentThumbnail);
        }
        //添加按钮
        horizontalBitmaps.add(DataConvertUtil.drawable2Bitmap(getResources().getDrawable(R.drawable.btn_add)));

        horizontalGalleryAdapter = new HorizontalGalleryAdapter(ActivityFilm.this, horizontalBitmaps);
        gallery_xiangce_film.setSelection(4);
        gallery_xiangce_film.setAnimationDuration(3000);
        gallery_xiangce_film.setSpacing(10);
        gallery_xiangce_film.setAdapter(horizontalGalleryAdapter);


        //取出第一个SaveImage的图像，并显示出来
        checkBitmapRecyled();
        currentSaveImage = saveImages.get(currentGalleryPosition);
        currentBitmap = ImageUtils.getBitmapFromPath(currentSaveImage.saveImagePath);

        currentShowBitmap = currentBitmap;
        if (currentBitmap != null) {
            showBackgroundByMode(currentBitmap);
        }

        gallery_xiangce_film.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //先保存currentShowBitmap到currentSaveImage.saveImagePath中；currentSaveImage的其他信息保存在ArrayList<SaveImage>中，随时更改。
                //取出position即选中的SaveImage，从路径取出图像，取出其他信息显示出来。

                //把更改的印记信息保存的currentSaveImage中
                if (currentMyTouchImageView != null) {
                    currentSaveImage.yinjiBitmap = currentMyTouchImageView.getResultBitmap();
                    /*currentSaveImage.x = (int) currentMyTouchImageView.getStartX();
					currentSaveImage.y = (int) currentMyTouchImageView.getStartY();*/
                    currentSaveImage.x = (int) currentMyTouchImageView.x_down;
                    currentSaveImage.y = (int) currentMyTouchImageView.y_down;
                    saveImages.set(currentGalleryPosition, currentSaveImage);

                }
                currentGalleryPosition = position;  //记录下选中的位置，但是不改变currentXX。

                showDialog();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        File file = new File(currentSaveImage.saveImagePath);
                        ImageUtils.saveBitmap(currentShowBitmap, file.getParent().toString(), file.getName().toString());

                        handler.sendEmptyMessage(Constants.SAVEIMAGE);
                    }
                }).start();
            }
        });

    }


    public class HorizontalGalleryAdapter extends BaseAdapter {
        private Context gContext;
        private List<Bitmap> horizontaList = new ArrayList<Bitmap>();
        private ViewHolderGallery viewHolderGallery;
        private LayoutInflater layoutInflater;


        public HorizontalGalleryAdapter(Context c, List<Bitmap> l) {
            // TODO Auto-generated constructor stub
            this.gContext = c;
            this.horizontaList = l;
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return horizontaList.size();
        }


        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return horizontaList.get(position);
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                viewHolderGallery = new ViewHolderGallery();
                convertView = layoutInflater.inflate(R.layout.item_gallery_film, null);
                viewHolderGallery.imageView = (ImageView) convertView.findViewById(R.id.iv_item_gallery_film);
                convertView.setTag(viewHolderGallery);
            } else {
                viewHolderGallery = (ViewHolderGallery) convertView.getTag();
            }

            Bitmap thumbBitmap = horizontaList.get(position);

            //最后一个add按钮
            if (position == horizontaList.size() - 1) {
                viewHolderGallery.imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //保存当前图像，保存ArrayList，传递给SelectPictureActivity
                        if (currentMyTouchImageView != null) {
                            currentSaveImage.yinjiBitmap = currentMyTouchImageView.getResultBitmap();
//							currentSaveImage.x = (int) currentMyTouchImageView.getStartX();
//							currentSaveImage.y = (int) currentMyTouchImageView.getStartY();
                            currentSaveImage.x = (int) currentMyTouchImageView.x_down;
                            currentSaveImage.y = (int) currentMyTouchImageView.y_down;
                            saveImages.set(currentGalleryPosition, currentSaveImage);
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                File file = new File(currentSaveImage.saveImagePath);
                                ImageUtils.saveBitmap(currentShowBitmap, file.getParent().toString(), file.getName().toString());

                                handler.sendEmptyMessage(Constants.ADDIMAGE);
                            }
                        }).start();

                    }
                });
            }
            //其他图像
            LayoutParams layoutParams = viewHolderGallery.imageView.getLayoutParams();
            layoutParams.width = 80;
            layoutParams.height = 100;
            viewHolderGallery.imageView.setLayoutParams(layoutParams);
            viewHolderGallery.imageView.setImageBitmap(thumbBitmap);

            return convertView;
        }


    }


    //加载滤镜
    //滤镜的Adapter和ProcessTask
    @SuppressWarnings("deprecation")
    private void LoadImageFilter() {
        filterAdapter = new ImageFilterAdapter(ActivityFilm.this);
        gallery_filter_film.setAdapter(filterAdapter);
        gallery_filter_film.setSelection(2);
        gallery_filter_film.setSpacing(10);
        gallery_filter_film.setAnimationDuration(3000);
        gallery_filter_film.setUnselectedAlpha(5);
        gallery_filter_film.setOnItemSelectedListener(this);
        gallery_filter_film.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                final IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);

//				new processImageTask(ActivityFilm.this, filter).execute();
                showDialog();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Image image = null;
                        if (currentBitmap != null) {
                            try {
                                image = new Image(currentBitmap);

                            } catch (Exception e) {
                                // TODO: handle exception
                                if (currentBitmap != null && !currentBitmap.isRecycled()) {
                                    currentBitmap.recycle();
                                }

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap tempBitmap = BitmapFactory.decodeFile(currentSaveImage.saveImagePath, options);
                                currentBitmap = ImageUtils.zoomBitmap(tempBitmap, tempBitmap.getWidth() / 2, tempBitmap.getHeight() / 2);
                                image = new Image(currentBitmap);
                            }

                        }

                        if (image != null) {
                            if (filter != null) {
                                image = filter.process(image);
                                image.copyPixelsFromBuffer();

                            }
                        }

                        currentShowBitmap = image.getDestImage();
                        handler.sendEmptyMessage(Constants.IMAGEPROCESS);
                    }
                }).start();
            }
        });
    }


    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;

        public processImageTask(Activity activity, IImageFilter imageFilter) {
            this.filter = imageFilter;
        }

        public Bitmap doInBackground(Void... params) {
            Image image = new Image(currentBitmap);
            try {
                if (image != null) {
                    if (filter != null) {
                        image = filter.process(image);
                        image.copyPixelsFromBuffer();
                    }
                }

                currentShowBitmap = image.getDestImage();
                return currentShowBitmap;
            } catch (Exception e) {
                if (image != null && image.destImage != null && !image.destImage.isRecycled()) {
                    image.destImage.recycle();
                    image.destImage = null;
                    System.gc(); // 提醒系统及时回收
                }
            } finally {
                if (image != null && image.image != null && !image.image.isRecycled()) {
                    image.image.recycle();
                    image.image = null;
                    System.gc(); // 提醒系统及时回收
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                super.onPostExecute(result);

                showBackgroundByMode(result);
            }
        }

    }


    public class ImageFilterAdapter extends BaseAdapter {
        private class FilterInfo {
            public int filterID;
            public IImageFilter filter;
            public String filterName;

            public FilterInfo(int r, IImageFilter filter, String n) {
                this.filterID = r;
                this.filter = filter;
                this.filterName = n;
            }
        }

        private int selectItem;

        private Context mContext;
        private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
        private LayoutInflater layoutInflater;

        public ImageFilterAdapter(Context c) {
            mContext = c;

            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            filterArray.add(new FilterInfo(R.drawable.filter_original,
                    new OriginFilter(), "原图"));
            filterArray.add(new FilterInfo(R.drawable.filter_meibai,
                    new BrightContrastFilter(), "美白"));
            filterArray.add(new FilterInfo(R.drawable.filter_feather,
                    new FeatherFilter(), "淡雅"));
            filterArray.add(new FilterInfo(R.drawable.filter_blue,
                    new HslModifyFilter(60f), "蓝调"));
            filterArray.add(new FilterInfo(R.drawable.filter_qinglv,
                    new HslModifyFilter(100f), "青柠"));
            filterArray.add(new FilterInfo(R.drawable.filter_jiuhong,
                    new HslModifyFilter(230f), "酒红"));
            filterArray.add(new FilterInfo(R.drawable.filter_baohe,
                    new SaturationModifyFilter(), "哥特"));
            filterArray.add(new FilterInfo(R.drawable.filter_light,
                    new LightFilter(), "美食"));

            filterArray.add(new FilterInfo(R.drawable.filter_lomo,
                    new LomoFilter(), "LOMO"));
            filterArray.add(new FilterInfo(R.drawable.filter_blackwhite,
                    new BlackWhiteFilter(), "黑白"));
            filterArray.add(new FilterInfo(R.drawable.filter_film,
                    new FilmFilter(60f), "夜色"));
            filterArray.add(new FilterInfo(R.drawable.filter_sharp,
                    new SharpFilter(), "锐色"));
			
	/*		filterArray.add(new FilterInfo(R.drawable.filter_ice,
					new IceFilter(), filterNames[5]));*/
			
			/*filterArray.add(new FilterInfo(R.drawable.filter_bilv,
					new HslModifyFilter(150f)));//碧绿
			
/*			filterArray.add(new FilterInfo(R.drawable.filter_meiguizi,
					new HslModifyFilter(300f), filterNames[9]));//玫瑰紫
*/			/*
			filterArray.add(new FilterInfo(R.drawable.filter_yellow,
					new ColorToneFilter(0x00FFFF, 192)));// yellow
*/			
			/*filterArray.add(new FilterInfo(R.drawable.filter_purple,
					new SceneFilter(5f, Gradient.Scene1())));// purple
		
/*			filterArray.add(new FilterInfo(R.drawable.filter_golden,
					new PaintBorderFilter(0x00FFFF), filterNames[11]));*/// golden
			/*filterArray.add(new FilterInfo(R.drawable.filter_zise,
					new PaintBorderFilter(0xFF0000)));// blue
*/

        }


        public int getCount() {
            return filterArray.size();
        }


        public Object getItem(int position) {
            return position < filterArray.size() ? filterArray.get(position).filter
                    : null;
        }


        public long getItemId(int position) {
            return position;
        }


        public void setSelectItem(int selectItem) {
            if (this.selectItem != selectItem) {
                this.selectItem = selectItem;
                notifyDataSetChanged();
            }
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                viewHolderGallery = new ViewHolderGallery();

                convertView = layoutInflater.inflate(R.layout.item_gallery_film, null);

                viewHolderGallery.rl_item_gallery = (RelativeLayout) convertView.findViewById(R.id.rl_item_gallery);
                viewHolderGallery.imageView = (ImageView) convertView.findViewById(R.id.iv_item_gallery_film);
                viewHolderGallery.textView = (TextView) convertView.findViewById(R.id.tv_item_gallery_film);
                convertView.setTag(viewHolderGallery);
            } else {
                viewHolderGallery = (ViewHolderGallery) convertView.getTag();
            }

            LayoutParams layoutParams = viewHolderGallery.imageView.getLayoutParams();
            layoutParams.width = 100;
            layoutParams.height = 130;
            viewHolderGallery.imageView.setLayoutParams(layoutParams);
            viewHolderGallery.imageView.setImageResource(filterArray.get(position).filterID);

            viewHolderGallery.textView.setText(filterArray.get(position).filterName);

            if (selectItem == position) {
                viewHolderGallery.rl_item_gallery.setBackgroundColor(getResources().getColor(R.color.title_xiangbin));
            } else {
                viewHolderGallery.rl_item_gallery.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }

            return convertView;
        }

    }

    ;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        filterAdapter.setSelectItem(position);

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }


    //印记的Adapter和ProcessTask
    private void LoadYinjiFilter() {
        for (int i = 0; i < yinjiStrings.length; i++) {
            yinjiList.add(yinjiStrings[i]);
        }
        yinjiFilterAdapter = new YinjiFilterAdapter(ActivityFilm.this, yinjiList);
        gallery_yinji_film.setAdapter(yinjiFilterAdapter);

        gallery_yinji_film.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                //使用当前选中图像的原图
                showBackgroundByMode(currentBitmap);

                if (currentMyTouchImageView != null) {
                    fl_gallery.removeView(currentMyTouchImageView);
                    currentMyTouchImageView = null;
                }
                currentMyTouchImageView = new MyTouchImageView(ActivityFilm.this);

                currentMyTouchImageView.setImageResource(yinjiShowStrings[position]);
                currentMyTouchImageView.setVisibility(View.VISIBLE);
                fl_gallery.addView(currentMyTouchImageView);

            }
        });
    }


    public class YinjiFilterAdapter extends BaseAdapter {
        private Context yContext;
        private List<Integer> yinjiList = new ArrayList<Integer>();

        public YinjiFilterAdapter(Context c, List<Integer> l) {
            // TODO Auto-generated constructor stub
            this.yContext = c;
            this.yinjiList = l;
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return yinjiList.size();
        }


        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return yinjiList.get(position);
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            int currentImage = yinjiList.get(position);
            ImageView imageview = new ImageView(yContext);
            imageview.setImageResource(currentImage);
            imageview.setLayoutParams(new Gallery.LayoutParams(100, 150));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageview;

        }


    }


    public class ViewHolderGallery {
        public RelativeLayout rl_item_gallery;
        public ImageView imageView;
        public TextView textView;
    }


    @SuppressLint("NewApi")
    public void showBackgroundByMode(Bitmap b) {
        LayoutParams params = tv_subtitletext_film.getLayoutParams();
        params.width = b.getWidth();
        params.height = b.getHeight();
		
		/*int screenWidth = ScreenUtils.getScreenWidth(ActivityFilm.this);
		int screenHeight = ScreenUtils.getScreenHeight(ActivityFilm.this);
		
		int otherHeight = DensityUtil.dip2px(ActivityFilm.this, 170);
		int flHeight = screenHeight - otherHeight;
		
		if(b.getWidth() > screenWidth || b.getHeight() > flHeight)
		{
			int widthScale = b.getWidth()/screenWidth;
			int heightScale = b.getHeight()/flHeight;
			if(widthScale > heightScale)
			{
				params.width = screenWidth;
				params.height = b.getHeight()/widthScale;
			}
			else 
			{
				params.height = flHeight;
				params.width = b.getWidth()/heightScale;
			}
			
		}*/
        tv_subtitletext_film.setLayoutParams(params);
        tv_subtitletext_film.setBackground(DataConvertUtil.bitmap2Drawable(b));

    }


    //Seekbar滑动
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        int flag = 0;

        switch (seekBar.getId()) {
            case R.id.seekBarSaturation: // 饱和度
                flag = 0;
                increaseProcessImage.setSaturation(progress);
                break;

            case R.id.seekBarDuibidu: // 色相
                flag = 1;
                increaseProcessImage.SetHue(progress);
                break;

            case R.id.seekBarLight: // 亮度
                flag = 2;
                increaseProcessImage.SetLum(progress);
                break;
        }
        showBackgroundByMode(increaseProcessImage.IncreaseProcessing(currentShowBitmap, flag));

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


    private void saveResultImages(final ArrayList<SaveImage> saveImageList) {
        showDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //先保存currentShowBitmap到currentSaveImage.saveImagePath中，此时所有Bitmap都保存到了SD卡中。
                if (currentMyTouchImageView != null) {
                    currentSaveImage.yinjiBitmap = currentMyTouchImageView.getResultBitmap();
					/*currentSaveImage.x = (int) currentMyTouchImageView.getStartX();
					currentSaveImage.y = (int) currentMyTouchImageView.getStartY();*/
                    currentSaveImage.x = (int) currentMyTouchImageView.x_down;
                    currentSaveImage.y = (int) currentMyTouchImageView.y_down;
                    saveImages.set(currentGalleryPosition, currentSaveImage);

                }
                File file = new File(currentSaveImage.saveImagePath);
                ImageUtils.saveBitmap(currentBitmap, file.getParent().toString(), file.getName().toString());

                checkBitmapRecyled();
                SaveImage itemSaveImage = null;
                Bitmap itemBitmap = null;
                for (int i = 0; i < saveImages.size(); i++) {
                    //取出每个SaveImage，从saveImagePath中取出图像，然后绘制字幕，绘制印记，再保存到Constants.resultPath路径下。
                    itemSaveImage = saveImageList.get(i);
                    itemBitmap = ImageUtils.getBitmapFromPath(itemSaveImage.saveImagePath);

                    //水印图
                    if (itemSaveImage.yinjiBitmap != null) {
                        itemBitmap = ImageUtils.watermarkWithBmp(itemBitmap, itemSaveImage.yinjiBitmap, itemSaveImage.x, itemSaveImage.y);
                    }

                    //带字幕图
                    if (!TextUtils.isEmpty(itemSaveImage.subtitle)) {
                        int bitmapwidth = itemBitmap.getWidth();
                        Paint paint = new Paint();
                        paint.setTextSize(tv_subtitletext_film.getTextSize());
                        int subtitlewidth = (int) paint.measureText(tv_subtitletext_film.getText().toString());

                        int x = (bitmapwidth - subtitlewidth) / 2;
                        int y = itemBitmap.getHeight() - 30;

                        int fontSize = 0;
                        int maxSize = Math.max(itemBitmap.getWidth(), itemBitmap.getHeight());
                        if (maxSize >= 800) {
                            fontSize = 30;
                        } else if (maxSize >= 600) {
                            fontSize = 25;
                        } else if (maxSize >= 400) {
                            fontSize = 20;
                        } else if (maxSize >= 200) {
                            fontSize = 15;
                        } else {
                            fontSize = 12;
                        }

                        itemBitmap = ImageUtils.watermarkWithText(itemBitmap, itemSaveImage.subtitle, x, y, fontSize, R.color.white_deep);
                    }

                    File itemFile = new File(itemSaveImage.saveImagePath);
                    ImageUtils.saveBitmap(itemBitmap, itemFile.getParent().toString(), itemFile.getName().toString());

                    toPostList.add(itemSaveImage.saveImagePath);

                    //清理图片
                    if (itemBitmap != null && !itemBitmap.isRecycled()) {
                        itemBitmap.recycle();
                    }

                }

                handler.sendEmptyMessage(Constants.SAVERESULTIMAGES);

            }
        }).start();
    }


    private void checkBitmapRecyled() {
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            currentBitmap.recycle();
        }
        if (currentShowBitmap != null && !currentShowBitmap.isRecycled()) {
            currentShowBitmap.recycle();
        }
        if (currentYinjiBitmap != null && !currentYinjiBitmap.isRecycled()) {
            currentYinjiBitmap.recycle();
        }
        System.gc();
    }


    /**
     * 显示Dialog
     */
    private void showDialog() {
        if (dialog == null) {
            dialog = AllDialog.createLoadingDialog(this, "正在加载中...");
            dialog.show();
        }
    }


    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.gc();
    }


}
