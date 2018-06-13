package com.eighteengray.procameralibrary.gpufilter;


import android.hardware.camera2.CameraMetadata;
import java.util.HashMap;



public class GpuFilterModel
{
    private static HashMap<String, Integer> sceneHashMap;
    private static HashMap<String, Integer> effectHashMap;


    public static Integer getSceneMode(String key)
    {
        if(sceneHashMap == null || sceneHashMap.size() == 0)
        {
            sceneHashMap = new HashMap<>();
            sceneHashMap.put("ACTION", CameraMetadata.CONTROL_SCENE_MODE_ACTION);
            sceneHashMap.put("BARCODE", CameraMetadata.CONTROL_SCENE_MODE_BARCODE);
            sceneHashMap.put("BEACH", CameraMetadata.CONTROL_SCENE_MODE_BEACH);
            sceneHashMap.put("CANDLELIGHT", CameraMetadata.CONTROL_SCENE_MODE_CANDLELIGHT);
            sceneHashMap.put("DISABLED", CameraMetadata.CONTROL_SCENE_MODE_DISABLED);
            sceneHashMap.put("FACE_PRIORITY", CameraMetadata.CONTROL_SCENE_MODE_FACE_PRIORITY);

            sceneHashMap.put("FIREWORKS", CameraMetadata.CONTROL_SCENE_MODE_FIREWORKS);
            sceneHashMap.put("HDR", CameraMetadata.CONTROL_SCENE_MODE_FIREWORKS);
            sceneHashMap.put("LANDSCAPE", CameraMetadata.CONTROL_SCENE_MODE_LANDSCAPE);
            sceneHashMap.put("NIGHT", CameraMetadata.CONTROL_SCENE_MODE_NIGHT);
            sceneHashMap.put("NIGHTPORTRAIT", CameraMetadata.CONTROL_SCENE_MODE_NIGHT_PORTRAIT);
            sceneHashMap.put("PARTY", CameraMetadata.CONTROL_SCENE_MODE_PARTY);
            sceneHashMap.put("PORTRAIT", CameraMetadata.CONTROL_SCENE_MODE_PORTRAIT);
            sceneHashMap.put("SNOW", CameraMetadata.CONTROL_SCENE_MODE_SNOW);
            sceneHashMap.put("SPORTS", CameraMetadata.CONTROL_SCENE_MODE_SPORTS);
            sceneHashMap.put("STEADYPHOTO", CameraMetadata.CONTROL_SCENE_MODE_STEADYPHOTO);
            sceneHashMap.put("SUNSET", CameraMetadata.CONTROL_SCENE_MODE_SUNSET);
            sceneHashMap.put("THEATRE", CameraMetadata.CONTROL_SCENE_MODE_THEATRE);
        }
        return sceneHashMap.get(key);
    }


    public static Integer getEffectMode(String key)
    {
        if(effectHashMap == null || effectHashMap.size() == 0)
        {
            effectHashMap = new HashMap<>();
            effectHashMap.put("AQUA", CameraMetadata.CONTROL_EFFECT_MODE_AQUA);
            effectHashMap.put("BLACKBOARD", CameraMetadata.CONTROL_EFFECT_MODE_BLACKBOARD);
            effectHashMap.put("MONO", CameraMetadata.CONTROL_EFFECT_MODE_MONO);
            effectHashMap.put("NEGATIVE", CameraMetadata.CONTROL_EFFECT_MODE_NEGATIVE);
            effectHashMap.put("POSTERIZE", CameraMetadata.CONTROL_EFFECT_MODE_POSTERIZE);
            effectHashMap.put("SEPIA", CameraMetadata.CONTROL_EFFECT_MODE_SEPIA);
            effectHashMap.put("SOLARIZE", CameraMetadata.CONTROL_EFFECT_MODE_SOLARIZE);
            effectHashMap.put("WHITEBOARD", CameraMetadata.CONTROL_EFFECT_MODE_WHITEBOARD);
            effectHashMap.put("OFF", CameraMetadata.CONTROL_EFFECT_MODE_OFF);
        }
        return effectHashMap.get(key);
    }

}
