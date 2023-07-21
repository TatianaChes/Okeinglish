package com.example.myapplicationtest.lessons.selectPair;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.customElements.CustomWord;
import com.example.myapplicationtest.lessons.ActivityNavigation;
import com.example.myapplicationtest.lessons.QuestionDataSource;
import com.nex3z.flowlayout.FlowLayout;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TapPairActivity extends AppCompatActivity {

// подберите пару

    @BindView(R.id.check_button)
    Button checkButton;
    @BindView(R.id.close_task)
    ImageView closeTask;
    @BindView(R.id.flow_layout)
    FlowLayout flowLayout;
    @BindView(R.id.task_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.Check)
    RelativeLayout Check;
    ArrayList<PairModel> pairs;
    ArrayList<CustomWord> compareWords = new ArrayList<>();
    boolean searchingPair = false;
    CustomWord customWord;
    int progressBarValue, pairsFormed, randomN;
    QuestionDataSource questionDataSource;
    MediaPlayer mp;
    Context context = TapPairActivity.this;
    Random random = new Random();
    ActivityNavigation activityNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_pair);
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

    }

    private void initData() {

        checkButton.setEnabled(false);
        questionDataSource = new QuestionDataSource();
        pairs = questionDataSource.getPairs(this);

        progressBarValue = 0;

        if (Hawk.get("progressBarValue") != null) {

            progressBarValue = Hawk.get("progressBarValue");

            progressBar.setProgress(progressBarValue);
        }
        activityNavigation = ActivityNavigation.getInstance(this);
        randomizePair(); // получение пар слов
        checkButtonListener(); // изменение состояния кнопки
    }
    private void randomizePair() {

        int randomIndex1;
        int randomIndex2;

        Collections.shuffle(pairs);
        randomN = random.nextInt(3) + 4;
        for (int i = 0; i < randomN; i++) {

            PairModel pair = pairs.get(i);
            String pair1 = pair.getPair1();
            String pair2 = pair.getPair2();
            CustomWord customWord1 = new CustomWord(this, pair1);
            CustomWord customWord2 = new CustomWord(this, pair2);
            customWord1.setTag(i);
            customWord2.setTag(i);
            customWord1.setOnTouchListener(new TouchListener());
            customWord2.setOnTouchListener(new TouchListener());

            if (flowLayout.getChildCount() != 0) {

                randomIndex1 = random.nextInt(flowLayout.getChildCount());
                randomIndex2 = random.nextInt(flowLayout.getChildCount());

                flowLayout.addView(customWord1, randomIndex1);
                flowLayout.addView(customWord2, randomIndex2);

            } else {

                flowLayout.addView(customWord1);
                flowLayout.addView(customWord2);
            }
        }
    }
    private class TouchListener implements View.OnTouchListener {
        // отслеживание нажатий на слова
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            customWord = (CustomWord) view;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                if (!isSearchingPair()) {

                    compareWords.add(customWord);

                    view.setBackground(getDrawable(R.drawable.custom_word_selected));

                    setSearchingPair(true);

                } else {

                    CustomWord previousWord = compareWords.get(0);

                    if (previousWord != view) {
                            // иизменение внешнего вида
                        if (previousWord.getTag() == view.getTag()) {

                            previousWord.setEnabled(false);
                            view.setEnabled(false);
                            previousWord.setBackground(getDrawable(R.drawable.custom_word_border));
                            previousWord.setTextColor(getResources().getColor(R.color.grey_button));
                            view.setBackground(getDrawable(R.drawable.custom_word_border));
                            customWord.setTextColor(getResources().getColor(R.color.grey_button));
                            setSearchingPair(false);
                            compareWords.clear();
                            checkCompleteness();

                        } else {

                            previousWord.setBackground(getDrawable(R.drawable.custom_word_border));
                            view.setBackground(getDrawable(R.drawable.custom_word_border));
                            setSearchingPair(false);
                            compareWords.clear();
                        }

                    } else {
                        previousWord.setBackground(getDrawable(R.drawable.custom_word_border));
                        view.setBackground(getDrawable(R.drawable.custom_word_border));
                        setSearchingPair(false);
                        compareWords.clear();
                    }
                }

                return true;
            }

            return false;
        }
    }
    private void checkButtonListener() {

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (progressBarValue < 100) {
                    // следующий урока
                    ActivityNavigation.getInstance(context).takeToRandomTask();

                } if (progressBarValue == 100) {

                    progressBarValue = 0;
                    // конец сессии
                    activityNavigation.lessonCompleted();

                    Hawk.put("progressBarValue", progressBarValue);

                }
            }
        });
    }
    private void checkCompleteness() {
        // когда все слова собраны
        pairsFormed ++;
        if (pairsFormed == randomN) {
            Check.setVisibility(View.VISIBLE);
            if (Hawk.get("soundButtons").equals(true)) { // разрешение на звук
                mp = MediaPlayer.create(TapPairActivity.this, R.raw.ring);
                mp.start(); // правильный ответ
            }

            progressBarValue += 10;

            progressBar.setProgress(progressBarValue);

            Hawk.put("progressBarValue", progressBarValue);

            checkButton.setEnabled(true);
            checkButton.setText("continue");
            checkButton.setTextColor(getResources().getColor(R.color.button_task_continue));
            checkButton.setBackground(getDrawable(R.drawable.button_task_continue));
        }
    }
    public boolean isSearchingPair() {
        return searchingPair;
    }

    public void setSearchingPair(boolean searchingPair) {
        this.searchingPair = searchingPair;
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