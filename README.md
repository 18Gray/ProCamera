# ProCamera
ProCamera��һ�����Camera2 API�������ʵ��������ĳ��ù��ܣ����󲻶��ھ�Camera2�ľ޴�Ǳ��������һ���ڹ��ܺ�����������������Ʒ��
![camera2](https://github.com/18Gray/ProCamera/blob/master/screenshot/camera2.jpg)
![modeselect](https://github.com/18Gray/ProCamera/blob/master/screenshot/modeselect.jpg)


## ����
1. ������ù��ܣ��Զ��Խ�/��⣬�ֶ��Խ�/��⣬ǰ������ͷ�л����л������ģʽ��ʹ��HDR�����GPU�˾�������������ڣ���ʱ��Ӱ��¼����Ƶ��
2. ͼ������أ�������½ǰ�ť�������ᣬѡȡ����ɽ���ͼ�����������ü����˾�����Ļ��ӡ�ǣ��Աȶȵȵĵ��ڡ�

## ��ʹ�÷���
1. ��xml������Camera2TextureView����ؼ���
2. ��Activity��Fragment�У�������һ��mFile��·�����Ա���ͼƬ��ַ��
3. ����onResume������cameraTextureView.openCamera()�������
4. ������հ�ť������cameraTextureView.takePicture()����������ա�
5. ��onPause�е���cameraTextureView.closeCamera()�ر������

## ����ʹ�÷���
����ʹ�÷���ʵ�����������ʹ�÷��������������˵�������ơ�ǰ������ͷ�л�������HDR���˾��ȹ��ܡ�
�����ձ���õ�һ��˼·�ǣ���onClick�е����ť�󣬻ᵯ��һ���Ի���Dialog��PopupWindow��Ȼ���ٵ��Dialog��PopupWindow�ϵ�ѡ���֮����Ϣ��ͨ��EventBus����Camera2Fragment�У���Camera2Fragment��ͨ��onXXX����������Ϣ����ִ��cameraTextureView.xxx����ִ����Ӧ���������
�����������Ϊ����
1. ��onClick�е�����PopupWindow��
2. ѡ��PopWindow���ĸ�ѡ�����һ��������iv_flash_auto������������Զ����⣬����ͨ��EventBus������Ϣ��
3. ��Camera2Fragment�е�onFlashSelect���յ���Ϣ���Ƚ���һЩUI�ĸĶ���Ȼ��cameraTextureView.setFlashMode�����������ģʽ��

## Gradle����


## Proguard����


## ��������



# ProCamera
ProCamera is a Camera based on Camera2 API, and it implements some common functions about camera. It will excavate the potential of Camera2 API continuously, to forge an impressive product on function and design.
![camera2](https://github.com/18Gray/ProCamera/blob/master/screenshot/camera2.jpg)
![modeselect](https://github.com/18Gray/ProCamera/blob/master/screenshot/modeselect.jpg)
