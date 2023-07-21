package com.example.myapplicationtest.input;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationtest.FirebaseDatabaseHelper;
import com.example.myapplicationtest.ui.home.MenuActivity;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.ui.profile.MyReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.hawk.Hawk;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.regEmail)
    EditText tvEmail;
    @BindView(R.id.regPassword)
    EditText etPassword;
    @BindView(R.id.regName)
    EditText userName;
    @BindView(R.id.regGroup)
    EditText userGroup;
    @BindView(R.id.registration_in_button)
    Button registrationInButton;
    @BindView(R.id.back_button)
    ImageView backButton;
    String email, password;
    FirebaseAuth firebaseAuth;
    CheckEnter checkEnter;
    Context context = RegistrationActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        checkEnter = new CheckEnter(); // для проверки входных данных
        Hawk.init(this).build(); // построение
        Hawk.deleteAll(); // очистка
        firebaseAuth = FirebaseAuth.getInstance(); // доступ к базе дынных
        ButterKnife.bind(this); // для инициализации переменных

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Welcome.class);
                startActivity(intent);
                finish(); // уничтожение и невозможность вернуться назад
            }
        });

        registrationInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
       email = tvEmail.getText().toString();
       password = etPassword.getText().toString();

        if (!checkEnter.isStringNull(email) && !checkEnter.isStringNull(password)
        && !checkEnter.isStringNull(userName.getText().toString()) &&
                !checkEnter.isStringNull(userGroup.getText().toString())) {
           if (checkEnter.checkEmail(email) && checkEnter.checkPassword(password)) {
                if(checkEnter.isNameIsValid(userName.getText().toString())) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, getString(R.string.account_creation_success), Toast.LENGTH_SHORT).show();
                                createNewUser(); // метод создания пользователя
                                Intent intent = new Intent(context, MenuActivity.class);
                                startActivity(intent);
                                finish(); // уничтожение и невозможность вернуться назад
                            } else {
                                Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(context, getString(R.string.current_name), Toast.LENGTH_SHORT).show();
                }
            }   else if (!checkEnter.checkEmail(email)) {
            Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
        } else if (!checkEnter.checkPassword(password)) {
            Toast.makeText(context, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
        }
    } else {
            Toast.makeText(context, getString(R.string.blankEditText), Toast.LENGTH_SHORT).show();
        }
    }

    private void createNewUser() {
        String name = userName.getText().toString();

        try {
            password = AESCrypt.encrypt(CheckEnter.key,password);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        String group = userGroup.getText().toString();
        String userId = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        UserData user = new UserData(name, email, password, group);
        myRef.child(userId).setValue(user);
        FirebaseDatabaseHelper.getSoundButton(); // пользовательское разрешение на отправку уведомления
        startAlarmBroadcastReceiver(RegistrationActivity.this); // установка времени уведомлений
    }


    public static void startAlarmBroadcastReceiver(Context context) {
        Intent _intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     //   alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,15);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            // Переводит устройство в режим ожидания
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), // time in millis
                    pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            // Выводит устройство из режима ожидания
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            // Старые API
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
     //   alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}