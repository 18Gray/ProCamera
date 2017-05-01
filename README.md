# ProCamera for Android
ProCamera是一款基于Camera2 API的相机，实现了相机的常用功能，力求不断挖掘Camera2的巨大潜力，锻造一款在功能和设计上完美的相机产品。


![camera2](https://github.com/18Gray/ProCamera/blob/master/screenshot/camera2.jpg)
![modeselect](https://github.com/18Gray/ProCamera/blob/master/screenshot/modeselect.jpg)


## 功能
1.相机常用功能：自动对焦/测光，手动对焦/测光，前后摄像头切换，切换闪光灯模式，使用HDR，添加GPU滤镜，拍摄比例调节，延时摄影，录制视频。
2.图像处理相关：点击左下角按钮会进入相册，选取相册后可进行图像处理。包括：裁剪，滤镜，字幕，印记，对比度等的调节。
3.设置：在设置页面，可以设置拍照和录制视频的相关参数。

## 设计
1.在UI设计上，整体采用Material Design，力求界面简洁易懂，不失生动。
2.架构设计上，除了主体采用Camera2的API，还使用了EventBus来进行消息和数据传递。在相册、设置等界面则采用了MVP-Clean的架构，同时引入RxJava作为异步请求的方式，使用Dagger2来为相互依赖解耦。

