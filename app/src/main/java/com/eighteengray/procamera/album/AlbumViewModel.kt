package com.eighteengray.procamera.album

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eighteengray.procamera.R
import kotlinx.coroutines.launch
import java.io.File


class AlbumViewModel : ViewModel() {
    private val _albumViewState = MutableLiveData<AlbumViewState>()
    val albumViewState : LiveData<AlbumViewState> = _albumViewState

    fun updateViewState(context: Context, isRadio: Boolean , isTakeCamera: Boolean , isShowAdd: Boolean ) {
        viewModelScope.launch {
            val cursor: Cursor? = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.DATA),
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                arrayOf("image/jpeg", "image/png", "image/jpg"),
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )

            var imageFolderHashMap: MutableMap<String, ImageFolder> = mutableMapOf()
            var imageFolderList: MutableList<ImageFolder> = mutableListOf()

            if (cursor != null && cursor.count > 0) {
                var isSetAllPicFolder = false
                val allPicImageFolder = ImageFolder()
                imageFolderList.add(allPicImageFolder)

                while (cursor.moveToNext()) {
                    val data = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val imagePath = cursor.getString(data)

                    //设置所有图片
                    if (!isSetAllPicFolder) {
                        allPicImageFolder.name = "所有图片"
                        allPicImageFolder.firstImagePath = imagePath
                        allPicImageFolder.isSelected = true
                        if (isTakeCamera) { // 第一个图，相机图
                            val takeCameraItem = ImageItem()
                            takeCameraItem.resource = R.mipmap.take_camera_gray_48dp
                            takeCameraItem.showCheckBox = false
                            takeCameraItem.isChecked = false
                            allPicImageFolder.imagePathList.add(0, takeCameraItem)
                        }
                        if (isShowAdd) { // 最后一个图，添加图
                            val addImageItem = ImageItem()
                            addImageItem.resource = R.mipmap.add_grey_24dp
                            addImageItem.showCheckBox = false
                            addImageItem.isChecked = false
                            allPicImageFolder.imagePathList.add(addImageItem)
                        }
                        isSetAllPicFolder = true
                    }

                    // 设置中间的图片
                    val imageItem = ImageItem()
                    imageItem.imagePath = imagePath
                    if (isRadio) {
                        imageItem.showCheckBox = false
                        imageItem.isChecked = false
                    } else {
                        imageItem.showCheckBox = true
                        imageItem.isChecked = false
                    }
                    allPicImageFolder.imagePathList.add(imageItem)


                    // 设置子文件夹
                    val parentFile = File(imagePath).parentFile
                    var dirPath = parentFile.absolutePath
                    if (TextUtils.isEmpty(dirPath)) {
                        val end = dirPath.lastIndexOf(File.separator)
                        if (end != -1) {
                            dirPath = imagePath.substring(0, end)
                        }
                    } else {  // 生成文件夹
                        var imageFolder: ImageFolder?
                        if (!imageFolderHashMap.containsKey(dirPath)) {
                            imageFolder =ImageFolder()
                            var folderName = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1)
                            if (TextUtils.isEmpty(folderName)) {
                                folderName = "/"
                            }
                            imageFolder.name = folderName
                            imageFolder.folderDir = dirPath
                            imageFolder.firstImagePath = imagePath
                            imageFolder.isSelected = false
                            imageFolderHashMap[dirPath] = imageFolder
                        } else {
                            imageFolder = imageFolderHashMap[dirPath]
                        }
                        // 导入图片地址
                        val imageItem1 = ImageItem()
                        imageItem1.imagePath = imagePath
                        if (isRadio) {
                            imageItem1.showCheckBox = false
                            imageItem1.isChecked = false
                        } else {
                            imageItem1.showCheckBox = true
                            imageItem1.isChecked = false
                        }
                        imageFolder?.imagePathList?.add(imageItem1)
                    }
                }

                // 遍历完成后，需要根据是否显示相机按钮和是否显示添加按钮，对每个文件夹的图像列表，
                // 在开头和结尾添加相机按钮和添加按钮
                imageFolderHashMap.forEach { (t, u) ->
                    var currentFolder = u
                    if (isTakeCamera) { // 第一个图，相机图
                        val takeCameraItem = ImageItem()
                        takeCameraItem.resource = R.mipmap.take_camera_gray_48dp
                        takeCameraItem.showCheckBox = false
                        takeCameraItem.isChecked = false
                        currentFolder.imagePathList.add(0, takeCameraItem)
                    }
                    if (isShowAdd) { // 最后一个图，添加图
                        val addImageItem = ImageItem()
                        addImageItem.resource = R.mipmap.add_grey_24dp
                        addImageItem.showCheckBox = false
                        addImageItem.isChecked = false
                        currentFolder.imagePathList.add(addImageItem)
                    }
                }

                imageFolderList.addAll(imageFolderHashMap.values)
                cursor.close()

                var newAlbumViewState = AlbumViewState(showPageType = ShowPageType.Normal, imageFolderList)
                _albumViewState.postValue(newAlbumViewState)
            }

        }
    }

}


data class AlbumViewState(
    var showPageType: ShowPageType = ShowPageType.Loading,
    var imageFolderList: List<ImageFolder>,
)

sealed class ShowPageType {
    object Loading : ShowPageType()
    object Error : ShowPageType()
    object Normal : ShowPageType()
}