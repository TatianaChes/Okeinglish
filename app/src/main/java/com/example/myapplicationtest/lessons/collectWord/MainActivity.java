package com.example.myapplicationtest.lessons.collectWord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.customElements.CustomLayout;
import com.example.myapplicationtest.customElements.CustomWord;
import com.example.myapplicationtest.lessons.ActivityNavigation;
import com.example.myapplicationtest.lessons.QuestionDataSource;
import com.example.myapplicationtest.lessons.QuestionModel;
import com.nex3z.flowlayout.FlowLayout;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // составление из спика предложенных слов предложения

    @BindView(R.id.sentence_line)
    FlowLayout sentenceLine;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.answer_container)
    RelativeLayout answerContainer;
    @BindView(R.id.Check)
    RelativeLayout Check;
    @BindView(R.id.close_task)
    ImageView closeTask;
    @BindView(R.id.audio)
    ImageView audio;
    @BindView(R.id.check_button)
    Button checkButton;
    @BindView(R.id.question)
    TextView tvQuestion;
    @BindView(R.id.checked)
    TextView  checked;
    @BindView(R.id.right)
    TextView  right;
    @BindView(R.id.rightAnswer)
    TextView  rightAnswer;
    @BindView(R.id.task_progress_bar)
    ProgressBar progressBar;
    TextToSpeech textToSpeech;
    CustomWord customWord;
    CustomLayout customLayout;
    MediaPlayer mp;
    QuestionModel questionModel;
    QuestionDataSource questionDataSource;
    int progressBarValue;
    ArrayList<String> words = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    Random random = new Random();
    ActivityNavigation activityNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // для инициализации переменных
        Initial();
        initData();
        texttoSpeak(); // воспроизведение

        closeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                texttoSpeak();
            }
        });
    }

    private void Initial() {
        Hawk.init(this).build();
        FirebaseDatabaseHelper.getSoundButton(); // разрешение на звук
        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setSpeechRate(0.7f); // скорость воспроизведения
        questionDataSource = new QuestionDataSource();
        customLayout = new CustomLayout(this);
        customLayout.setGravity(Gravity.CENTER);
        answers = questionDataSource.getAnswer(this);
        initCustomLayout(); // формирование поля со словами
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
    private void texttoSpeak() { // воспроизведение предложения
        String text = questionModel.getQuestion();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    private class TouchListener implements View.OnTouchListener {
        // определение клика по слову
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !customLayout.empty()) {
                customWord = (CustomWord) view;
                customWord.goToViewGroup(customLayout, sentenceLine);
                checkChildCount();
                return true;
            }

            return false;
        }
    }

    private void initData() {
            checkButton.setEnabled(false);
            questionModel = questionDataSource.getRandomQuestionObj(this);
            progressBarValue = 0;
            if (Hawk.get("progressBarValue") != null) {

                progressBarValue = Hawk.get("progressBarValue");

                progressBar.setProgress(progressBarValue);
            }
            tvQuestion.setText(questionModel.getQuestion());
            randomizeCustomWords();
            activityNavigation = ActivityNavigation.getInstance(this);
            checkAnswer(); // метод проверки

        }

    private void initCustomLayout() {
        // построение площадки выбора
        customLayout = new CustomLayout(this);
        customLayout.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        answerContainer.addView(customLayout, params);
    }


    private void checkAnswer() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder answer = new StringBuilder();
                if (checkButton.getText().equals("check")) { // проверка активности кнопки
                    for (int i = 0; i < sentenceLine.getChildCount(); i++) {
                        customWord = (CustomWord) sentenceLine.getChildAt(i);
                        answer.append(customWord.getText().toString() + " ");
                    }
                    if (answer.toString().equals(questionModel.getAnswer() + " ")) {
                        if(Hawk.get("soundButtons").equals(true)) { // проверка на разрешение звука
                            mp = MediaPlayer.create(MainActivity.this, R.raw.ring);
                            mp.start(); // звук правильного варианта
                        }
                        Check.getLayoutParams().height = 155*3;
                        Check.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.green_answer));
                        checked.setText("Правильно");
                        checked.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24, 0, 0, 0);
                        Check.setVisibility(View.VISIBLE);
                      //  Toast.makeText(MainActivity.this, "You Are Correct!", Toast.LENGTH_SHORT).show();
                        progressBarValue += 10;
                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockViews();
                    } else {
                        if(Hawk.get("soundButtons").equals(true)) { // 2 проверка
                            mp = MediaPlayer.create(MainActivity.this, R.raw.error);
                            mp.start(); // звук неверного результата
                        }
                        Check.setVisibility(View.VISIBLE);
                        right.setVisibility(View.VISIBLE);
                        rightAnswer.setVisibility(View.VISIBLE);
                        rightAnswer.setText(questionModel.getAnswer());

                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockViews();
                   }
                    checkButton.setText("continue");
                    checkButton.setBackground(getDrawable(R.drawable.button_task_continue));
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));

                } else if (checkButton.getText().equals("continue")) {

                    if (progressBarValue < 100) {
                        activityNavigation.takeToRandomTask(); // следущее задание

                    } if (progressBarValue == 100) {
                        progressBarValue = 0;
                        activityNavigation.lessonCompleted(); // завершение урока
                        Hawk.put("progressBarValue", progressBarValue);
                    }
                }
            }
        });
    }

    private void checkChildCount() { // изменение состояния кнопки

        if (sentenceLine.getChildCount() > 0) {

            checkButton.setBackground(getDrawable(R.drawable.button_task_continue));
            checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
            checkButton.setEnabled(true);

        } else {

            checkButton.setBackground(getDrawable(R.drawable.button_task_check));
            checkButton.setTextColor(getResources().getColor(R.color.button_task_check));
            checkButton.setEnabled(false);
        }
    }

    private void lockViews() { // измение состояния выбранных слов

        for (int i = 0; i < sentenceLine.getChildCount(); i++) {

            customWord = (CustomWord) sentenceLine.getChildAt(i);
            customWord.setEnabled(false);
        }

        for (int i = 0; i < customLayout.getChildCount(); i++) {

            customWord = (CustomWord) customLayout.getChildAt(i);
            customWord.setEnabled(false);
        }

    }

    private void randomizeCustomWords() {

        String[] wordsFromSentence = questionModel.getAnswer().split(" ");
        Collections.addAll(words, wordsFromSentence);

        if(wordsFromSentence.length <= 3){
            //сколько слов осталось для завершения макета
            int leftSize = 2;
            //Выбор случайного числа из "leftSize" и добавление 2
            int leftRandom = random.nextInt(leftSize) + 2;
            while (words.size() - leftSize < leftRandom) {
                addArrayWords();
            }
        }

        Collections.shuffle(words);
        for (String word : words) {
            CustomWord customWord = new CustomWord(getApplicationContext(), word);
            customWord.setOnTouchListener(new TouchListener());
            customLayout.push(customWord);
        }
    }

    private void addArrayWords() {

        String[] wordsFromAnswerArray =
                answers.get(random.nextInt(answers.size())).split(" ");
        for (int i = 0; i < 2; i++) {
            String word =
                    wordsFromAnswerArray[random.nextInt(wordsFromAnswerArray.length)];
            if (!words.contains(word)) {
                words.add(word);
            }
        }
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
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}