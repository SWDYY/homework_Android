package MyHander;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import com.example.myapplication.R;

public class setDialogHandler extends Handler {
    private Activity showClass;

    public setDialogHandler(Activity self){
        this.showClass=self;
    }

    @Override
    public void handleMessage(Message msg) {
        String s = String.valueOf(msg.obj);
        String tmp[] = s.split(",");
        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(showClass, android.R.style.Theme_NoTitleBar_Fullscreen);
        alertDialog1.setTitle(tmp[0])//标题
                .setMessage(tmp[1])//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();
    }
}
