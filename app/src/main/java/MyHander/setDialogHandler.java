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
        System.out.println("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        String tmp[] = s.split(",");
        AlertDialog alertDialog1 = new AlertDialog.Builder(showClass,R.style.myFullscreenAlertDialogStyle)
                .setTitle(tmp[0])//标题
                .setMessage(tmp[1])//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();
        alertDialog1.getWindow().setLayout(900,600);
    }
}
