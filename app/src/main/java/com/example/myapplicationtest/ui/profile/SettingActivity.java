package com.example.myapplicationtest.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.CheckEnter;
import com.example.myapplicationtest.input.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {
    static Context context;

    @BindView(R.id.soundSwitch)
    Switch SoundSwitch;
    @BindView(R.id.sendSwitch)
    Switch SendSwitch;
    boolean changeTime = false;
    @BindView(R.id.editTextTime)
    EditText editTextTime;
    @BindView(R.id.deleteAcc)
    ImageView deleteAccount;
    @BindView(R.id.Comments)
    EditText  Comment;
    @BindView(R.id.xp)
    EditText  xp;
    @BindView(R.id.save_button)
    Button Save;
    @BindView(R.id.send_button)
    Button Send;
    @BindView(R.id.back)
    ImageView back;
    String login, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this); // для инициализации переменных
        context = getApplicationContext();
        CheckEnter checkEnter = new CheckEnter();
        Initial();
        Hawk.init(this).build();
      //  Toast.makeText(this, new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()).toString(), Toast.LENGTH_LONG).show();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             login = extras.getString("login");
             password = extras.getString("password");

        }
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Question();
            }
        });
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            changeTime = true;
            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // намерение отправки письма
                Intent intent = new Intent(Intent.ACTION_SEND);
                // получатель
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"okeinglish@gmail.com"});
                // тема
                intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                // само сообщение
                intent.putExtra(Intent.EXTRA_TEXT, Comment.getText().toString());
                // установка выбора почтового клиента
                intent.setPackage("com.google.android.gm");
                intent.setType("message/rfc822");

                startActivity(intent);
                Comment.setText("");
               // Toast.makeText(SettingActivity.this, R.string.send_message, Toast.LENGTH_LONG).show();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            boolean sound, send;
            @Override
            public void onClick(View view) {
                if(SoundSwitch.isChecked()){
                    sound = true;
                } else {sound = false;}
                if(SendSwitch.isChecked()){
                    send = true;
                } else {send = false;}


                if(!checkEnter.isStringNull(xp.getText().toString()) &&
                        !checkEnter.isStringNull(editTextTime.getText().toString())){
                    String[] time = editTextTime.getText().toString().split(":");
                        if(Integer.parseInt(time[0]) < 24 && Integer.parseInt(time[0]) >= 0 &&
                           Integer.parseInt(time[1]) >= 0 && Integer.parseInt(time[1]) < 60){
                                if(Integer.parseInt(xp.getText().toString()) >= 10){
                                    // отправка изменений на сервер
                                    FirebaseDatabaseHelper.setSettings(sound, send, editTextTime.getText().toString(),Integer.parseInt(xp.getText().toString()));

                                    Toast.makeText(SettingActivity.this, R.string.change_settings, Toast.LENGTH_LONG).show();
                                    if(changeTime) {
                                        // изменение времени уведомления
                                        Hawk.put("sendingTime", editTextTime.getText().toString());

                                        Intent _intent = new Intent(context, MyReceiver.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
                                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                        //  alarmManager.cancel(pendingIntent);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(System.currentTimeMillis());
                                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                                        calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                                        calendar.set(Calendar.SECOND, 0);
                                        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                                            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                                        }
                                        if (Build.VERSION.SDK_INT >= 23) {
                                            // Wakes up the device in Doze Mode
                                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), // time in millis
                                                    pendingIntent);
                                        } else if (Build.VERSION.SDK_INT >= 19) {
                                            // Wakes up the device in Idle Mode
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        } else {
                                            // Old APIs
                                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, getString(R.string.check_daily), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.check_time), Toast.LENGTH_SHORT).show();
                            }
                     }
                else {
                    Toast.makeText(context, getString(R.string.blankEditText), Toast.LENGTH_SHORT).show();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void Question() {
        new MaterialDialog.Builder(this)
                .title("Оповещение")
                .content("Вы уверенны что хотите удалить аккаунт?")
                .positiveText("Удалить")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users")
                                .child(user.getUid());
                        myRef.removeValue();
                        //  подтверждаем пользователя с помощью логина и пароля
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(login, password);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                                                            startActivity(intent);
                                                            Log.d("result", "User account deleted.");
                                                        }
                                                    }
                                                });

                                    }
                                });

                    }
                })
                .negativeText("Отменить")
                .show();
    }

    private void Initial() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // установка состояний
                    SoundSwitch.setChecked(snapshot.child("soundButtons").getValue(Boolean.class));
                    SendSwitch.setChecked(snapshot.child("sendingMessage").getValue(Boolean.class));
                    editTextTime.setText(snapshot.child("sendingTime").getValue().toString().replace(":", ""));
                    xp.setText(snapshot.child("dailyGoal").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}