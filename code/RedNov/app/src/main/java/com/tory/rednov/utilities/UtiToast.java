package com.tory.rednov.utilities;

import android.content.Context;
import android.widget.Toast;

public class UtiToast {
    private static Context mContext = null;
    public static void setContext(Context context){
        mContext = context;
    }

    static public void Toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
