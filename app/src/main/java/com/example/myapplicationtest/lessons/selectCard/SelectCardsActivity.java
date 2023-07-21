package com.example.myapplicationtest.lessons.selectCard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.lessons.QuestionDataSource;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.lessons.ActivityNavigation;
import com.orhanobut.hawk.Hawk;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCardsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.translate_this_sentence)
    TextView tvQuestion;
    @BindView(R.id.checked)
    TextView checked;
    MyPictureAdapter adapter;
    @BindView(R.id.task_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.Check)
    RelativeLayout Check;
    @BindView(R.id.check_button)
    Button checkButton;
    @BindView(R.id.close_task)
    ImageView closeTask;
    String selection;
    Boolean click = true;  MediaPlayer mp;
    QuestionDataSource questionDataSource;
    ActivityNavigation activityNavigation;
    int progressBarValue;
    List<PictureModel> mPictureList;
    TextToSpeech textToSpeech;
    Context context = SelectCardsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_cards);
        ButterKnife.bind(this); // для инициализации переменных
        Initial();
        initData();

        MyPictureAdapter.OnTextClickListener dataClickListener = new MyPictureAdapter.OnTextClickListener() {
            @Override
            public void onTextClick(PictureModel data, int position) {
                if(click) { // для предотвращения многократного нажатия
                    click = false;
                    if (selection.equals(data.getAnswer())) { // если ответ правильный
                        if(Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                            mp = MediaPlayer.create(SelectCardsActivity.this, R.raw.ring);
                            mp.start(); // правильный ответ
                        }
                        texttoSpeak(data.getAnswer()); // воспроизведение правильного ответа
                        Check.setBackgroundColor(ContextCompat.getColor(SelectCardsActivity.this, R.color.green_answer));
                        checked.setText("Правильно");
                        checked.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24, 0, 0, 0);
                        Check.setVisibility(View.VISIBLE);
                        //  Toast.makeText(context, "You Are Correct!", Toast.LENGTH_SHORT).show();
                        progressBarValue += 10;
                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                    } else {
                        if(Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                            mp = MediaPlayer.create(SelectCardsActivity.this, R.raw.error);
                            mp.start(); // неправильный ответ
                        }
                        Check.setVisibility(View.VISIBLE);

                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                    }
                    checkButton.setText("continue");
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
                    checkButton.setBackground(getDrawable(R.drawable.button_task_continue));
                    checkButton.setEnabled(true);
                }
           }
        };

        adapter = new MyPictureAdapter(this, mPictureList, dataClickListener);
        mRecyclerView.setAdapter(adapter);

        closeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });
    }
    private void Initial() {
        Hawk.init(this).build();
        FirebaseDatabaseHelper.getSoundButton(); // получение разрешение на звук кнопок
        textToSpeech = new TextToSpeech(this, this);
        // компоновка картинок
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(SelectCardsActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        questionDataSource = new QuestionDataSource();
        // получение картинок
        mPictureList = questionDataSource.getCards(this);
        Random random = new Random();
        int randomIndex = random.nextInt(mPictureList.size());
        // установка вопроса
        tvQuestion.setText("Choose: " + mPictureList.get(randomIndex).getQuestion());
        // ответ на вопрос
        selection = mPictureList.get(randomIndex).getAnswer();

    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
             //   texttoSpeak(String text);
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }
    private void texttoSpeak(String text) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private void initData() {

        checkButton.setEnabled(false);

        progressBarValue = 0;

        if (Hawk.get("progressBarValue") != null) {

            progressBarValue = Hawk.get("progressBarValue");
            progressBar.setProgress(progressBarValue);
        }

        activityNavigation = ActivityNavigation.getInstance(this);
        checkAnswer(); // проверка ответа
    }
    public void checkAnswer() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             if (checkButton.getText().equals("continue")) {

                    if (progressBarValue < 100) {
                        // следующий урок
                        ActivityNavigation.getInstance(context).takeToRandomTask();

                    } if (progressBarValue == 100) {
                        // завершение сессии
                        progressBarValue = 0;
                        activityNavigation.lessonCompleted();

                        Hawk.put("progressBarValue", progressBarValue);

                    }

                }
            }
        });
    }

    public void Exit(){

        new MaterialDialog.Builder(this)
                .title(R.string.question)
                .content(R.string.question_exit)
                .positiveText(R.string.button_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        progressBarValue = 0;

                        Hawk.put("progressBarValue", progressBarValue);

                        finish();
                    }
                })
                .negativeText(R.string.exit_negative)
                .show();
    }


    @Override
    public void onBackPressed() {
        Exit();

    }

    @Override
    protected void onStop() {

        progressBarValue = 0;

        Hawk.put("progressBarValue", progressBarValue);

        finish();

        super.onStop();
    }
}