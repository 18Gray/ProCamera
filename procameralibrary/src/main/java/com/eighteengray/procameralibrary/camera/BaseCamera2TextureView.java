package com.eighteengray.procameralibrary.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;
import com.eighteengray.procameralibrary.permission.DefaultRationale;
import com.eighteengray.procameralibrary.permission.PermissionSetting;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import java.util.List;
import java.util.concurrent.Semaphore;



public abstract class BaseCamera2TextureView extends TextureView
{
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public Context context;
    public WindowManager windowManager;
    public HandlerThread mBackgroundThread;
    public Handler mBackgroundHandler;
    public Handler mMainHandler;

    public String mCameraId;
    public int cameraNum = 0;
    public Size mPreviewSize;
    public Semaphore mCameraOpenCloseLock = new Semaphore(1);
    public int mSensorOrientation;

    protected CameraManager manager;
    protected CameraDevice mCameraDevice;
    protected CameraCaptureSession mCaptureSession;
    protected Surface surface;
    protected CameraCharacteristics mCameraCharacteristics;

    // 权限管理
    private Rationale mRationale;
    private PermissionSetting permissionSetting;


    //TextureView的状态监听，TextureView好了之后，打开相机
    protected final SurfaceTextureListener mSurfaceTextureListener = new SurfaceTextureListener()
    {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height)
        {
            openCameraReal(width, height, 0);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height)
        {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture)
        {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture)
        {
        }

    };



    //******************************************************************************************
    //  初始化方法
    //********************************************************************************************

    public BaseCamera2TextureView(Context context)
    {
        this(context, null);
        init(context);
    }

    public BaseCamera2TextureView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        init(context);
    }

    public BaseCamera2TextureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context c)
    {
        this.context = c;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        mRationale = new DefaultRationale();
        permissionSetting = new PermissionSetting(context);
    }

    protected void setAspectRatio(int width, int height)
    {
        if (width < 0 || height < 0)
        {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight)
        {
            setMeasuredDimension(width, height);
        } else
        {
            if (width < height * mRatioWidth / mRatioHeight)
            {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else
            {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }


    //******************************************************************************************
    //  public 方法，供外部调用
    //********************************************************************************************

    public void openCamera()
    {
        startBackgroundThread();
        if (isAvailable())
        {
            openCameraReal(getWidth(), getHeight(), cameraNum);
        } else
        {
            setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void closeCamera()
    {
        closeCameraReal();
        stopBackgroundThread();
    }

    public void switchCamera()
    {
        if(cameraNum == 0)
        {
            cameraNum = 1;
        }else if(cameraNum == 1)
        {
            cameraNum = 0;
        }
        closeCameraReal();
        openCameraReal(getWidth(), getHeight(), cameraNum);
    }


    //******************************************************************************************
    //  private 方法，内部调用
    //********************************************************************************************
    //后台线程
    private void startBackgroundThread()
    {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mMainHandler = new Handler(context.getMainLooper());
    }

    private void stopBackgroundThread()
    {
        if(mBackgroundHandler != null)
        {
            mBackgroundThread.quitSafely();
            try
            {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    //打开相机，预览
    private void openCameraReal(int width, int height, final int cameraNum)
    {
        configureCamera(width, height, cameraNum);
        configureTransform(width, height);

        AndPermission.with(context)
                .permission(new String[]{Permission.CAMERA, Permission.RECORD_AUDIO, Permission.WRITE_EXTERNAL_STORAGE})
                .rationale(new DefaultRationale())
                .onGranted(new Action() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onAction(List<String> permissions) {
                        try
                        {
                            manager.openCamera(mCameraId, deviceStateCallback, mBackgroundHandler);
                        } catch (CameraAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            new PermissionSetting(context).showSetting(permissions);
                        }
                    }
                })
                .start();
    }

    //监听，相机打开好后，进入预览
    protected final CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback()
    {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice)
        {
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error)
        {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

    };

    protected void initSurface()
    {
        SurfaceTexture texture = getSurfaceTexture();
        assert texture != null;
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        surface = new Surface(texture);
    }

    protected void closePreviewSession()
    {
        if (mCaptureSession != null)
        {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    protected void updatePreview(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureSessionCaptureCallback)
    {
        try
        {
            if(mCaptureSession != null && mBackgroundHandler != null){
                mCaptureSession.setRepeatingRequest(captureRequest, captureSessionCaptureCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }



    //abstract方法
    public abstract void configureCamera(int width, int height, int cameraNum);
    public abstract void configureTransform(int width, int height);
    public abstract void createCameraPreviewSession();
    public abstract void closeCameraReal();
}
