package com.eighteengray.procamera.card.baserecycler;

import com.eighteengray.procamera.card.viewmodel.IViewModel;
import com.eighteengray.procamera.card.viewmodel.ViewModel_Album;
import com.eighteengray.procamera.card.viewmodel.ViewModel_Effect;
import com.eighteengray.procamera.card.viewmodel.ViewModel_Scene;
import com.eighteengray.procamera.card.viewmodel.ViewModel4;


/**
 * Created by lutao on 2017/9/29.
 */

public class ViewModelFactory
{

    public static IViewModel viewModelFactory(int viewType){
        IViewModel iViewModel = null;
        switch (viewType){
            case 1:
                iViewModel = new ViewModel_Album();
                break;
            case 2:
                iViewModel = new ViewModel_Scene();
                break;
            case 3:
                iViewModel = new ViewModel_Effect();
                break;
            case 4:
                iViewModel = new ViewModel4();
                break;
        }
        return iViewModel;
    }

}
