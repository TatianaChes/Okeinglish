package com.example.myapplicationtest.lessons.pictureTranslate;

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
import com.example.myapplicationtest.lessons.selectCard.PictureModel;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureTranslateActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    // перевод картинки

    @BindView(R.id.Check)
    RelativeLayout Check;
    @BindView(R.id.Text)
    TextView tvQuestion;
    @BindView(R.id.checked)
    TextView checked;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.rightAnswer)
    TextView rightAnswer;
    @BindView(R.id.check_button)
    Button checkButton;
    @BindView(R.id.user_answer)
    EditText userAnswer;
    @BindView(R.id.task_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.close_task)
    ImageView closeTask;
    ActivityNavigation activityNavigation;
    QuestionDataSource questionDataSource;
    int progressBarValue;    MediaPlayer mp;
    PictureModel pictureModel;
    Context context = PictureTranslateActivity.this;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_translate);
        ButterKnife.bind(this); // для инициализации переменных
        Initial();
        initData();

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
        questionDataSource = new QuestionDataSource();
        textToSpeech = new TextToSpeech(this, this);
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
        pictureModel = questionDataSource.getPicture(this);
        progressBarValue = 0;

        if (Hawk.get("progressBarValue") != null) {

            progressBarValue = Hawk.get("progressBarValue");

            progressBar.setProgress(progressBarValue);
        }
            // загрузка картинки
        Picasso.with(context).load(pictureModel.getPicture()).into(image);
        tvQuestion.setText(pictureModel.getQuestion());

        activityNavigation = ActivityNavigation.getInstance(this);
        checkAnswer(); // проверка ответа
        enableButton(); // изменение состояния кнопки
    }
    private void checkAnswer() {

    checkButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // полчуние ответа пользователя
            String answer = userAnswer.getText().toString().toLowerCase().replace(" ", "").trim();

            if (checkButton.getText().equals("check")) { // проверка состояния кнопки

                if (pictureModel.getAnswer().toLowerCase(Locale.ROOT).replace(" ","").equals(answer)) {
                    if(Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                        mp = MediaPlayer.create(PictureTranslateActivity.this, R.raw.ring);
                        mp.start(); // првильный ответ
                    }
                    texttoSpeak(answer);
                    Check.getLayoutParams().height = 155*3;
                    Check.setBackgroundColor(ContextCompat.getColor(PictureTranslateActivity.this, R.color.green_answer));
                    checked.setText("Правильно");
                    checked.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24, 0, 0, 0);
                    Check.setVisibility(View.VISIBLE);

                  //  Toast.makeText(context, "You Are Correct!", Toast.LENGTH_SHORT).show();

                    progressBarValue += 10;
                    progressBar.setProgress(progressBarValue);
                    Hawk.put("progressBarValue", progressBarValue);
                    lockEditText();

                } else {
                    if(Hawk.get("soundButtons").equals(true)) { // ращрешение на звук
                        mp = MediaPlayer.create(PictureTranslateActivity.this, R.raw.error);
                        mp.start(); // неправильный ответ
                    }
                    Check.setVisibility(View.VISIBLE);
                    right.setVisibility(View.VISIBLE);
                    rightAnswer.setVisibility(View.VISIBLE);
                    rightAnswer.setText(pictureModel.getAnswer());

                    progressBar.setProgress(progressBarValue);
                    Hawk.put("progressBarValue", progressBarValue);
                    lockEditText();
                }
                checkButton.setText("continue");
                checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
                checkButton.setBackground(getDrawable(R.drawable.button_task_continue));

            } else if (checkButton.getText().equals("continue")) {

                if (progressBarValue < 100) {
                        // следующий урок
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
        // отслеживание состояния поля ввода для изменения кнопки
        userAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 > 0) {

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
}
