package com.example.myapplicationtest.ui.textlist;
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

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    interface OnTextClickListener{
        void onTextClick(Text text, int position);
    }
    private final OnTextClickListener onClickListener;
    private LayoutInflater inflater;
    private  List<Text> texts;
    Context context;

    public ListAdapter(Context context, List<Text> texts, OnTextClickListener onClickListener)  {
        this.onClickListener = onClickListener;
        this.texts = texts;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void filterList(ArrayList<Text> filterdNames) {
        this.texts = filterdNames;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Text text = texts.get(position);
        holder.Name.setText(text.getName());
        switch (text.getUnit()){ // установка цвета фона, в зависимости от unit'a
            case 1:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.cyan));
                break;
            case 2:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.green));
                break;
            case 3:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                break;
            case 4:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.orange));
                break;
            case 5:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.red));
                break;
            case 6:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.purple));
                break;
            case 7:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.lightgreen));
                break;
            case 8:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.darkpurple));
                break;
            case 9:
               holder.Name.setBackgroundColor(context.getResources().getColor(R.color.lightbrown));
                break;
            case 10:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
            case 11:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.fon));
                break;
            case 12:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.gray));
                break;
            case 13:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.blue_background));
                break;
            case 14:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.darkgreen));
                break;
            default:
                holder.Name.setBackgroundColor(context.getResources().getColor(R.color.default_light_grey_bg));
                break;

        }

        holder.Name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onTextClick(text, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final Button Name;
        ViewHolder(View view){
            super(view);
            Name = view.findViewById(R.id.btn);
        }
    }

}