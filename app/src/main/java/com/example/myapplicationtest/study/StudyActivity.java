package com.example.myapplicationtest.study;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplicationtest.R;
import com.example.myapplicationtest.input.SplashActivity;
import com.example.myapplicationtest.ui.home.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudyActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    Spinner spinnerProperty;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.exit)
    ImageView exit;
    @BindView(R.id.recycleStudent)
    RecyclerView recyclerView;

    ArrayList<Student> students = new ArrayList<Student>();
    ListAdapterStudy adapter;
    ListAdapterStudy.OnStudentClickListener studentClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this); // для инициализации переменных


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exit();
            }
        });

// определяем слушателя нажатия элемента в списке
       studentClickListener = new ListAdapterStudy.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student, int position) {
                Intent intent = new Intent(StudyActivity.this, CurrentStudent.class);
                intent.putExtra(Student.class.getSimpleName(), student);
                startActivity(intent);
            }
        };

        setInitialData(); // получение всех пользователей


        spinnerProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selected = adapterView.getItemAtPosition(position).toString();
                if(selected.equals("")){
                    adapter.filterList(students);
                }else {
                    filter(selected);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void Exit() {
        new MaterialDialog.Builder(this)
                .title(R.string.exit_title)
                .content(R.string.exit_content)
                .positiveText(R.string.exit_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(StudyActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .negativeText(R.string.exit_negative)
                .show();
    }

    private void setInitialData(){ // получение и вывод в recycleView
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        rootRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> propertyList = new ArrayList<String>();
                propertyList.add("");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(firebaseAuth.getCurrentUser().getUid())){

                    } else {
                        try {
                            String name = ds.child("name").getValue(String.class);
                            String group = ds.child("group").getValue(String.class);
                            double overallProgress = ds.child("overallProgress").getValue(Double.class);
                            int continuousDay = (int) ds.child("continuousDay").getValue(Integer.class);
                            students.add(new Student(name, group, overallProgress, continuousDay));
                            propertyList.add(group);
                        } catch (Exception e) {
                        }
                    }
            }
                propertyList = propertyList.stream().distinct().collect(Collectors.toList());
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(StudyActivity.this,
                        android.R.layout.simple_spinner_item, propertyList);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProperty.setAdapter(addressAdapter);
                // создаем адаптер
                adapter = new ListAdapterStudy(StudyActivity.this, students, studentClickListener);
                // устанавливаем для списка адаптер
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void filter(String text) {
        //новый список массивов, который будет содержать отфильтрованные данные
        ArrayList<Student> filterdNames = new ArrayList<>();

        //перебор существующих элементов
        for (Student s : students) {
            //если существующие элементы содержат входные данные для поиска
            if (s.getGroup().toLowerCase().contains(text.toLowerCase())) {
                //добавление элемента в отфильтрованный список
                filterdNames.add(s);
            }
        }

        //вызов метода класса adapter и передача отфильтрованного списка
        adapter.filterList(filterdNames);
    }

    @Override
    public void onBackPressed() {
    Intent intent = new Intent(StudyActivity.this, MenuActivity.class);
    startActivity(intent);
    }

}