package com.eighteengray.procamera.audio

import android.media.MediaPlayer
import android.os.Bundle
import com.supaur.baseactivity.baseactivity.BaseActivity
import java.io.IOException


class AudioActivity: BaseActivity() {
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer()
        try {
//            mediaPlayer.setDataSource("/sdcard/sendit.mp3");
            val fileDescriptor = assets.openFd("sendit.mp3")
            mediaPlayer!!.setDataSource(
                fileDescriptor.fileDescriptor,
                fileDescriptor.startOffset,
                fileDescriptor.length
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            mediaPlayer!!.seekTo(15)
            mediaPlayer!!.setOnSeekCompleteListener { }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer!!.pause()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.reset()
        mediaPlayer!!.release()
    }

}