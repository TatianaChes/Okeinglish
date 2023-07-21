package com.example.myapplicationtest.translater;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplicationtest.R;

public class TooltipWindow {
    static final int MSG_DISMISS_TOOLTIP = 100;
    Context ctx;
    TextView tooltipText;
    PopupWindow tipWindow;
    View contentView;
    LayoutInflater inflater;
    public TooltipWindow(Context ctx) {
        this.ctx = ctx;
        tipWindow = new PopupWindow(ctx);
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        contentView = inflater.inflate(R.layout.custom_toast, null);
        tooltipText = contentView.findViewById(R.id.tooltip_text);
    }
    void showToolTip(View anchor, float x, float y, String word) {
        tooltipText.setText(word);
        tipWindow.setHeight(LayoutParams.WRAP_CONTENT);
        tipWindow.setWidth(LayoutParams.WRAP_CONTENT);
        tipWindow.setOutsideTouchable(true);
        tipWindow.setTouchable(true);
        tipWindow.setFocusable(true);
        tipWindow.setBackgroundDrawable(new BitmapDrawable());
        tipWindow.setContentView(contentView);
        int screen_pos[] = new int[2];
        // Получить местоположение  вида на экране
        anchor.getLocationOnScreen(screen_pos);
      // Получить прямоугольник для просмотра привязки
      //   Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0]
      //           + anchor.getWidth(), screen_pos[1] + anchor.getHeight());
      // Вызов view measure, чтобы рассчитать, насколько большим должен быть вид
        contentView.measure(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
      //  int contentViewHeight = contentView.getMeasuredHeight();
        int contentViewWidth = contentView.getMeasuredWidth();

        int position_x = (int) (x - (contentViewWidth / 2.2));
      //  int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);
        tipWindow.showAtLocation(anchor, Gravity.NO_GRAVITY,position_x, (int)y);
        // отправить сообщение обработчику, чтобы закрыть tipWindow через X миллисекунд
        handler.sendEmptyMessageDelayed(MSG_DISMISS_TOOLTIP, 4000);
    }
    boolean isTooltipShown() {
        if(tipWindow != null && tipWindow.isShowing())
            return true;
        return false;
    }
    void dismissTooltip() {
        if (tipWindow != null && tipWindow.isShowing())
            tipWindow.dismiss();
    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_TOOLTIP:
                    if (tipWindow != null && tipWindow.isShowing())
                        tipWindow.dismiss();
                    break;
            }
        }
    };
}
