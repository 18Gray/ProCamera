# ProCamera
ProCamera是一款基于Camera2 API的相机，实现了相机的常用功能，力求不断挖掘Camera2的巨大潜力，锻造一款在功能和设计上完美的相机产品。
![camera2](https://github.com/18Gray/ProCamera/blob/master/screenshot/camera2.jpg)
![modeselect](https://github.com/18Gray/ProCamera/blob/master/screenshot/modeselect.jpg)


## 功能
1. 相机常用功能：自动对焦/测光，手动对焦/测光，前后摄像头切换，切换闪光灯模式，使用HDR，添加GPU滤镜，拍摄比例调节，延时摄影，录制视频。
2. 图像处理相关：点击左下角按钮会进入相册，选取相册后可进行图像处理。包括：裁剪，滤镜，字幕，印记，对比度等的调节。

## 简单使用方法
1. 在xml中引入Camera2TextureView这个控件。
2. 在Activity或Fragment中，先设置一个mFile的路径用以保存图片地址。
3. 在其onResume，调用cameraTextureView.openCamera()打开相机。
4. 点击拍照按钮，调用cameraTextureView.takePicture()就完成了拍照。
5. 在onPause中调用cameraTextureView.closeCamera()关闭相机。

## 复杂使用方法
复杂使用方法实际是在上面简单使用方法基础上增加了调节闪光灯、前后摄像头切换、设置HDR、滤镜等功能。
这里普遍采用的一个思路是：在onClick中点击按钮后，会弹出一个对话框Dialog或PopupWindow，然后再点击Dialog或PopupWindow上的选择项，之后消息会通过EventBus传到Camera2Fragment中，在Camera2Fragment中通过onXXX方法接收消息，再执行cameraTextureView.xxx方法执行相应相机操作。
以闪光灯设置为例：
1. 在onClick中弹出了PopupWindow。
2. 选择PopWindow中四个选择项的一个，例如iv_flash_auto这个，是设置自动闪光，于是通过EventBus发送消息。
3. 在Camera2Fragment中的onFlashSelect接收到消息，先进行一些UI的改动，然后cameraTextureView.setFlashMode来设置闪光灯模式。

## Gradle导入


## Proguard混淆


## 其他问题



# ProCamera
ProCamera is a Camera based on Camera2 API, and it implements some common functions about camera. It will excavate the potential of Camera2 API continuously, to forge an impressive product on function and design.
![camera2](https://github.com/18Gray/ProCamera/blob/master/screenshot/camera2.jpg)
![modeselect](https://github.com/18Gray/ProCamera/blob/master/screenshot/modeselect.jpg)
