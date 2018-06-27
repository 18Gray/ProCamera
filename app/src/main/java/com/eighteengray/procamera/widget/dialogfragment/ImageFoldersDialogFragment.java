package com.eighteengray.procamera.widget.dialogfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.eighteengray.cardlibrary.widget.RecyclerLayout;
import com.eighteengray.commonutillibrary.DimenUtil;
import com.eighteengray.procamera.R;
import com.eighteengray.procamera.bean.ImageFolder;
import com.eighteengray.procamera.business.GenerateDataUtils;
import com.eighteengray.procameralibrary.common.Constants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;



public class ImageFoldersDialogFragment extends BaseRecyclerDialogFragment
{
    ArrayList<ImageFolder> imageFolderArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imageFolderArrayList = (ArrayList<ImageFolder>) bundle.getSerializable(Constants.IMAGEFOLDERS);
    }


    @Override
    protected void showRecyclerView()
    {
        super.showRecyclerView();
        recycler_layout.setLayoutManagerNum(1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recycler_layout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = DimenUtil.dp2px(getActivity(), 400);
        recycler_layout.setLayoutParams(layoutParams);
        recycler_layout.showRecyclerView(GenerateDataUtils.generateDataBeanList(4, imageFolderArrayList), Constants.viewModelPackage);
    }
}
