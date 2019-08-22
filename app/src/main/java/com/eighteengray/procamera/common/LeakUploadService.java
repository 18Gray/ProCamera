package com.eighteengray.procamera.common;

import android.util.Log;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;


public class LeakUploadService extends DisplayLeakService {

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        if(result.leakFound || result.excludedLeak){
            return;
        }
        Log.d("LeakCanaryUpload", heapDump.heapDumpFile.toString());
        Log.d("LeakCanaryUpload", leakInfo);
    }

}
