package com.example.myapplicationtest.input;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationtest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome extends AppCompatActivity {

    @BindView(R.id.get_started_button)
    Button getStartedButton;
    @BindView(R.id.log_in_link)
    TextView logInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this); // для инициализации переменных

        // переход к входу
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        // переход к регистрации
        logInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}