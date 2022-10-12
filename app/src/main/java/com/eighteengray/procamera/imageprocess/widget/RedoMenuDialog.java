package com.eighteengray.procamera.imageprocess.widget;

import android.os.Bundle;
import android.widget.RelativeLayout;
import com.eighteengray.commonutillibrary.DimenUtil;
import com.eighteengray.procamera.common.GenerateDataUtils;
import com.eighteengray.procamera.imageprocess.bean.HorizontalRecyclerItem;
import com.eighteengray.procamera.widget.dialogfragment.BaseRecyclerDialogFragment;
import com.eighteengray.procameralibrary.common.Constants;
import java.util.ArrayList;
import java.util.List;


public class RedoMenuDialog extends BaseRecyclerDialogFragment {
    List<HorizontalRecyclerItem> horizontalRecyclerItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        horizontalRecyclerItems = GenerateDataUtils.generateRedoMenuList();
    }

    @Override
    protected void showRecyclerView() {
        super.showRecyclerView();
        binding.recyclerLayout.setLayoutManagerNum(1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.recyclerLayout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = DimenUtil.dp2px(getActivity(), 300);
        binding.recyclerLayout.setLayoutParams(layoutParams);
        binding.recyclerLayout.showRecyclerView(GenerateDataUtils.generateDataBeanList(9, horizontalRecyclerItems), Constants.viewModelPackage);
    }

}
