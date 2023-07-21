package com.example.myapplicationtest.translater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.CheckEnter;
import com.example.myapplicationtest.internet.AlertDialogCustom;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplicationtest.ui.textlist.Text;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.tomergoldst.tooltips.ToolTipsManager;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TextItselfActivity extends AppCompatActivity  implements ToolTipsManager.TipListener{

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.title)
    TextView title;

    TooltipWindow tipWindow;
    ToolTipsManager toolTipsManager;
    static Translator englishRussianTranslator;
    Button PlayVideo;
    static AlertDialogCustom md;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        md = new AlertDialogCustom(this, "Пожалуйста,\nдождитесь окончания загрузки языковой модели");

        ModelDownload(); // загрузка языковой модели
        setContentView(R.layout.activity_text_itself);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getExtras();
        Text text = (Text) arguments.getSerializable(Text.class.getSimpleName());
        textView.setText(text.getContent());
        title.setText(text.getName());
        url = text.getUrl();
        toolTipsManager=new ToolTipsManager(this);
        tipWindow = new TooltipWindow(TextItselfActivity.this);
        if(!(url == null || url == "" || url.length() == 0 || url == " ")){
        // отображение кнопочки перехода, если есть ссылка
            PlayVideo = new Button(this);
            PlayVideo.setText("PlayVideo");
            PlayVideo.setTextColor(Color.WHITE);
            PlayVideo.setBackground(ContextCompat.getDrawable(this,R.drawable.button_task_continue));
            PlayVideo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_circle_24, 0,R.drawable.ic_baseline_arrow_forward_24, 0);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(470, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0, 0, 20, 60);
            params.addRule(RelativeLayout.BELOW, R.id.textView);
            PlayVideo.setLayoutParams(params);
            relativeLayout.addView(PlayVideo);

            PlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });

        }

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                  int  mOffset = textView.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                  // получить нажатое слово
                  String word = findWordForRightHanded(textView.getText().toString(), mOffset);
                  // передать для перевода
                  Translater(word,motionEvent.getRawX(), motionEvent.getRawY());
                  // Toast.makeText(TextItselfActivity.this, findWordForRightHanded(textView.getText().toString(), mOffset), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });


    }


    public void ModelDownload() {

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.RUSSIAN)
                        .build();
        englishRussianTranslator =
                Translation.getClient(options);
        RemoteModelManager modelManager = RemoteModelManager.getInstance();
        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();

        if(!englishRussianTranslator.downloadModelIfNeeded(conditions).isSuccessful()){

            md.build(true); // построение окна на время загрузки

            englishRussianTranslator.downloadModelIfNeeded(conditions).
                    addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object v) {
                            md.build(false); // закрытие окна
                            //  Toast.makeText(TextItselfActivity.this, "Модель установлена", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                // если ошибка удаляем, потом скачиваем заново
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Не удалось загрузить модель или другая внутренняя ошибка
                    TranslateRemoteModel russianModel =
                            new TranslateRemoteModel.Builder(TranslateLanguage.RUSSIAN).build();
                    modelManager.deleteDownloadedModel(russianModel)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object v) {
                                    // Model deleted.
                                    Toast.makeText(TextItselfActivity.this, "Model deleted1", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    //     Toast.makeText(TextItselfActivity.this, "Error1", Toast.LENGTH_LONG).show();
                                }
                            });


                    TranslateRemoteModel englishModel =
                            new TranslateRemoteModel.Builder(TranslateLanguage.ENGLISH).build();
                    modelManager.deleteDownloadedModel(englishModel)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object v) {
                                    // Model deleted.
                                    Toast.makeText(TextItselfActivity.this, "Model deleted", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });


                    TranslateRemoteModel enModel =
                            new TranslateRemoteModel.Builder(TranslateLanguage.ENGLISH).build();
                    DownloadConditions conditions = new DownloadConditions.Builder()
                            .build();
                    modelManager.download(enModel, conditions)
                            .addOnSuccessListener(new OnSuccessListener() {

                                @Override
                                public void onSuccess(Object v) {
                           Toast.makeText(TextItselfActivity.this, "Model downloaded1", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // .
                                    //   Toast.makeText(TextItselfActivity.this, "Error1", Toast.LENGTH_LONG).show();
                                }
                            });

                    TranslateRemoteModel rusModel =
                            new TranslateRemoteModel.Builder(TranslateLanguage.RUSSIAN).build();
                    DownloadConditions conditions2 = new DownloadConditions.Builder()
                            .build();
                    modelManager.download(rusModel, conditions2)
                            .addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object v) {

                                    Toast.makeText(TextItselfActivity.this, "Model downloaded2", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //    Toast.makeText(TextItselfActivity.this, "Error2" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
        }
    }

    private void Translater(String word, float rawX, float rawY) {

        englishRussianTranslator.translate(word)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object translatedText) {
                                // Translation successful.
                              if (!tipWindow.isTooltipShown())
                                    tipWindow.showToolTip(textView, rawX,rawY, translatedText.toString());
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                Toast.makeText(TextItselfActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
    }

        // получение выбранного слова
    private String findWordForRightHanded(String str, int offset) {
        if (str.length() == offset) {
            offset--;
        }

        if (str.charAt(offset) == ' ') {
            offset--;
        }
        int startIndex = offset;
        int endIndex = offset;

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

     // получение чистого слова
        char last = str.charAt(endIndex - 1);
        if (last == ',' || last == '.' || last == '«' ||
                last == '!' || last == '?' || last == '»' ||
                last == ':' || last == ';' || last == ')' || last == '(' ) {
            endIndex--;
        }
    try {
         return str.substring(startIndex, endIndex);
        } catch (Exception e){

        }
    return  " ";
}


    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        if(byUser)
        {
            // когда пользователь отклоняет всплывающую подсказку
            // показать тосты
            Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CheckEnter.update = false;
    }
}