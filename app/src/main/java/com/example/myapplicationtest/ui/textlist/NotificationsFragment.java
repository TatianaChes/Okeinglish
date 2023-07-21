package com.example.myapplicationtest.ui.textlist;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplicationtest.ui.textlist.currentText.DatabaseHelper;
import com.example.myapplicationtest.translater.TextItselfActivity;
import com.example.myapplicationtest.databinding.FragmentNotificationsBinding;


import java.io.IOException;
import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    ArrayList<Text> texts = new ArrayList<Text>();
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    ListAdapter adapter;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentNotificationsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        // подключение к БД
         ConnectDB();
        // начальная инициализация списка
        setInitialData();

        // определяем слушателя нажатия элемента в списке
        ListAdapter.OnTextClickListener textClickListener = new ListAdapter.OnTextClickListener() {
            @Override
            public void onTextClick(Text text, int position) {

                Intent intent = new Intent(getContext(), TextItselfActivity.class);
                intent.putExtra(Text.class.getSimpleName(), text);
                startActivity(intent);

            }
        };

        binding.Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        // создаем адаптер
        adapter = new ListAdapter(getContext(), texts, textClickListener);
        // устанавливаем для списка адаптер
       binding.RecyclerView.setAdapter(adapter);

        return root;
    }

    private void ConnectDB() {
        mDBHelper = new DatabaseHelper(getContext());
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    private void filter(String text) {
        //новый список массивов, который будет содержать отфильтрованные данные
        ArrayList<Text> filterdNames = new ArrayList<>();
        //перебор существующих элементов
        for (Text s : texts) {
            //если существующие элементы содержат входные данные для поиска
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
            //добавление элемента в отфильтрованный список
                filterdNames.add(s);
            }
        }
        //вызов метода класса adapter и передача отфильтрованного списка
        adapter.filterList(filterdNames);
    }
    private void setInitialData(){

        Cursor cursor = mDb.rawQuery("SELECT * FROM texts ORDER BY id DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            texts.add(new Text (cursor.getString(2), cursor.getInt(1), cursor.getString(3), cursor.getString(4)));
            cursor.moveToNext();
        }
        cursor.close();
    }
@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}