package com.eighteengray.cardlibrary.viewmodel;


/**
 * Created by lutao on 2017/9/29.
 */

public class ViewModelFactory
{

    public static IViewModel createViewModel(String viewModelPackage, int viewType){
        IViewModel iViewModel = null;

        String clazzName = viewModelPackage + ".ViewModel_" + viewType;
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(clazzName);
            iViewModel = (IViewModel) clazz.newInstance();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        return iViewModel;
    }

}
