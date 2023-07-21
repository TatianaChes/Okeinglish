package com.example.myapplicationtest.input;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.ui.home.MenuActivity;
import com.example.myapplicationtest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.hawk.Hawk;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();
        Hawk.init(SplashActivity.this).build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseAuth.getCurrentUser() != null) { // проверка существования пользователя
                    FirebaseDatabaseHelper.getLessonCompleted(); // для заполнения кругов
                    FirebaseDatabaseHelper.getOverallProgress(); // для заполнения процента
                    FirebaseDatabaseHelper.getDailyGoal(); // ежедневная цель
                    FirebaseDatabaseHelper.getUpdateWeek(); // неделя
                    FirebaseDatabaseHelper.getDailyXp(); // текущий прогресс
                    FirebaseDatabaseHelper.ContinuousDay(false); // непрерывные дни
                    FirebaseDatabaseHelper.getLastChild();
                    startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, Welcome.class));
                    finish();
                }
            }
        }, 3000);
    }
}