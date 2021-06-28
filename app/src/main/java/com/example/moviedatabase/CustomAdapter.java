package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.myViewHolder> implements Filterable {
    private Context mContext;
    private List<Result> mData;
    private onMovieListener mOnMovieListener;
    private List<Result> allData;
    public Filter filter = new Filter() {
        //run in background
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Result> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty())
                filteredList.addAll(allData);
            else
            {
                for(Result movie: allData)
                {
                    if(movie.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        System.out.println(movie.getTitle());
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        // run on UI Thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData.clear();
            mData.addAll((Collection<? extends Result>) results.values);
            notifyDataSetChanged();
        }
    };
    public CustomAdapter(Context mContext, List<Result> mData,onMovieListener onMovieListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.mOnMovieListener = onMovieListener;
        allData = new ArrayList<>(mData);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v= inflater.inflate(R.layout.movie_item,parent,false);
        return new myViewHolder(v,mOnMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
         holder.id.setText(mData.get(position).getTitle());
         holder.name.setText(mData.get(position).getVoteAverage().toString());
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w185" + mData.get(position).getPosterPath()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
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
