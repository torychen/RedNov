package com.tory.rednov.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tory.rednov.R;

public class DiscoveryDialogFragment extends DialogFragment {
    // 加载动画
    AnimationDrawable loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.fragment_discoverying, container, false);// 得到加载view
        ImageView loadingImg = v.findViewById(R.id.loading_img);
        loading = (AnimationDrawable) loadingImg.getBackground();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loading != null && !loading.isRunning()) {
            loading.start();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (loading != null && loading.isRunning()) {
            loading.stop();
        }
    }
}
