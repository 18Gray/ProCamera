package com.eighteengray.procamera.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.eighteengray.commonutillibrary.ImageUtils;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.SaveImage;
import com.eighteengray.procamera.common.Constants;
import com.eighteengray.procamera.widget.AllDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class AlbumActivity extends BaseActivity
{
    private static int MAX_NUM = 8;
    public static final String INTENT_MAX_NUM = "intent_max_num";


    private Context context;
    private GridView gridview;
    private PictureAdapter adapter;

    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();

    private ContentResolver mContentResolver;
    private Button btn_select, btn_ok;
    private ListView listview;
    private FolderAdapter folderAdapter;
    private ImageFloder imageAll, currentImageFolder;

    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private String cameraPath = null;

    ArrayList<SaveImage> saveImages = null;

    private Dialog dialog;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case Constants.OKSELECT:
                    closeDialog();
                    Intent intent = new Intent(AlbumActivity.this, ActivityFilm.class);
                    intent.putExtra("saveImages", saveImages);
                    startActivity(intent);
                    finish();
                    break;

                case Constants.GOCAMERA:
                    Intent intent2 = new Intent(AlbumActivity.this, MainActivity.class);
                    intent2.putExtra("saveImages", saveImages);
                    startActivity(intent2);
                    break;

                case Constants.NOPIC:
                    Toast.makeText(getApplicationContext(), "请还没有选择图片哦", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album);

        MAX_NUM = getIntent().getIntExtra(INTENT_MAX_NUM, 8);
        context = this;
        mContentResolver = getContentResolver();

        saveImages = (ArrayList<SaveImage>) getIntent().getSerializableExtra("saveImages");
        if (saveImages == null)
        {
            saveImages = new ArrayList<SaveImage>();
        }

        initView();
    }


    private void initView()
    {
        imageAll = new ImageFloder();
        imageAll.setDir("/所有图片");
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_ok.setText("完成0/" + MAX_NUM);

        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new PictureAdapter();
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (position == 0)
                {
                    goCamare();
                }
            }
        });

        listview = (ListView) findViewById(R.id.listview);
        folderAdapter = new FolderAdapter();
        listview.setAdapter(folderAdapter);
        listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                currentImageFolder = mDirPaths.get(position);
                Log.d("zyh",
                        position + "-------" + currentImageFolder.getName()
                                + "----" + currentImageFolder.images.size());
                hideListAnimation();
                adapter.notifyDataSetChanged();
                btn_select.setText(currentImageFolder.getName());
            }
        });
        getThumbnail();
    }


    public void select(View v)
    {
        if (listview.getVisibility() == View.VISIBLE)
        {
            hideListAnimation();
        } else
        {
            listview.setVisibility(View.VISIBLE);
            showListAnimation();
            folderAdapter.notifyDataSetChanged();
        }
    }


    public void showListAnimation()
    {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
                0f);
        ta.setDuration(200);
        listview.startAnimation(ta);
    }


    public void hideListAnimation()
    {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
                1f);
        ta.setDuration(200);
        listview.startAnimation(ta);
        ta.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                listview.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 点击完成按钮
     *
     * @param v
     * @version 1.0
     * @author zyh
     */
    public void ok(View v)
    {
        if (selectedPicture.size() <= 0)
        {
            handler.sendEmptyMessage(Constants.NOPIC);
        } else
        {
            showDialog();
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    //保存所有图像到MyCamera路径下和ArrayList，再跳转
                    for (int i = 0; i < selectedPicture.size(); i++)
                    {
                        Bitmap currentBitmap = ImageUtils.getBitmapFromPath(selectedPicture.get(i));
                        if (currentBitmap != null)
                        {
                            Long time = System.currentTimeMillis();
                            String imageName = Long.toString(time) + ".jpg";
                            ImageUtils.saveBitmap(currentBitmap, Constants.path, imageName);
                            SaveImage currentSaveImage = new SaveImage();
                            currentSaveImage.saveImagePath = Constants.path + imageName;
                            currentSaveImage.subtitle = "";
                            currentSaveImage.isZhefu = false;
                            currentSaveImage.yinjiBitmap = null;
                            currentSaveImage.x = 100;
                            currentSaveImage.y = 100;

                            saveImages.add(currentSaveImage);
                        }
                    }


                    handler.sendEmptyMessage(Constants.OKSELECT);
                }
            }).start();
        }


    }


    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    protected void goCamare()
    {
        if (selectedPicture.size() + 1 > MAX_NUM)
        {
            Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                //保存所有图像到MyCamera路径下和ArrayList，再跳转
                for (int i = 0; i < selectedPicture.size(); i++)
                {
                    Bitmap currentBitmap = ImageUtils.getBitmapFromPath(selectedPicture.get(i));
                    if (currentBitmap != null)
                    {
                        Long time = System.currentTimeMillis();
                        String imageName = Long.toString(time) + ".jpg";
                        ImageUtils.saveBitmap(currentBitmap, Constants.path, imageName);
                    }

                    SaveImage currentSaveImage = new SaveImage();
                    currentSaveImage.saveImagePath = selectedPicture.get(i);
                    currentSaveImage.subtitle = "";
                    currentSaveImage.isZhefu = false;
                    currentSaveImage.yinjiBitmap = null;
                    currentSaveImage.x = 100;
                    currentSaveImage.y = 100;

                    saveImages.add(currentSaveImage);
                }

                handler.sendEmptyMessage(Constants.GOCAMERA);
            }
        }).start();

    }


    /**
     * 用于拍照时获取输出的Uri
     *
     * @return
     * @version 1.0
     * @author zyh
     */
    protected Uri getOutputMediaFileUri()
    {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Night");
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && cameraPath != null)
        {
            selectedPicture.add(cameraPath);
            Intent data2 = new Intent();
            data2.putExtra(Constants.INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }


    public void back(View v)
    {
        onBackPressed();
    }


    class PictureAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            ViewHolder holder = null;
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.grid_item_picture,
                        null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.checkBox = (Button) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0)
            {
                holder.iv
                        .setImageResource(R.drawable.pickphotos_to_camera_normal);
                holder.checkBox.setVisibility(View.INVISIBLE);
            } else
            {
                position = position - 1;
                holder.checkBox.setVisibility(View.VISIBLE);
                final ImageItem item = currentImageFolder.images.get(position);
//				loader.displayImage("file://" + item.path, holder.iv, options);
                boolean isSelected = selectedPicture.contains(item.path);
                holder.checkBox.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!v.isSelected()
                                && selectedPicture.size() + 1 > MAX_NUM)
                        {
                            Toast.makeText(context, "最多选择" + MAX_NUM + "张",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (selectedPicture.contains(item.path))
                        {
                            selectedPicture.remove(item.path);
                        } else
                        {
                            selectedPicture.add(item.path);
                        }
                        btn_ok.setEnabled(selectedPicture.size() > 0);
                        btn_ok.setText("完成" + selectedPicture.size() + "/"
                                + MAX_NUM);
                        v.setSelected(selectedPicture.contains(item.path));
                    }
                });
                holder.checkBox.setSelected(isSelected);
            }
            return convertView;
        }
    }


    class ViewHolder
    {
        ImageView iv;
        Button checkBox;
    }


    class FolderAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            FolderViewHolder holder = null;
            if (convertView == null)
            {
                convertView = View.inflate(context, R.layout.list_dir_item,
                        null);
                holder = new FolderViewHolder();
                holder.id_dir_item_image = (ImageView) convertView
                        .findViewById(R.id.id_dir_item_image);
                holder.id_dir_item_name = (TextView) convertView
                        .findViewById(R.id.id_dir_item_name);
                holder.id_dir_item_count = (TextView) convertView
                        .findViewById(R.id.id_dir_item_count);
                holder.choose = (ImageView) convertView
                        .findViewById(R.id.choose);
                convertView.setTag(holder);
            } else
            {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFloder item = mDirPaths.get(position);
            /*loader.displayImage("file://" + item.getFirstImagePath(),
					holder.id_dir_item_image, options);*/
            holder.id_dir_item_count.setText(item.images.size() + "张");
            holder.id_dir_item_name.setText(item.getName());
            holder.choose.setVisibility(currentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }


    class FolderViewHolder
    {
        ImageView id_dir_item_image;
        ImageView choose;
        TextView id_dir_item_name;
        TextView id_dir_item_count;
    }


    /**
     * 得到缩略图
     */
    private void getThumbnail()
    {
        Cursor mCursor = mContentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]
                        {MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        // Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst())
        {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do
            {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                // Log.e("TAG", path);
                imageAll.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                {
                    continue;
                }
                ImageFloder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath))
                {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else
                {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        // for (int i = 0; i < mDirPaths.size(); i++) {
        // ImageFloder f = mDirPaths.get(i);
        // Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        // }
        tmpDir = null;
    }


    class ImageFloder
    {
        /**
         * 图片的文件夹路径
         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir()
        {
            return dir;
        }

        public void setDir(String dir)
        {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath()
        {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath)
        {
            this.firstImagePath = firstImagePath;
        }

        public String getName()
        {
            return name;
        }

    }


    class ImageItem
    {
        String path;

        public ImageItem(String p)
        {
            this.path = p;
        }
    }


    private void showDialog()
    {
        if (dialog == null)
        {
            dialog = AllDialog.createLoadingDialog(this, "正在加载中...");
            dialog.show();
        }
    }


    private void closeDialog()
    {
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }


}
