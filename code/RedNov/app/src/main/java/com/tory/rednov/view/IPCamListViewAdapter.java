package com.tory.rednov.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tory.rednov.R;

import java.util.List;

public class IPCamListViewAdapter extends ArrayAdapter<IPCamItem> {
    private int resourceId;

    public IPCamListViewAdapter(Context context, int textViewResourceID, List<IPCamItem> objects) {
        super(context,textViewResourceID, objects);
        resourceId = textViewResourceID;
    }

    //Optimization
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IPCamItem ipCamItem = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.ipCamImg = view.findViewById(R.id.imgIPCamItem);
            viewHolder.ipCamIP = view.findViewById(R.id.tvIPCamIP);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.ipCamImg.setImageResource(ipCamItem.getImageId());
        viewHolder.ipCamIP.setText(ipCamItem.getIp());

        return view;
    }

    class ViewHolder {
        ImageView ipCamImg;
        TextView ipCamIP;
    }

}
