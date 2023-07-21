package com.example.myapplicationtest.lessons.enterAudio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.lessons.ActivityNavigation;
import com.example.myapplicationtest.lessons.QuestionDataSource;
import com.example.myapplicationtest.lessons.QuestionModel;
import com.orhanobut.hawk.Hawk;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterAudioActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    @BindView(R.id.Check)
    RelativeLayout Check;
    @BindView(R.id.checked)
    TextView checked;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.rightAnswer)
    TextView rightAnswer;
    @BindView(R.id.check_button)
    Button checkButton;
    @BindView(R.id.image)
    ImageButton audio;
    @BindView(R.id.user_answer)
    EditText userAnswer;
    @BindView(R.id.task_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.close_task)
    ImageView  closeTask;
    ActivityNavigation activityNavigation;
    QuestionDataSource questionDataSource;
    int progressBarValue;    QuestionModel questionModel;
    TextToSpeech textToSpeech;  MediaPlayer mp ;
    Context context = EnterAudioActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_audio);
        ButterKnife.bind(this); // для инициализации переменных
        Initial();
        initData();

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texttoSpeak();
            }
        });

        closeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });
    }
    private void Initial() {
        Hawk.init(this).build();
        FirebaseDatabaseHelper.getSoundButton(); // разрешение на звук
        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setSpeechRate(0.1f); // скорость воспроизведения

    }

    private void initData() {

        checkButton.setEnabled(false);
        questionDataSource = new QuestionDataSource();
        questionModel = questionDataSource.getRandomQuestionObj(this);
        progressBarValue = 0;

        if (Hawk.get("progressBarValue") != null) {

            progressBarValue = Hawk.get("progressBarValue");

            progressBar.setProgress(progressBarValue);
        }

        texttoSpeak(); // воспроизведение
        activityNavigation = ActivityNavigation.getInstance(this);
        checkAnswer(); // проверка
        enableButton(); // изменение состояния кнопки

    }
    private void checkAnswer() {

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // получение пользовательского ответа
                String answer = userAnswer.getText().toString().toLowerCase().replace(" ", "").trim();

                if (checkButton.getText().equals("check")) { // проверка состояния кнопки

                    if (questionModel.getQuestion().toLowerCase(Locale.ROOT).replace(" ","").equals(answer)) {
                        if (Hawk.get("soundButtons").equals(true)) { // разрешение на звук кнокпи
                            mp = MediaPlayer.create(EnterAudioActivity.this,
                              R.raw.ring);
                            mp.start(); // правильный ответ
                        }

                        Check.setBackgroundColor(ContextCompat.getColor(EnterAudioActivity.this, R.color.green_answer));
                        checked.setText("Правильно");
                        checked.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24, 0, 0, 0);
                        right.setVisibility(View.VISIBLE);
                        right.setText("Перевод:");
                        rightAnswer.setVisibility(View.VISIBLE);
                        rightAnswer.setText(questionModel.getAnswer());
                        Check.setVisibility(View.VISIBLE);
                        //  Toast.makeText(context, "You Are Correct!", Toast.LENGTH_SHORT).show();
                        progressBarValue += 10;
                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockEditText();

                    } else {
                        if (Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                            mp = MediaPlayer.create(EnterAudioActivity.this, R.raw.error);
                            mp.start(); // неправильный ответ
                        }
                        Check.setVisibility(View.VISIBLE);
                        right.setVisibility(View.VISIBLE);
                        rightAnswer.setVisibility(View.VISIBLE);
                        rightAnswer.setText(questionModel.getQuestion());

                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockEditText();
                    }
                    checkButton.setText("continue");
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
                    checkButton.setBackground(getDrawable(R.drawable.button_task_continue));

                } else if (checkButton.getText().equals("continue")) {

                    if (progressBarValue < 100) {
                        // следущее задание
                        ActivityNavigation.getInstance(context).takeToRandomTask();

                    } if (progressBarValue == 100) {
                        // конец сессии
                        progressBarValue = 0;
                        activityNavigation.lessonCompleted();
                        Hawk.put("progressBarValue", progressBarValue);
                    }

                }
            }
        });
    }
    private void lockEditText() {
        userAnswer.setEnabled(false);
    }
    private void enableButton() {

        // отслеживание поля ввода для передачи
        userAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 > 0) {
                    // передача информации кнопке
                    checkButton.setBackground(getDrawable(R.drawable.button_task_continue));
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));

                    checkButton.setEnabled(true);

                } else {

                    checkButton.setBackground(getDrawable(R.drawable.button_task_check));
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_check));
                    checkButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.UK);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                texttoSpeak();
            }
        } else {
            Log.e("error", "Failed to Initialize");
        }
    }
    private void texttoSpeak() {
        String text = questionModel.getQuestion();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
