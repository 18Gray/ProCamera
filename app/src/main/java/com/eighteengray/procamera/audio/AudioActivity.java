package com.eighteengray.procamera.audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.eighteengray.basecomponent.baseactivity.BaseActivity;

import java.io.IOException;


public class AudioActivity extends BaseActivity
{
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        try
        {
//            mediaPlayer.setDataSource("/sdcard/sendit.mp3");
            AssetFileDescriptor fileDescriptor = getAssets().openFd("sendit.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.seekTo(15);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
            {
                @Override
                public void onSeekComplete(MediaPlayer mediaPlayer)
                {

                }
            });

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.reset();
        mediaPlayer.release();
    }
}
