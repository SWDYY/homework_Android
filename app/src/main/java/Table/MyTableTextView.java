package Table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.widget.TextView;

/**
 * 显示带边框的表格
 */
@SuppressLint("AppCompatCustomView")
public class MyTableTextView extends TextView {

    Paint paint = new Paint();

    public MyTableTextView(Context context,int textcolor) {
        super(context);
        paint.setColor(Color.BLACK);// 为边框设置颜色
        this.setTextColor(textcolor);//为文字设置颜色
        this.setHeight(80);
        this.setTextSize(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画TextView的4个边
        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
    }
}