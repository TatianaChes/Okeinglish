package com.example.myapplicationtest.input;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationtest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText tvEmail;
    @BindView(R.id.password)
    EditText etPassword;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.back_button)
    ImageView backButton;

    Context context = SignInActivity.this;
    CheckEnter checkEnter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

         checkEnter = new CheckEnter();// для проверки входных данных
         Hawk.init(context).build();
         Hawk.deleteAll(); // очистка хэша
         ButterKnife.bind(this); // для инициализации переменных

         forgotPassword.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                         // строительство диалогового окна в этой активности
                         AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignInActivity.this);
                         //  получение подготовленного окна из файла ресурсов
                         View mView = getLayoutInflater().inflate(R.layout.forgot_password, null);
                         // получение элементов
                         EditText emailReset =  mView.findViewById(R.id.inputEmail);
                         Button mOk =  mView.findViewById(R.id.btnOk);
                         Button mCancel =  mView.findViewById(R.id.btnCancel);
                         // создание
                         mBuilder.setView(mView);
                         final AlertDialog timerDialog = mBuilder.create();
                         //  добавление простушивателей
                         mOk.setOnClickListener(new View.OnClickListener() { // нажатие сохранить
                             @Override
                             public void onClick (View view) {
                                 // проверка на пустоту
                                 if (!checkEnter.isStringNull(emailReset.getText().toString()) && checkEnter.checkEmail(emailReset.getText().toString())) {
                                     FirebaseAuth auth = FirebaseAuth.getInstance();
                                     auth.sendPasswordResetEmail(emailReset.getText().toString())
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()) {
                                                         Toast.makeText(getApplicationContext(), getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                                                         emailReset.setText(""); // очистка поля
                                                     } else {
                                                         Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });

                              } else{
                                   Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                              }
                             }
                         });

                         //  нажатие отмена
                         mCancel.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick (View view) {
                                 timerDialog.dismiss();
                             } // закрытие окна
                         });

                         //  Finally показ Dialog
                         timerDialog.show();
                     }
                 });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, Welcome.class);
                startActivity(intent);
                finish(); // уничтожение и невозможность вернуться назад
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getText().toString();
                String password = etPassword.getText().toString();
               if (!checkEnter.isStringNull(email) || !checkEnter.isStringNull(password)) {
                    if (checkEnter.checkEmail(email) && checkEnter.checkPassword(password)) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithEmailAndPassword(email, password) // аутентификация пользователя
                                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {
                                            Toast.makeText(context, getString(R.string.auth_failed),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            checkChanges(firebaseAuth, password);
                                            checkAdmin(email, password);
                                        }
                                    }
                                });

                    } else if (!checkEnter.checkEmail(email)) {

                        Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();

                    } else if (!checkEnter.checkPassword(password)) {

                        Toast.makeText(context, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(context, getString(R.string.blankEditText), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void checkAdmin(String email, String password) {
        // проверка на администратора
        if(email.equals("okeinglish@gmail.com") && password.equals("@@@nfyz@@@")){
            Intent intent = new Intent(SignInActivity.this, SplashActivity.class);
            Hawk.put("key", "Admin");
            startActivity(intent);
            finish(); // уничтожение и невозможность вернуться назад

        } else {
            Intent intent = new Intent(SignInActivity.this, SplashActivity.class);
            Hawk.put("key", "");
            startActivity(intent);
           finish(); // уничтожение и невозможность вернуться назад
        }
    }

    private void checkChanges(FirebaseAuth firebaseAuth, String password) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    try { // проверка на изменения(сброс) пароля и в его случае перезапись
                        if(!AESCrypt.encrypt(CheckEnter.key,snapshot.child("password").getValue().toString()).equals(password)){
                            try {
                                databaseReference
                                        .child("password")
                                        .setValue(AESCrypt.encrypt(CheckEnter.key,password))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}