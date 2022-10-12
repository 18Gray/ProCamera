package com.eighteengray.procamera.album



data class ImageFolder(
    var folderDir: String? = null,
    var firstImagePath: String? = null,
    var name: String? = null,
    var isSelected: Boolean = false,
    var imagePathList: MutableList<ImageItem> = mutableListOf()
)

data class ImageItem(
    var imagePath: String? = null,
    var resource: Int = 0,
    var showCheckBox: Boolean = false,
    var isChecked: Boolean = false,
)