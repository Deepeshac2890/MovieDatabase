package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavCustomAdapter extends RecyclerView.Adapter<FavCustomAdapter.myViewHolder> {
    private Context mContext;
    private List<movieModal> mData;
    private onMovieListener mOnMovieListener;

    public FavCustomAdapter(Context mContext, List<movieModal> mData,onMovieListener onMovieListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOnMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v= inflater.inflate(R.layout.movie_item,parent,false);
        return new myViewHolder(v,mOnMovieListener);
    }


    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.id.setText(mData.get(position).getMovieTitle());
        holder.name.setText(mData.get(position).getRating().toString());
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w185" + mData.get(position).getPosterPath()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id;
        TextView name;
        ImageView image;
        onMovieListener onMovieListener;
        public myViewHolder(@NonNull View itemView, onMovieListener onMovieListener) {
            super(itemView);
            id = itemView.findViewById(R.id.textView);
            name = itemView.findViewById(R.id.textView2);
            image = itemView.findViewById(R.id.imageView);
            this.onMovieListener = onMovieListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }

    public interface onMovieListener{
        void onMovieClick(int position);
    }
}
