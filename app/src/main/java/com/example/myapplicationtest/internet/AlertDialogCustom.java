package com.example.myapplicationtest.internet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplicationtest.R;
// уведомление об отсутсвии  интернета

public class AlertDialogCustom {
    private Activity mActivity;
    private android.app.AlertDialog dialog;

    public AlertDialogCustom(Activity a, String subject) {

        this.mActivity = a;
        @SuppressLint("ResourceType")
        Animation anim = AnimationUtils.loadAnimation(mActivity, R.animator.rotate); // получение анимации
        LayoutInflater inflater = LayoutInflater.from(mActivity); // место отображения

        View dialoglayout = inflater.inflate(R.layout.custom_dialog, null); // получение размеки
        final ImageView planet = dialoglayout.findViewById(R.id.planet); // картинка с макета
        final TextView title = dialoglayout.findViewById(R.id.TitleDialog);
        title.setText(subject);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity);
        builder.setView(dialoglayout);

        dialog = builder.create(); // построение макета
        // для видимости участков вне макета
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // запуск анимаци для картинки
        planet.startAnimation(anim);
    }
    @SuppressWarnings("InflateParams")
    public void build(boolean visible) {
                // если интернета нет
                if(visible){
                    dialog.show(); // показываем
                // если интернет есть
                } else {
                    dialog.dismiss(); // не показываем(скрываем)
               }
    }
}
