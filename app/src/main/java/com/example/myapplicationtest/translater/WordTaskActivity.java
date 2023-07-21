package com.example.myapplicationtest.translater;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.lessons.ActivityNavigation;
import com.example.myapplicationtest.lessons.QuestionDataSource;
import com.example.myapplicationtest.lessons.QuestionModel;
import com.example.myapplicationtest.ui.textlist.currentText.DatabaseHelper;
import com.orhanobut.hawk.Hawk;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordTaskActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ToolTipsManager.TipListener{

    // ручной ввод перевода
    @BindView(R.id.Check)
    RelativeLayout Check;
    @BindView(R.id.question)
    TextView tvQuestion;
    @BindView(R.id.checked)
    TextView   checked;
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
    @BindView(R.id.close_task)
    ImageView closeTask;
    @BindView(R.id.audio)
    ImageView  audio;
    MediaPlayer mp;
    QuestionDataSource questionDataSource;
    ActivityNavigation activityNavigation;
    TooltipWindow tipWindow;
    ToolTipsManager toolTipsManager;
    TextToSpeech textToSpeech;
    int progressBarValue, number;
    int unit = 0; int idSentence = 0;
    QuestionModel questionModel;
    Context context = WordTaskActivity.this;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    Cursor cursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_sentence);
        ButterKnife.bind(this);
        Initial();
        initData();
        texttoSpeak();
        idSentence = Hawk.get("idSentense"); // ид предложения


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

        tvQuestion.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


              if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    int  mOffset = tvQuestion.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                  // получить нажатое слово
                  String word = findWordForRightHanded(tvQuestion.getText().toString(), mOffset);

                  // получить перевод слова
                  cursor = mDb.rawQuery("SELECT * FROM translater where LOWER(english)" + " LIKE \"" + word.toLowerCase().trim() + "\" AND unit =" + unit + " AND idSentence =" + idSentence, new String[]{});
                  if (cursor.moveToFirst() && cursor.getCount() == 1) {
                      downloadModal(cursor.getString(4), motionEvent.getRawX(), motionEvent.getRawY());
                  } // если полность совпадает

                  else { // если слово содержится в связке
                      cursor = mDb.rawQuery("SELECT * FROM translater where LOWER(english)" + " LIKE \"%" + word.toLowerCase().trim() + "%\" AND unit =" + unit + " AND idSentence =" + idSentence, new String[]{});
                      if (cursor.moveToFirst() && cursor.getCount() == 1) {
                          downloadModal(cursor.getString(4), motionEvent.getRawX(), motionEvent.getRawY());
                      }

                  }

                  cursor.close();

                }
                return false;
            }
        });

    }
    private String findWordForRightHanded(String str, int offset) {
        if (str.length() == offset) {
            offset--;
        }

        if (str.charAt(offset) == ' ') {
            offset--;
        }
       int startIndex = offset;
       int  endIndex = offset;

        try {
            while (str.charAt(startIndex) != ' ' && str.charAt(startIndex) != '\n') {
                startIndex--;
            }
        } catch (StringIndexOutOfBoundsException e) {
            startIndex = 0;
        }

        try {
            while (str.charAt(endIndex) != ' ' && str.charAt(endIndex) != '\n') {
                endIndex++;
            }
        } catch (StringIndexOutOfBoundsException e) {
            endIndex = str.length();
        }

      // очистка слов
        char last = str.charAt(endIndex - 1);
        if (last == ',' || last == '.' ||
                last == '!' || last == '?' ||
                last == ':' || last == ';') {
            endIndex--;
        }

        return str.substring(startIndex, endIndex);
    }

    private void downloadModal(String word, float rawX, float rawY) {

       if (!tipWindow.isTooltipShown()){
         tipWindow.showToolTip(tvQuestion, rawX,rawY, word.toLowerCase(Locale.ROOT));
          }

    }

    private void Initial() {

        Hawk.init(this).build();

        mDBHelper = new DatabaseHelper(context);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        FirebaseDatabaseHelper.getSoundButton();
        toolTipsManager = new ToolTipsManager(this);
        tipWindow = new TooltipWindow(WordTaskActivity.this);
        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setSpeechRate(0.7f); // скорость воспроизведения
        number =  Hawk.get("number"); // получение урока
        // получение юнита
        if(number >= 1 && number <= 6) {
           unit = 1;
        }
        else if (number >= 7 && number <= 12) {
            unit = 2;
        }
        else if (number >= 13 && number <= 18) {
            unit = 3;
        }
        else if (number>= 19 && number <= 24) {
            unit = 4;
        }
        else if (number >= 25 && number <= 30) {
            unit = 5;
        }
        else if (number >= 31 && number <= 36) {
            unit = 6;
        }
        else if (number >= 37 && number <= 42) {
            unit = 7;
        }
        else if (number >= 43 && number <= 48) {
            unit = 8;
        }
        else if (number >= 49 && number <= 54) {
            unit = 9;
        }
        else if (number >= 55 && number <= 60) {
            unit = 10;
        }
        else if (number >= 61 && number <= 66) {
            unit = 11;
        }
        else if (number >= 67 && number <= 72) {
            unit = 12;
        }
        else if (number >= 73 && number <= 78) {
            unit = 13;
        }
        else if (number >= 79 && number <= 84) {
            unit = 14;
        }

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

            tvQuestion.setText(questionModel.getQuestion());
            activityNavigation = ActivityNavigation.getInstance(this);
            checkAnswer(); // проверка ответа
            enableButton(); // изменение состояния кнопочки
        }



    private void checkAnswer() {

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // получение нужного ответа
                String answer = userAnswer.getText().toString().toLowerCase()
                        .replace(" ", "")
                        .replace("ё","е")
                        .replace("?", "").trim();

                if (checkButton.getText().equals("check")) { // проверка кнопочки

                    if (questionModel.getAnswer().toLowerCase(Locale.ROOT).replace(" ","").replace("?", "").equals(answer)) {
                        if (Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                            mp = MediaPlayer.create(WordTaskActivity.this, R.raw.ring);
                            mp.start(); // верный ответ
                        }
                        Check.getLayoutParams().height = 155*3;
                        Check.setBackgroundColor(ContextCompat.getColor(WordTaskActivity.this, R.color.green_answer));
                        checked.setText("Правильно");
                        checked.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_24, 0, 0, 0);
                        Check.setVisibility(View.VISIBLE);
                        progressBarValue += 10;
                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockEditText();

                    } else {
                        if (Hawk.get("soundButtons").equals(true)) { //разрешение на звук
                            mp = MediaPlayer.create(WordTaskActivity.this, R.raw.error);
                            mp.start(); // неправильный ответ
                        }
                        Check.setVisibility(View.VISIBLE);
                        right.setVisibility(View.VISIBLE);
                        rightAnswer.setVisibility(View.VISIBLE);
                        rightAnswer.setText(questionModel.getAnswer());

                        progressBar.setProgress(progressBarValue);
                        Hawk.put("progressBarValue", progressBarValue);
                        lockEditText();
                    }

                    checkButton.setText("continue");
                    checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
                    checkButton.setBackground(getDrawable(R.drawable.button_task_continue));

                } else if (checkButton.getText().equals("continue")) {

                    if (progressBarValue < 100) {
                        // слудующее задание
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
        // запрет внесения изменений после проверки ответа
        userAnswer.setEnabled(false);
    }
    private void enableButton() {
        // изменение кнопки в зависимости от состояния текстового поля ввода
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

        public  void Exit(){

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
    private void texttoSpeak() { // аудиовоспроизведение речи
        String text = questionModel.getQuestion();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        if(byUser)
        {
          Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
        }
    }
}
