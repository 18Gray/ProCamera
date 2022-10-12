package com.eighteengray.procamera.album

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eighteengray.procamera.R
import com.eighteengray.procamera.common.GenerateDataUtils
import com.eighteengray.procamera.widget.dialogfragment.ImageFoldersDialogFragment
import com.eighteengray.procameralibrary.common.Constants
import com.eighteengray.procameralibrary.dataevent.ImageFolderEvent
import com.supaur.baseactivity.baseactivity.BaseActivity
import kotlinx.android.synthetic.main.activity_album.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class AlbumActivity : BaseActivity() {
    lateinit var albumViewModel: AlbumViewModel

    private var isRadio = true
    private var isTakeCamera = false
    private var isShowAdd = false
    var imageFoldersDialogFragment: ImageFoldersDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val bundle = intent.extras
        isRadio = bundle!!.getBoolean(Constants.IS_RADIO)
        isTakeCamera = bundle.getBoolean(Constants.IS_TAKE_CAMERA)
        isShowAdd = bundle.getBoolean(Constants.IS_SHOW_ADD)

        EventBus.getDefault().register(this)

        initView()
    }

    private fun initView() {
        showProgressBar()

        albumViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AlbumViewModel::class.java]
        albumViewModel?.updateViewState(this, isRadio, isTakeCamera, isShowAdd)
        albumViewModel?.albumViewState?.observe(this, Observer {
            hideProgressBar()
            when (it.showPageType) {
                ShowPageType.Error -> showErrorView()
                ShowPageType.Normal -> showData(it.imageFolderList)
            }
        })

    }

    private fun showErrorView() {

    }

    private fun showData(imageFolderList: List<ImageFolder>?) {
        tv_select_album.setOnClickListener {
            var imageFoldersDialogFragment = ImageFoldersDialogFragment()
            val bundle = Bundle()
//            bundle.putSerializable(Constants.IMAGEFOLDERS, imageFolderList)
            imageFoldersDialogFragment.arguments = bundle
            imageFoldersDialogFragment.show(supportFragmentManager, "")
        }

        setAdapterData(imageFolderList!!, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageFolderSelected(imageFolderEvent: ImageFolderEvent) {
        val currentImageFolderNum = imageFolderEvent.currentImageFolderNum
        updateImageFolderList(albumViewModel.albumViewState.value?.imageFolderList!!, currentImageFolderNum)
        setAdapterData(albumViewModel.albumViewState.value?.imageFolderList!!, currentImageFolderNum)
        tv_select_album.text = albumViewModel.albumViewState.value?.imageFolderList!![currentImageFolderNum].name
        if (imageFoldersDialogFragment != null && imageFoldersDialogFragment!!.isVisible) {
            imageFoldersDialogFragment!!.dismiss()
        }
    }

    fun setAdapterData(imageFolderList: List<ImageFolder?>, currentImageFolderNum: Int) {
        if (imageFolderList != null && imageFolderList.isNotEmpty()) {
            val imageItemList: List<ImageItem> = imageFolderList[currentImageFolderNum]?.imagePathList!!
            rl_pics_album.showRecyclerView(
                GenerateDataUtils.generateDataBeanList(1, imageItemList),
                Constants.viewModelPackage
            )
        }
    }

    private fun updateImageFolderList(imageFolders: List<ImageFolder>, currentImageFolderNum: Int) {
        for (i in imageFolders.indices) {
            imageFolders[i].isSelected = i == currentImageFolderNum
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}