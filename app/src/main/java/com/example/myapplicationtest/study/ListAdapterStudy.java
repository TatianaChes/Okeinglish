package com.example.myapplicationtest.study;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationtest.R;
import java.util.ArrayList;
import java.util.List;

public class ListAdapterStudy extends RecyclerView.Adapter<ListAdapterStudy.ViewHolder>{

    interface OnStudentClickListener{
        void onStudentClick(Student student, int position);
    }
    private final OnStudentClickListener onClickListener;
    private LayoutInflater inflater;
    private  List<Student> students;
    Context context;

    public ListAdapterStudy(Context context, List<Student> students, OnStudentClickListener onClickListener)  {
        this.onClickListener = onClickListener;
        this.students = students;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void filterList(ArrayList<Student> filterdNames) {
        this.students = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout_study, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Student student = students.get(position);
        holder.Name.setText(student.getName());

        holder.Name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onStudentClick(student, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button Name;
        ViewHolder(View view){
            super(view);
            Name = view.findViewById(R.id.btnStudy);
        }
    }

}