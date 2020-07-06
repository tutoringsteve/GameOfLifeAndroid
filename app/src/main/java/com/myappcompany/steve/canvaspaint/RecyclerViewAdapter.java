package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private ArrayList<String> mSaveNames;
    private ArrayList<String> mSaveDates;

    public RecyclerViewAdapter(Context context, ArrayList<String> saveNames, ArrayList<String> saveDates) {
        mContext = context;
        mSaveNames = saveNames;
        mSaveDates = saveDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_load_list_item, parent, false);

        //this is the instance of our inner class
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Delete at " + position);
                Toast.makeText(v.getContext(), "Delete at " + position, Toast.LENGTH_SHORT).show();
                removeAt(position);
            }
        });
        holder.saveDateTextView.setText(mSaveDates.get(position));
        holder.saveNameTextView.setText(mSaveNames.get(position));
    }

    private void removeAt(int position) {
        mSaveNames.remove(position);
        mSaveDates.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSaveNames.size();
    }


    //This inner class is responsible for holding the views in memory
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView deleteImageView;
        TextView saveNameTextView;
        TextView saveDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            saveNameTextView = itemView.findViewById(R.id.saveNameTextView);
            saveDateTextView = itemView.findViewById(R.id.saveDateTextView);
        }
    }
}
