package MyHander;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

public class set_Editview_canInput extends Handler{
    private Activity showClass;
    private EditText editText;

    public set_Editview_canInput(Activity self,EditText editText){
        this.showClass=self;
        this.editText=editText;
    }

    @Override
    public void handleMessage(Message msg) {
        editText.setTextColor(Color.BLACK);
        //设置不可输入
        editText.setText("");
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
        editText.setClickable(true);
    }
}
