package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myappcompany.steve.canvaspaint.data.SaveData;

import java.util.ArrayList;

public class SaveLoadRecyclerViewAdapter extends RecyclerView.Adapter<SaveLoadRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<SaveData> mSaves;
    private OnItemListener mOnItemListener;

    public SaveLoadRecyclerViewAdapter(Context context, ArrayList<SaveData> saves, OnItemListener onItemListener) {
        mContext = context;
        mSaves = saves;
        mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_load_list_item, parent, false);
        return new ViewHolder(view, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        String date = mContext.getString(R.string.saved_on, mSaves.get(position).getSaveDate());
        holder.saveDateTextView.setText(date);
        holder.saveNameTextView.setText(mSaves.get(position).getSaveName());
    }

    @Override
    public int getItemCount() {
        return mSaves.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView deleteImageView;
        TextView saveNameTextView, saveDateTextView;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            deleteImageView.setOnClickListener(this);
            saveNameTextView = itemView.findViewById(R.id.saveNameTextView);
            saveDateTextView = itemView.findViewById(R.id.saveDateTextView);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(View view, int position);
    }

}
