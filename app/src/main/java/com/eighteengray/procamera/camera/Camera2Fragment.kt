package com.eighteengray.procamera.camera

import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.media.ThumbnailUtils
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.eighteengray.cardlibrary.bean.BaseDataBean
import com.eighteengray.commonutillibrary.FileUtils
import com.eighteengray.commonutillibrary.SDCardUtils
import com.eighteengray.procamera.R
import com.eighteengray.procamera.common.JumpActivityUtils
import com.eighteengray.procamera.widget.TextureViewTouchListener
import com.eighteengray.procamera.widget.dialogfragment.ModeSelectDialogFragment
import com.eighteengray.procamera.widget.dialogfragment.PopupWindowFactory
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.CameraConfigure.*
import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent.ImagePathAvailable
import com.eighteengray.procameralibrary.dataevent.ImageAvailableEvent.ImageReaderAvailable
import kotlinx.android.synthetic.main.fragment_camera2.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


class Camera2Fragment: BaseCameraFragment() {
    var mFile: File? = null
    var textureViewTouchListener: TextureViewTouchListener? = null
    var sceneArrayList: MutableList<String> = mutableListOf()
    var effectArrayList: MutableList<String> = mutableListOf()
    var isHDRVisible = false
    var isEFFECTVisible = false
    private var delayTime = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_camera2, null)

        mFile = FileUtils.createFile(SDCardUtils.getAppFile(getActivity()).absolutePath, "saveImg")

        initView()

        EventBus.getDefault().register(this)
        return view
    }

    override fun onResume() {
        super.onResume()

        cameraTextureView.openCamera()
        textureViewTouchListener = TextureViewTouchListener(cameraTextureView)
        cameraTextureView.setOnTouchListener(textureViewTouchListener)
    }

    private fun initView() {
        rl_scene.showRecyclerView(generateSceneData(), Constants.viewModelPackage)
        rl_effect.showRecyclerView(generateEffectData(), Constants.viewModelPackage)
        seekbar_camera2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    cameraTextureView.changeFocusDistance(progress)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        iv_flash_camera.setOnClickListener {
            tv_mode_gpufileter.visibility = View.GONE
            val location1 = IntArray(2)
            iv_flash_camera.getLocationOnScreen(location1)
            PopupWindowFactory.createFlashPopupWindow(getActivity()).showAtLocation(
                iv_flash_camera,
                Gravity.LEFT or Gravity.TOP,
                location1[0] + iv_flash_camera.width,
                location1[1] - iv_flash_camera.height + 60
            )
        }

        iv_switch_camera.setOnClickListener {
            cameraTextureView.switchCamera()
        }

        tv_scene_camera.setOnClickListener {
            if (rl_effect.visibility == View.VISIBLE) {
                rl_effect.visibility = View.GONE
            }
            if (!isHDRVisible) {
                rl_scene.visibility = View.VISIBLE
                isHDRVisible = true
            } else {
                rl_scene.visibility = View.GONE
                isHDRVisible = false
            }
        }

        tv_mode_select.setOnClickListener {
            val modeSelectDialogFragment = ModeSelectDialogFragment()
            modeSelectDialogFragment.show(requireFragmentManager(), "mode")
        }

        iv_gpufilter_camera.setOnClickListener {
            if (rl_scene.visibility == View.VISIBLE) {
                rl_scene.visibility = View.GONE
            }
            if (!isEFFECTVisible) {
                rl_effect.visibility = View.VISIBLE
                isEFFECTVisible = true
            } else {
                rl_effect.visibility = View.GONE
                isEFFECTVisible = false
            }
        }

        iv_album_camera.setOnClickListener {
            JumpActivityUtils.jump2AlbumActivity(getActivity(), true, true, false)
        }

        iv_ratio_camera.setOnClickListener {
            val location = IntArray(2)
            iv_ratio_camera.getLocationOnScreen(location)
            PopupWindowFactory.createRatioPopupWindow(getActivity()).showAtLocation(iv_ratio_camera, Gravity.BOTTOM, 0, toolbar.measuredHeight + 350)
        }

        iv_shutter_camera.setOnClickListener {
            showViewTakePicture()
            cameraTextureView.takePicture()
        }

        iv_delay_shutter.setOnClickListener {
            when (delayTime) {
                0 -> delayTime = 3
                3 -> delayTime = 10
                10 -> delayTime = 0
            }
            tv_delay_second.setText(delayTime.toString() + "")
            cameraTextureView.setDalayTime((delayTime * 1000).toLong())
        }

    }

    private fun generateSceneData(): List<BaseDataBean<String>>? {
        val list: MutableList<BaseDataBean<String>> = ArrayList()
        sceneArrayList.add("DISABLED")
        sceneArrayList.add("ACTION")
        sceneArrayList.add("BARCODE")
        sceneArrayList.add("BEACH")
        sceneArrayList.add("CANDLELIGHT")
        sceneArrayList.add("FACE_PRIORITY")
        sceneArrayList.add("FIREWORKS")
        sceneArrayList.add("HDR")
        sceneArrayList.add("LANDSCAPE")
        sceneArrayList.add("NIGHT")
        sceneArrayList.add("NIGHTPORTRAIT")
        sceneArrayList.add("PARTY")
        sceneArrayList.add("PORTRAIT")
        sceneArrayList.add("SNOW")
        sceneArrayList.add("SPORTS")
        sceneArrayList.add("STEADYPHOTO")
        sceneArrayList.add("SUNSET")
        sceneArrayList.add("THEATRE")
        for (i in sceneArrayList.indices) {
            val baseDataBean = BaseDataBean(2, sceneArrayList[i])
            list.add(baseDataBean)
        }
        return list
    }

    private fun generateEffectData(): List<BaseDataBean<String>>? {
        val list: MutableList<BaseDataBean<String>> = ArrayList()
        effectArrayList.add("AQUA")
        effectArrayList.add("BLACKBOARD")
        effectArrayList.add("MONO")
        effectArrayList.add("NEGATIVE")
        effectArrayList.add("POSTERIZE")
        effectArrayList.add("SEPIA")
        effectArrayList.add("SOLARIZE")
        effectArrayList.add("WHITEBOARD")
        effectArrayList.add("OFF")
        for (i in effectArrayList.indices) {
            val baseDataBean = BaseDataBean(2, effectArrayList[i])
            list.add(baseDataBean)
        }
        return list
    }

    private fun showViewTakePicture() {
        iv_takepicture_done.visibility = View.VISIBLE
        iv_takepicture_done.visibility = View.GONE
    }

    override fun onPause() {
        if (cameraTextureView != null) {
            cameraTextureView.closeCamera()
        }
        super.onPause()

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    //EventBus--接收相机配置的参数
    @Subscribe(threadMode = ThreadMode.MAIN) //闪光灯设置
    @Throws(CameraAccessException::class)
    fun onFlashSelect(flash: Flash) {
        tv_mode_gpufileter.visibility = View.VISIBLE
        when (flash.flash) {
            Constants.FLASH_AUTO -> iv_flash_camera.setImageResource(R.mipmap.flash_auto_white_24dp)
            Constants.FLASH_ON -> iv_flash_camera.setImageResource(R.mipmap.flash_on_white_24dp)
            Constants.FLASH_OFF -> iv_flash_camera.setImageResource(R.mipmap.flash_off_white_24dp)
            Constants.FLASH_FLARE -> iv_flash_camera.setImageResource(R.mipmap.flash_flare_white_24dp)
        }
        cameraTextureView.setFlashMode(flash.flash)
        tv_mode_gpufileter.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //HDR模式选择
    @Throws(CameraAccessException::class)
    fun onSceneSelect(scene: Scene) {
        cameraTextureView.setSceneMode(scene.scene)
        rl_scene.visibility = View.GONE
        isHDRVisible = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //GPU滤镜选择
    @Throws(CameraAccessException::class)
    fun onEffectSelect(effect: Effect) {
        cameraTextureView.setEffectMode(effect.effect)
        tv_mode_gpufileter.text = effect.effect
        rl_effect.visibility = View.GONE
        isEFFECTVisible = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //拍摄比例调节
    fun onRatioSelect(ratio: Ratio) {
        cameraTextureView.setRatioMode(ratio.ratio)
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //延时拍摄
    fun onDelayTime(delayTime: DelayTime) {
        when (delayTime.delaytime) {
            Constants.DELAY_3 -> {}
            Constants.DELAY_5 -> {}
            Constants.DELAY_8 -> {}
            Constants.DELAY_10 -> {}
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //拍照完成后，拿到ImageReader，然后做保存图片的操作
    fun onImageReaderAvailable(imageReaderAvailable: ImageReaderAvailable) {
        Thread(
            ImageSaver(
                imageReaderAvailable.imageReader,
                getActivity()
            )
        ).start()
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //存储图像完成后，拿到ImagePath图片路径
    fun onImagePathAvailable(imagePathAvailable: ImagePathAvailable) {
        val bitmap = BitmapFactory.decodeFile(imagePathAvailable.imagePath)
        if (bitmap != null) {
            val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 120, 120)
            iv_album_camera.setImageBitmap(thumbnail)
        }
    }

}