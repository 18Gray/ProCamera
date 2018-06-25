package com.eighteengray.procamera.imageprocess.widget;

import android.os.Bundle;
import com.eighteengray.procamera.business.GenerateDataUtils;
import com.eighteengray.procamera.imageprocess.bean.HorizontalRecyclerItem;
import com.eighteengray.procamera.widget.dialogfragment.BaseRecyclerDialogFragment;
import com.eighteengray.procameralibrary.common.Constants;
import java.util.ArrayList;
import java.util.List;


public class OutputMenuDialog extends BaseRecyclerDialogFragment
{
    List<HorizontalRecyclerItem> horizontalRecyclerItems = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        horizontalRecyclerItems = GenerateDataUtils.generateOutputMenuList();
    }

    @Override
    protected void showRecyclerView()
    {
        super.showRecyclerView();
        recycler_layout.setLayoutManagerNum(1);
        recycler_layout.showRecyclerView(GenerateDataUtils.generateDataBeanList(9, horizontalRecyclerItems), Constants.viewModelPackage);
    }


}
