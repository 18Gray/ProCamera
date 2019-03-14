
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

// C++: class HarrisLaplaceFeatureDetector
//javadoc: HarrisLaplaceFeatureDetector
public class HarrisLaplaceFeatureDetector extends Feature2D {

    protected HarrisLaplaceFeatureDetector(long addr) { super(addr); }


    //
    // C++: static Ptr_HarrisLaplaceFeatureDetector create(int numOctaves = 6, float corn_thresh = 0.01f, float DOG_thresh = 0.01f, int maxCorners = 5000, int num_layers = 4)
    //

    //javadoc: HarrisLaplaceFeatureDetector::create(numOctaves, corn_thresh, DOG_thresh, maxCorners, num_layers)
    public static HarrisLaplaceFeatureDetector create(int numOctaves, float corn_thresh, float DOG_thresh, int maxCorners, int num_layers)
    {
        
        HarrisLaplaceFeatureDetector retVal = new HarrisLaplaceFeatureDetector(create_0(numOctaves, corn_thresh, DOG_thresh, maxCorners, num_layers));
        
        return retVal;
    }

    //javadoc: HarrisLaplaceFeatureDetector::create()
    public static HarrisLaplaceFeatureDetector create()
    {
        
        HarrisLaplaceFeatureDetector retVal = new HarrisLaplaceFeatureDetector(create_1());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_HarrisLaplaceFeatureDetector create(int numOctaves = 6, float corn_thresh = 0.01f, float DOG_thresh = 0.01f, int maxCorners = 5000, int num_layers = 4)
    private static native long create_0(int numOctaves, float corn_thresh, float DOG_thresh, int maxCorners, int num_layers);
    private static native long create_1();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
