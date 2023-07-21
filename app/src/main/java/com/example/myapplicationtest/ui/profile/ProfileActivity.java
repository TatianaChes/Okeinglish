package com.example.myapplicationtest.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.myapplicationtest.input.CheckEnter;
import com.example.myapplicationtest.ui.home.MenuActivity;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.SplashActivity;
import com.example.myapplicationtest.internet.AlertDialogCustom;
import com.example.myapplicationtest.internet.ConnectionDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.hawk.Hawk;
import com.scottyab.aescrypt.AESCrypt;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.security.GeneralSecurityException;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileActivity extends AppCompatActivity {
    BroadcastReceiver mNetworkReceiver;

    @BindView(R.id.change_button)
    Button change;
    @BindView(R.id.exit)
    ImageView exit;
    @BindView(R.id.imgCamera)
    ImageView  imgCamera;
    @BindView(R.id.imgProfile)
    ImageView  imgProfile;
    @BindView(R.id.settings)
    ImageView  imgSettings;

    static AlertDialogCustom md;
    FirebaseAuth firebaseAuth;
    Uri imageUri;

    @BindView(R.id.Name)
    TextView Name;
    @BindView(R.id.Group)
    TextView Group;
    @BindView(R.id.Email)
    TextView Email;
    @BindView(R.id.Password)
    TextView Password;

    String Image, oldPassword, currentEmail;
    String IMAGE_PATH = "/Profile/image_profile.jpg";
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this); // для инициализации переменных
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference(); // получение пути сохранения картинки
        Hawk.init(this).build();
        md = new AlertDialogCustom(this, "Пожалуйста\nпроверьте подключение к интернету...");
        mNetworkReceiver = new ConnectionDetector(); // для проверки интернета
        registerNetworkBroadcastForNougat(); // для запуска окна оповещения
        getUserData(); // для заполнения полей

        change.setOnClickListener(new View.OnClickListener() { // нажатие кнопки изменить
            @Override
            public void onClick(View view) {
                if(CorrectChange() == true) { // если все корректно -> обновляем
                    signUp();
                }
            }
        });

        imgSettings.setOnClickListener(new View.OnClickListener() { // переход в настройки
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                intent.putExtra("login",Email.getText().toString());
                intent.putExtra("password", Password.getText().toString());
                startActivity(intent);
            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                   ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                }
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });

    }

    private boolean CorrectChange() {
        CheckEnter checkEnter = new CheckEnter();
        if (!checkEnter.isStringNull(Name.getText().toString()) &&
                !checkEnter.isStringNull(Group.getText().toString())
                && !checkEnter.isStringNull(Password.getText().toString())) {
            if (checkEnter.checkPassword(Password.getText().toString())) {
                if(checkEnter.isNameIsValid(Name.getText().toString())) {
                    return  true;
                }
                else {
                    Toast.makeText(this, getString(R.string.current_name),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else {
                Toast.makeText(this, getString(R.string.invalid_password),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, getString(R.string.blankEditText),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public static void dialogProfile(boolean value){

        if(value){
            md.build(false);

        }else {
            md.build(true);
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    private void pickImage() {

        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(ProfileActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.getUri();
                Glide.with(this).load(imageUri).into(imgProfile); // загрузка картинки

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception exception = result.getError();
                Toast.makeText(ProfileActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signUp() {
        // добавление картинки на сервер БД
                if(imageUri != null) {
                    storageReference.child(firebaseAuth.getUid() + IMAGE_PATH).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> image = taskSnapshot.getStorage().getDownloadUrl();
                            image.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> imageTask) {

                                    if (imageTask.isSuccessful()) {

                                        String url = imageTask.getResult().toString();
                                        ChangeProfile(url); // запуск метода изменения самих данных
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });
                } else {
                    ChangeProfile(null);
                }
    }

    private void ChangeProfile(String url) {
        // получение текущего пользователя
        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
        if(!oldPassword.equals(Password.getText().toString()) ) { // если паспорт измeнился -> обновляем
            ChangePassword();
        }
        try {
            if (url != null) { // если картинка изменилась(добавилась) -> обновляем
                myRef.child(currentUserID).child("image").setValue(url);
            }
            myRef.child(currentUserID).child("name").setValue(Name.getText().toString());
            myRef.child(currentUserID).child("group").setValue(Group.getText().toString());
            myRef.child(currentUserID).child("email").setValue(Email.getText().toString());
            myRef.child(currentUserID).child("password").setValue(AESCrypt.encrypt(CheckEnter.key,Password.getText().toString()));
            Toast.makeText(this, R.string.update_profile, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    private void ChangePassword() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, oldPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           user.updatePassword(Password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProfileActivity.this, R.string.change_passport, Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                           oldPassword = Password.getText().toString();
                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.error_change_passport, Toast.LENGTH_LONG).show();
                       }
                    }
                });
    }


    public void Exit(){
        new MaterialDialog.Builder(this)
                .title(R.string.exit_title)
                .content(R.string.exit_content)
                .positiveText(R.string.exit_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .negativeText(R.string.exit_negative)
                .show();
    }


    private void getUserData() { // получение данных пользователя
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    try {
                        Image = snapshot.child("image").getValue().toString();
                        Glide.with(ProfileActivity.this).load(Image).into(imgProfile);
                    } catch (Exception e){}

                    try { // расшифровка пароля пользователя
                        Name.setText(snapshot.child("name").getValue().toString());
                        Group.setText(snapshot.child("group").getValue().toString());
                        Email.setText(snapshot.child("email").getValue().toString());
                        Password.setText(AESCrypt.decrypt(CheckEnter.key,
                                snapshot.child("password").getValue().toString()));
                        oldPassword = AESCrypt.decrypt(CheckEnter.key,snapshot.child("password").getValue().toString());
                        currentEmail = snapshot.child("email").getValue().toString();
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

    public void ShowHidePass(View view) { // видимость пароля

        if(view.getId()==R.id.show_pass_btn){
            if(Password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_24);
                //Show Password
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
                //Hide Password
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}