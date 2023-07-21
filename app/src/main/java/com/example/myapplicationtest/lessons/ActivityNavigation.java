package com.example.myapplicationtest.lessons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.myapplicationtest.lessons.collectWord.MainActivity;
import com.example.myapplicationtest.lessons.enterAudio.EnterAudioActivity;
import com.example.myapplicationtest.lessons.pictureTranslate.PictureTranslateActivity;
import com.example.myapplicationtest.lessons.selectCard.SelectCardsActivity;
import com.example.myapplicationtest.translater.WordTaskActivity;
import com.example.myapplicationtest.lessons.selectPair.TapPairActivity;

import java.util.ArrayList;
import java.util.Random;

public class ActivityNavigation {
    static ActivityNavigation INSTANCE;

    Context context;
    ArrayList<Class> activities = new ArrayList<>();
    Random random = new Random();

    public ActivityNavigation(Context context) {
        this.context = context;
        initData();
    }
    public static ActivityNavigation getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new ActivityNavigation(context);
        }
        return INSTANCE;
    }

    private void initData() {
        activities.add(EnterAudioActivity.class);
        activities.add(SelectCardsActivity.class);
        activities.add(WordTaskActivity.class);
        activities.add(MainActivity.class);
        activities.add(TapPairActivity.class);
        activities.add(PictureTranslateActivity.class);
    }
    public void takeToRandomTask() { // открытие следующего задания
        int randomIndex = random.nextInt(activities.size());
        Intent intent = new Intent(context, activities.get(randomIndex));
        context.startActivity(intent);
    }
    public void lessonCompleted() { // переход к экрану окончания сессии
        Intent intent = new Intent(context, LessonCompletedActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}

