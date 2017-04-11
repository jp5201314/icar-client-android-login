package cn.icarowner.icarowner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;

public class DialogCreater {

    public static WaitDialog createProgressDialog(Context context, String str){
       WaitDialog waitDialog = new WaitDialog(context);

        waitDialog.setTitle(str);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            waitDialog.create();
        }
        return  waitDialog;
    }
}
