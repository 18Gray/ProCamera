package com.eighteengray.procameralibrary.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder surfaceHolder;

    public CustomSurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(111111);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
