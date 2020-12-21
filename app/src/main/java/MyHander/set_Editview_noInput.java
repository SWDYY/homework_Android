package MyHander;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

public class set_Editview_noInput extends Handler {
    private Activity showClass;
    private EditText editText;

    public set_Editview_noInput(Activity self,EditText editText){
        this.showClass=self;
        this.editText=editText;
    }

    @Override
    public void handleMessage(Message msg) {
        editText.setTextColor(Color.BLUE);
        //设置不可输入
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        editText.setClickable(false);
    }
}
