package com.example.myapplicationtest.study;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplicationtest.databinding.ActivityCurrentStudentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class CurrentStudent extends AppCompatActivity {


   ActivityCurrentStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrentStudentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle arguments = getIntent().getExtras();
        Student student = (Student) arguments.getSerializable(Student.class.getSimpleName());

        DecimalFormat df = new DecimalFormat("#.##");

        binding.FIO.setText(student.getName());
        binding.Group.setText(student.getGroup());
        binding.TextDay.setText(Integer.toString(student.getContinuousDay()));
        if(student.getOverallProgress() > (double)100){
            binding.Present.setText("100%");
        } else {
            binding.Present.setText(df.format(student.getOverallProgress()) + "%");
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentStudent.this, StudyActivity.class);
                startActivity(intent);
            }
        });
        // максимум 60, т.к. в каждом юните 6 уроков
        binding.progress1.setMax(60);
        binding.progress2.setMax(60);
        binding.progress3.setMax(60);
        binding.progress4.setMax(60);
        binding.progress5.setMax(60);
        binding.progress6.setMax(60);
        binding.progress7.setMax(60);
        binding.progress8.setMax(60);
        binding.progress9.setMax(60);
        binding.progress10.setMax(60);
        binding.progress11.setMax(60);
        binding.progress12.setMax(60);
        binding.progress13.setMax(60);
        binding.progress14.setMax(60);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");

        rootRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int unit1 = 0;
                int unit2 = 0;
                int unit3 = 0;
                int unit4 = 0;
                int unit5 = 0;
                int unit6 = 0;
                int unit7 = 0;
                int unit8 = 0;
                int unit9 = 0;
                int unit10 = 0;
                int unit11 = 0;
                int unit12 = 0;
                int unit13 = 0;
                int unit14 = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    try{
                    if (ds.child("name").getValue().equals(student.getName()) && ds.child("group").getValue().equals(student.getGroup())) {

                        DataSnapshot data = ds.child("lessons");
                        if (data.child("lesson1").getValue() != null && data.child("lesson1").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson2").getValue() != null && data.child("lesson2").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson3").getValue() != null && data.child("lesson3").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson4").getValue() != null && data.child("lesson4").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson5").getValue() != null && data.child("lesson5").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson6").getValue() != null && data.child("lesson6").getValue(Integer.class) >= 100)
                            unit1 = unit1 + 10;
                        if (data.child("lesson7").getValue() != null && data.child("lesson7").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson8").getValue() != null && data.child("lesson8").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson9").getValue() != null && data.child("lesson9").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson10").getValue() != null && data.child("lesson10").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson11").getValue() != null && data.child("lesson11").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson12").getValue() != null && data.child("lesson12").getValue(Integer.class) >= 100)
                            unit2 = unit2 + 10;
                        if (data.child("lesson13").getValue() != null && data.child("lesson13").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson14").getValue() != null && data.child("lesson14").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson15").getValue() != null && data.child("lesson15").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson16").getValue() != null && data.child("lesson16").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson17").getValue() != null && data.child("lesson17").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson18").getValue() != null && data.child("lesson18").getValue(Integer.class) >= 100)
                            unit3 = unit3 + 10;
                        if (data.child("lesson19").getValue() != null && data.child("lesson19").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson20").getValue() != null && data.child("lesson20").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson21").getValue() != null && data.child("lesson21").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson22").getValue() != null && data.child("lesson22").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson23").getValue() != null && data.child("lesson23").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson24").getValue() != null && data.child("lesson24").getValue(Integer.class) >= 100)
                            unit4 = unit4 + 10;
                        if (data.child("lesson25").getValue() != null && data.child("lesson25").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson26").getValue() != null && data.child("lesson26").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson27").getValue() != null && data.child("lesson27").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson28").getValue() != null && data.child("lesson28").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson29").getValue() != null && data.child("lesson29").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson30").getValue() != null && data.child("lesson30").getValue(Integer.class) >= 100)
                            unit5 = unit5 + 10;
                        if (data.child("lesson31").getValue() != null && data.child("lesson31").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson32").getValue() != null && data.child("lesson32").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson33").getValue() != null && data.child("lesson33").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson34").getValue() != null && data.child("lesson34").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson35").getValue() != null && data.child("lesson35").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson36").getValue() != null && data.child("lesson36").getValue(Integer.class) >= 100)
                            unit6 = unit6 + 10;
                        if (data.child("lesson37").getValue() != null && data.child("lesson37").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson38").getValue() != null && data.child("lesson38").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson39").getValue() != null && data.child("lesson39").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson40").getValue() != null && data.child("lesson40").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson41").getValue() != null && data.child("lesson41").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson42").getValue() != null && data.child("lesson42").getValue(Integer.class) >= 100)
                            unit7 = unit7 + 10;
                        if (data.child("lesson43").getValue() != null && data.child("lesson43").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson44").getValue() != null && data.child("lesson44").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson45").getValue() != null && data.child("lesson45").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson46").getValue() != null && data.child("lesson46").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson47").getValue() != null && data.child("lesson47").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson48").getValue() != null && data.child("lesson48").getValue(Integer.class) >= 100)
                            unit8 = unit8 + 10;
                        if (data.child("lesson49").getValue() != null && data.child("lesson49").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson50").getValue() != null && data.child("lesson50").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson51").getValue() != null && data.child("lesson51").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson52").getValue() != null && data.child("lesson52").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson53").getValue() != null && data.child("lesson53").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson54").getValue() != null && data.child("lesson54").getValue(Integer.class) >= 100)
                            unit9 = unit9 + 10;
                        if (data.child("lesson55").getValue() != null && data.child("lesson55").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson56").getValue() != null && data.child("lesson56").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson57").getValue() != null && data.child("lesson57").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson58").getValue() != null && data.child("lesson58").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson59").getValue() != null && data.child("lesson59").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson60").getValue() != null && data.child("lesson60").getValue(Integer.class) >= 100)
                            unit10 = unit10 + 10;
                        if (data.child("lesson61").getValue() != null && data.child("lesson61").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson62").getValue() != null && data.child("lesson62").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson63").getValue() != null && data.child("lesson63").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson64").getValue() != null && data.child("lesson64").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson65").getValue() != null && data.child("lesson65").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson66").getValue() != null && data.child("lesson66").getValue(Integer.class) >= 100)
                            unit11 = unit11 + 10;
                        if (data.child("lesson67").getValue() != null && data.child("lesson67").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson68").getValue() != null && data.child("lesson68").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson69").getValue() != null && data.child("lesson69").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson70").getValue() != null && data.child("lesson70").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson71").getValue() != null && data.child("lesson71").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson72").getValue() != null && data.child("lesson72").getValue(Integer.class) >= 100)
                            unit12 = unit12 + 10;
                        if (data.child("lesson73").getValue() != null && data.child("lesson73").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson74").getValue() != null && data.child("lesson74").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson75").getValue() != null && data.child("lesson75").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson76").getValue() != null && data.child("lesson76").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson77").getValue() != null && data.child("lesson77").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson78").getValue() != null && data.child("lesson78").getValue(Integer.class) >= 100)
                            unit13 = unit13 + 10;
                        if (data.child("lesson79").getValue() != null && data.child("lesson79").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;
                        if (data.child("lesson80").getValue() != null && data.child("lesson80").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;
                        if (data.child("lesson81").getValue() != null && data.child("lesson81").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;
                        if (data.child("lesson82").getValue() != null && data.child("lesson82").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;
                        if (data.child("lesson83").getValue() != null && data.child("lesson83").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;
                        if (data.child("lesson84").getValue() != null && data.child("lesson84").getValue(Integer.class) >= 100)
                            unit14 = unit14 + 10;

                    }
                } catch(Exception e){}

                }
                binding.progress1.setProgress(unit1);
                binding.progress2.setProgress(unit2);
                binding.progress3.setProgress(unit3);
                binding.progress4.setProgress(unit4);
                binding.progress5.setProgress(unit5);
                binding.progress6.setProgress(unit6);
                binding.progress7.setProgress(unit7);
                binding.progress8.setProgress(unit8);
                binding.progress9.setProgress(unit9);
                binding.progress10.setProgress(unit10);
                binding.progress11.setProgress(unit11);
                binding.progress12.setProgress(unit12);
                binding.progress13.setProgress(unit13);
                binding.progress14.setProgress(unit14);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}