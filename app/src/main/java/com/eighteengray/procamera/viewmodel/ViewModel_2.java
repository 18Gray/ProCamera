package com.eighteengray.procamera.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.eighteengray.cardlibrary.viewmodel.IViewModel;
import com.eighteengray.cardlibrary.widget.BaseRecyclerViewHolder;
import com.eighteengray.procamera.R;
import com.eighteengray.procameralibrary.dataevent.CameraConfigure;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by lutao on 2017/3/24.
 * 纯TextView
 */
public class ViewModel_2 implements IViewModel<String>
{


    @Override
    public View onCreateView(LayoutInflater layoutInflater)
    {
        return layoutInflater.inflate(R.layout.item_text_grid, null);
    }

    @Override
    public void onBindView(Context context, RecyclerView.ViewHolder holder, final String data, int position)
    {
        final BaseRecyclerViewHolder baseRecyclerViewHolder = (BaseRecyclerViewHolder) holder;
        TextView textView = baseRecyclerViewHolder.getViewById(R.id.tv_item_textgrid);
        textView.setText(data);

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CameraConfigure.Scene scene = new CameraConfigure.Scene();
                scene.setScene(data);
                EventBus.getDefault().post(scene);
            }
        });
    }


}
