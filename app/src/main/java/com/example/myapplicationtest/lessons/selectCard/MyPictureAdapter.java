package com.example.myapplicationtest.lessons.selectCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationtest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyPictureAdapter extends RecyclerView.Adapter<MyPictureAdapter.ViewHolder> {

    private List<PictureModel> datas;
    private OnTextClickListener onClickListener;
    private LayoutInflater inflater;

    interface OnTextClickListener{
        void onTextClick(PictureModel data, int position);
    }
    MyPictureAdapter(Context mContext, List<PictureModel> datas, OnTextClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(mContext);
        this.datas = datas;
    }

    @Override
    public MyPictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPictureAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // загрузка данных в полученные элементы
        PictureModel data = datas.get(position);
        Picasso.with(inflater.getContext()).load(data.getPicture()).into(holder.mImage);
        holder.mTitle.setText(data.getAnswer());
        // нажатие
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onTextClick(data, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // получение элементов с формочки
        final  ImageView mImage;
        final  TextView mTitle;
        final  CardView mCardView;
     ViewHolder(View itemView) {
        super(itemView);
        mCardView = itemView.findViewById(R.id.cardview);
        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
    }
}
}