package ButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class jumpFromTo implements View.OnClickListener {
    private Activity from;
    private Class toActivity;
    private String user_name;
    private String belongTo;

    public jumpFromTo(Activity from,Class activity,String user_name,String belongTo) {
        this.user_name=user_name;
        this.belongTo=belongTo;
        this.from=from;
        this.toActivity = activity;
    }

    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // todo
                Intent intent = new Intent();
                //setClass函数的第一个参数是一个Context对象
                //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                intent.setClass(from, toActivity);
                intent.putExtra("user_name", user_name);
                intent.putExtra("belongTo", belongTo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                from.startActivity(intent);
            }
        }).start();
    }
}
